package org.dromara.fai.exception;

public class FaiToolException extends FaiRuntimeException {
    
    public FaiToolException(Throwable cause) {
        super("Failed to call the tool, cause: " + cause.getMessage(), cause);
    }
}
