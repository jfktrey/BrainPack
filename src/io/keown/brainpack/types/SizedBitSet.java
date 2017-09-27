package io.keown.brainpack.types;

import java.util.BitSet;

/**
 * An implementation of BitSet that has a minimum length.
 *
 * @author      Trey Keown <trey @ keown.io>
 * @since       1.0
 */
public class SizedBitSet extends BitSet {
    /**
     * The internal field holding the minimum size to return as the
     * {@code BitSet} length.
     */
    private Integer minimumSize;

    /**
     * Creates a new {@code SizedBitSet}. All bits are initially zero.
     *
     * @param minimumSize the minimum size to return as the BitSet length
     */
    public SizedBitSet(Integer minimumSize) {
        this(0, minimumSize);
    }

    /**
     * Creates a new {@code SizedBitSet}. Initializes with the specified value.
     *
     * @param initialValue initial value for the BitSet
     * @param minimumSize the minimum size to return as the BitSet length
     */
    public SizedBitSet(long initialValue, Integer minimumSize) {
        this(new long[]{ initialValue }, minimumSize);
    }

    /**
     * Creates a new {@code SizedBitSet}. Initializes with the specified value.
     *
     * @param initialValue initial value for the BitSet
     * @param minimumSize the minimum size to return as the BitSet length
     */
    public SizedBitSet(long[] initialValue, Integer minimumSize) {
        super();
        set(initialValue);
        this.minimumSize = minimumSize;
    }

    /**
     * Returns the length of the {@code SizedBitSet}.
     *
     * @return an {@code int} corresponding to the larger of the actual {@code BitSet}
     *         length or the minimum size
     */
    @Override
    public int length() {
        return Math.max(super.length(), minimumSize);
    }

    /**
     * Sets the value of this {@code SizedBitSet} to the specified value.
     */
    private void set(long[] initialValue) {
        this.clear();
        this.or(BitSet.valueOf(initialValue));
    }
}
