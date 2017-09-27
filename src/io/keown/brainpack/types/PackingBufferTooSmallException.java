package io.keown.brainpack.types;

/**
 * Thrown when attempting to write bits into a buffer which is too small to
 * hold all of them.
 *
 * @author  Trey Keown <trey @ keown.io>
 * @since   1.0
 */
public class PackingBufferTooSmallException extends Exception {

    /**
     * Constructs a {@code PackingBufferTooSmallException}
     */
    public PackingBufferTooSmallException() {
        super();
    }

    /**
     * Constructs a {@code PackingBufferTooSmallException} with the
     * specified message.
     *
     * @param   message   the message
     */
    public PackingBufferTooSmallException(String message) {
        super(message);
    }
}
