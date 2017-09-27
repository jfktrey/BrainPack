package io.keown.brainpack;

import io.keown.brainpack.types.CharacterEncodingMap;
import io.keown.brainpack.types.PackingBufferTooSmallException;
import io.keown.brainpack.types.SizedBitSet;

import java.nio.BufferOverflowException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Converts an input string into a byte array according to character encodings
 * defined in an input CharacterEncodingMap.
 *
 * @author      Trey Keown <trey @ keown.io>
 * @version     1.0
 * @since       1.0
 */
public class Packer {
    /* Number of bits in a byte */
    private static final Integer BYTE_SIZE = 8;

    /**
     * The internal field which holds the input string.
     */
    private String source;

    /**
     * The internal field used for mapping characters in the input string to a {@code SizedBitSet}.
     */
    private CharacterEncodingMap encoding;

    /**
     * Creates a new Packer.
     *
     * @param input the string which will be encoded
     * @param encoding a mapping from all characters to be encoded to a {@code SizedBitSet}
     */
    public Packer(String input, CharacterEncodingMap encoding) {
        List<Integer> encodingLengths = encoding.getMapping().values().stream()
                .map(SizedBitSet::length)
                .distinct()
                .collect(Collectors.toList());

        if (encodingLengths.size() < 1) {
            throw new IllegalArgumentException("Instruction encoding map contains no entries.");
        }

        this.source = input;
        this.encoding = encoding;

        if ((BYTE_SIZE % encoding.length() != 0) && (encoding.length() % BYTE_SIZE != 0)) {
            throw new IllegalArgumentException("Encoded instruction length does not evenly pack into a byte.");
        }
    }

    /**
     * Using the input string and specified character encoding, generate an
     * array of bytes. If a character in the string is a key in the encoding
     * map, it will be encoded, otherwise it is ignored. If the generated array
     * is shorter than the desired size, it is padded using {@code filler} until
     * it's the correct size.
     *
     * @param arraySizeBytes the size of the resulting array generated
     * @param filler a {@code SizedBitSet} used to pad the array up to the
     *               desired size (if necessary)
     * @throws PackingBufferTooSmallException if the specified array size is too
     *                                        small to hold the generated encoding
     * @since 1.0
     */
    public byte[] pack(Integer arraySizeBytes, SizedBitSet filler) throws PackingBufferTooSmallException {
        if (filler.length() > encoding.length()) {
            throw new IllegalArgumentException("Filler data length is too large.");
        }
        if (arraySizeBytes % encoding.length() != 0) {
            throw new IllegalArgumentException("Requested file size is not a multiple of encoded instruction length.");
        }

        BitWriter writer = new BitWriter(arraySizeBytes);

        try {
            source.chars()
                    .mapToObj(letterInt -> (char) letterInt)
                    .map(character -> encoding.getMapping().get(character))
                    .filter(Objects::nonNull)
                    .forEachOrdered(bitSet -> writer.write(bitSet));
        } catch (BufferOverflowException e) {
            throw new PackingBufferTooSmallException("The specified array size ("
                    + arraySizeBytes
                    + ") is too small to hold the generated encoding.");
        }

        while (!writer.isFilled()) {
            writer.write(filler);
        }

        return writer.readBuffer();
    }

    /**
     * Using the input string and specified character encoding, generate an
     * array of bytes. If a character in the string is a key in the encoding
     * map, it will be encoded, otherwise it is ignored. If the generated array
     * is shorter than the desired size, it is padded using {@code filler} until
     * it's the correct size.
     *
     * @param arraySizeBytes the size of the resulting file generated
     * @param fillerCharacter the character which will be used to pad the array
     *                        up to the desired size (if necessary)
     * @throws PackingBufferTooSmallException if the specified array size is too
     *                                        small to hold the generated encoding
     * @since 1.0
     */
    public byte[] pack(Integer arraySizeBytes, Character fillerCharacter) throws PackingBufferTooSmallException {
        if (!encoding.getMapping().keySet().contains(fillerCharacter)) {
            throw new IllegalArgumentException("Specified filler character " + fillerCharacter + " was not found in encoding map.");
        }

        return pack(arraySizeBytes, encoding.getMapping().get(fillerCharacter));
    }
}
