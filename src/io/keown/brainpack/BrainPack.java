package io.keown.brainpack;

import io.keown.brainpack.types.CharacterEncodingMap;
import io.keown.brainpack.types.PackingBufferTooSmallException;
import io.keown.brainpack.types.SizedBitSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BrainPack {
    public static void main(String[] args) {
        final String source = "<h+e-l.l,[]>! \naaa.";

        final Map<Character, SizedBitSet> encoding = new HashMap<>();
        encoding.put('.', new SizedBitSet(0b0000, 4));
        encoding.put(',', new SizedBitSet(0b0001, 4));
        encoding.put('+', new SizedBitSet(0b0010, 4));
        encoding.put('-', new SizedBitSet(0b0011, 4));
        encoding.put('<', new SizedBitSet(0b0100, 4));
        encoding.put('>', new SizedBitSet(0b0101, 4));
        encoding.put('[', new SizedBitSet(0b0110, 4));
        encoding.put(']', new SizedBitSet(0b0111, 4));

        Packer packer = new Packer(source, new CharacterEncodingMap(encoding));
        byte[] output = new byte[]{};
        try {
            output = packer.pack(1024, ',');
        } catch (PackingBufferTooSmallException e) {}

        try {
            Files.write(Paths.get("/Users/treykeown/test.bin"), output);
        } catch (IOException e) {

        }
    }
}
