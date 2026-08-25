package org.dromara.fai.exception;

public class FaiRuntimeException extends RuntimeException {
    
    public FaiRuntimeException(final String message) {
        super(message);
    }
    
    public FaiRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public FaiRuntimeException(final Throwable cause) {
        super(cause);
    }
    
}
