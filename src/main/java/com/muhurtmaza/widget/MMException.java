package com.muhurtmaza.widget;

/**
 * Created by Sandeep on 11/22/15.
 */
public class MMException extends Exception {

    /**
     * Constructs a new {@code Exception} that includes the current stack trace.
     */
    public MMException() {
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage
     *            the detail message for this exception.
     */
    public MMException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace, the
     * specified detail message and the specified cause.
     *
     * @param detailMessage
     *            the detail message for this exception.
     * @param throwable
     *            the cause of this exception.
     */
    public MMException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified cause.
     *
     * @param throwable
     *            the cause of this exception.
     */
    public MMException(Throwable throwable) {
        super(throwable);
    }
}
