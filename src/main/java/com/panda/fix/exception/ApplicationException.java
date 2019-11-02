package com.panda.fix.exception;

import java.io.IOException;

public class ApplicationException extends RuntimeException {

    public ApplicationException(){

    }

    public ApplicationException(String message){
        super(message);
    }

    public ApplicationException(String message, Throwable cause){
        super(message, cause);
    }

    public ApplicationException(Throwable e) {
        super(e);
    }
}
