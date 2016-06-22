package com.vinci.imageloader.core.exception;

/**
 * Created by SpringXu on 15/9/1.
 */
public class VinciException extends Exception {


    /**
     * Returns the cause of this {@code Throwable}, or {@code null} if there is
     * no cause.
     */
    @Override
    public Throwable getCause() {
        return super.getCause();
    }

    /**
     * Returns the detail message which was provided when this
     * {@code Throwable} was created. Returns {@code null} if no message was
     * provided at creation time.
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * Returns the detail message which was provided when this
     * {@code Throwable} was created. Returns {@code null} if no message was
     * provided at creation time. Subclasses may override this method to return
     * localized text for the message. Android returns the regular detail message.
     */
    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage the detail message for this exception.
     */
    public VinciException(String detailMessage) {
        super("VinciException:" + detailMessage);
    }

    /**
     * Constructs a new {@code Exception} that includes the current stack trace.
     */
    public VinciException() {
        super();
    }
}
