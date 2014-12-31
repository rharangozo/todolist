package rh.persistence.service;

public class NoFreeOrderException extends RuntimeException {

    public NoFreeOrderException(String string) {
        super(string);
    }

    public NoFreeOrderException() {
    }  
}
