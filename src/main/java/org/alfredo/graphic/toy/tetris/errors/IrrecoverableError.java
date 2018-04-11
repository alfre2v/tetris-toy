package org.alfredo.graphic.toy.tetris.errors;

public class IrrecoverableError extends RuntimeException {

    public IrrecoverableError() {
        super();
    }

    public IrrecoverableError (String message) {
        super (message);
    }

    public IrrecoverableError (Throwable cause) {
        super (cause);
    }

    public IrrecoverableError (String message, Throwable cause) {
        super (message, cause);
    }

}
