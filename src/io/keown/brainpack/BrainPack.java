package io.keown.brainpack;

import io.keown.brainpack.types.CharacterEncodingMap;
import io.keown.brainpack.types.PackingBufferTooSmallException;
import io.keown.brainpack.types.SizedBitSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class BrainPack {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Four arguments are required.");
            System.out.println("Usage: java -jar brainpack.jar source mapping filesize output");
            System.out.println("    source   - Brainfuck file to be parsed");
            System.out.println("    mapping  - Mapping file from Brainfuck instruction to encoded form");
            System.out.println("    filesize - Size of generated file");
            System.out.println("    output   - Output filename");
            System.exit(1);
        }

        String source = null;
        try {
            source = new String(readAllBytes(get(args[0])));
        } catch (IOException e) {
            System.out.println("Unable to read source file.");
            System.exit(1);
        }

        Map<Character, SizedBitSet> encodingMap = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[1]));
            String line = reader.readLine();
            Integer bitWidth = null;

            try {
                bitWidth = Integer.valueOf(line);
            } catch (NumberFormatException e) {
                System.out.println("Mapping file incorrect format. Error parsing bit width.");
                System.exit(1);
            }

            while ((line = reader.readLine()) != null) {
                List<String> lineParts = Arrays.stream(line.split("[ \t]*"))
                        .filter(part -> !part.isEmpty())
                        .collect(Collectors.toList());

                if (lineParts.size() != 2) {
                    System.out.println("Mapping file incorrect format. Too many parts per line.");
                    System.exit(1);
                }

                if (lineParts.get(0).length() != 1) {
                    System.out.println("Mapping file incorrect format. Too many characters in first column.");
                    System.exit(1);
                }

                Character mapKey = lineParts.get(0).charAt(0);
                Long mapValue = null;
                try {
                    mapValue = Long.valueOf(lineParts.get(1));
                } catch (NumberFormatException e) {
                    System.out.println("Mapping file incorrect format. Unable to parse encoding.");
                    System.exit(1);
                }

                encodingMap.put(mapKey, new SizedBitSet(mapValue, bitWidth));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Unable to read mapping file.");
            System.exit(1);
        }

        Integer fileSize = null;
        try {
            fileSize = Integer.valueOf(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("File size must be an integer.");
            System.exit(1);
        }

        Packer packer = new Packer(source, new CharacterEncodingMap(encodingMap));
        byte[] output = null;
        try {
            output = packer.pack(fileSize, ',');
        } catch (PackingBufferTooSmallException e) {
            System.out.println("File size is too small.");
            System.exit(1);
        }

        try {
            Files.write(new File(args[3]).toPath(), output);
        } catch (IOException e) {
            System.out.println("Unable to open output file for writing.");
            System.exit(1);
        }
    }
}
