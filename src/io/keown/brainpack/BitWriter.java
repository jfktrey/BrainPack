package io.keown.brainpack;

import io.keown.brainpack.types.SizedBitSet;

import java.nio.BufferOverflowException;
import java.util.Arrays;

/**
 * Write multiple {@code SizedBitSet}s into a buffer bit by bit. Values are
 * written starting with the byte at index 0. Within each byte, bits are
 * written least-significant bit to most-significant bit. Bits are written
 * sequentially, and after performing a write, the next value will be
 * written to the bits immediately following the previous write.
 *
 * @author      Trey Keown <trey @ keown.io>
 * @version     1.0
 * @since       1.0
 */
public class BitWriter {
    /**
     * The internal field corresponding to the bits which have been written.
     */
    private byte[] buffer;

    /**
     * The internal fields pointing to the next bit to be written in the buffer.
     */
    private Integer bufferIndex;
    private Integer byteIndex;

    /**
     * Creates a new bit writer. All bits are initially zero.
     *
     * @param byteArrayLength the number of bytes in the buffer used for holding
     *                        all the bits which will be written
     */
    public BitWriter(Integer byteArrayLength) {
        this.buffer = new byte[byteArrayLength];
        this.bufferIndex = 0;
        this.byteIndex = 0;
    }

    /**
     * Writes a {@code SizedBitSet} into the buffer, and throws a
     * {@code BufferOverflowException} if the buffer is already full or becomes
     * over-filled as a result of this write.
     *
     * @param bits a {@code SizedBitSet} containing the bits to be
     *             written into the buffer
     * @since 1.0
     */
    public void write(SizedBitSet bits) {
        write(bits, true);
    }

    /**
     * Writes a {@code SizedBitSet} into the buffer. If
     * {@code failOnOverflow} is true, throws a {@code BufferOverflowException}
     * if the buffer is already full or becomes over-filled as a result of this
     * write. If {@code failOnOverflow} is false, writing ceases when the
     * buffer becomes full, and the number of bytes written is returned. In the
     * normal case where all bytes are written, null is returned.
     *
     * @param bits a {@code SizedBitSet} containing the bits to be
     *             written into the buffer
     * @param failOnOverflow if set, throws a {@code BufferOverflowException}
     *                       when attempting to write past the end of the buffer
     * @throws BufferOverflowException when attempting overfill the buffer
     * @since 1.0
     */
    public Integer write(SizedBitSet bits, Boolean failOnOverflow) {
        for (int i = 0; i < bits.length(); i++) {
            if (isFilled()) {
                if (failOnOverflow) {
                    throw new BufferOverflowException();
                } else {
                    return i;
                }
            }

            if (bits.get(i)) {
                buffer[bufferIndex] |= (1 << byteIndex);
            }

            if (++byteIndex == 8) {
                byteIndex = 0;
                bufferIndex++;
            }
        }

        return null;
    }

    /**
     * Returns a new byte array representing the buffer in its entirety.
     *
     * @return a byte array equivalent to the internal buffer, containing all
     *         bits written so far, and bits which have not yet been written
     *         initialized to zero
     * @since 1.7
     */
    public byte[] readBuffer() {
        return Arrays.copyOf(buffer, buffer.length);
    }

    /**
     * Returns a whether or not all bits in the buffer have been written.
     *
     * @return a boolean representing whether or not all bits have been written
     * @since 1.7
     */
    public boolean isFilled() {
        return bufferIndex > (buffer.length - 1);
    }
}
