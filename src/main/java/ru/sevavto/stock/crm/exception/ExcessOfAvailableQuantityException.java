package ru.sevavto.stock.crm.exception;

public class ExcessOfAvailableQuantityException extends RuntimeException{
    public ExcessOfAvailableQuantityException(String message) {
        super(message);
    }
}
