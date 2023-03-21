package ru.sevavto.stock.crm.exception;

public class NotValidRefreshTokenException extends RuntimeException{
    public NotValidRefreshTokenException(String message) {
        super(message);
    }
}
