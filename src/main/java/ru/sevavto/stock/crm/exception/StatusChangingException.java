package ru.sevavto.stock.crm.exception;

public class StatusChangingException extends RuntimeException{
    public StatusChangingException(String message) {
        super(message);
    }
}
