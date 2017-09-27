package io.keown.brainpack.types;

import java.util.Map;

/**
 * A class mapping a {@code Character} (to be encoded) to a {@code SizedBitSet}.
 *
 * @author      Trey Keown <trey @ keown.io>
 * @since       1.0
 */
public class CharacterEncodingMap {
    /**
     * The internal field mapping each {@code Character} (to be encoded) to
     * a {@code SizedBitSet}.
     */
    private Map<Character, SizedBitSet> encodingMap;

    /**
     * The internal field holding length (in bits) of the encoding result for
     * each character.
     */
    private Integer encodedLength;

    /**
     * Creates a new {@code CharacterEncodingMap}. Infers the encoding length
     * for each character from the maximum SizedBitSet length in the {@code encodingMap}
     *
     * @param encodingMap the mapping from each {@code Character} (to be
     *                    encoded) to the corresponding {@code SizedBitSet}
     */
    public CharacterEncodingMap(Map<Character, SizedBitSet> encodingMap) {
        this.encodingMap = encodingMap;
        this.encodedLength = encodingMap.values().stream()
                .map(SizedBitSet::length)
                .max(Integer::compare)
                .orElse(0);
    }

    /**
     * Creates a new {@code CharacterEncodingMap}.
     *
     * @param encodingMap the mapping from each {@code Character} (to be
     *                    encoded) to the corresponding {@code SizedBitSet}
     * @param encodedLength the length (in bits) of the encoding result for
     *                      each character
     */
    public CharacterEncodingMap(Map<Character, SizedBitSet> encodingMap, Integer encodedLength) {
        this.encodingMap = encodingMap;
        this.encodedLength = encodedLength;
    }

    /**
     * Returns the {@code CharacterEncodingMap} encoded value length (in bits).
     *
     * @return an {@code int} corresponding to the length of the encoded value
     * length (in bits).
     */
    public Integer length() {
        return encodedLength;
    }

    /**
     * Returns the mapping from {@code Character} to {@code SizedBitSet}.
     *
     * @return a mapping from each {@code Character} (to be encoded) to
     * a {@code SizedBitSet}.
     */
    public Map<Character, SizedBitSet> getMapping() {
        return encodingMap;
    }
}
