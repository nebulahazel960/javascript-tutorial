package org.dromara.fai.exception;

public class FaiDuplicateToolNameException extends FaiRuntimeException {

    public FaiDuplicateToolNameException(String name) {
        super("Duplicate tool name: '" + name + "'");
    }
}
