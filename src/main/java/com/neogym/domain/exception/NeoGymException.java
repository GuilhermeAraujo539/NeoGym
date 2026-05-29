package com.neogym.domain.exception;

public class NeoGymException extends RuntimeException {
    public NeoGymException(String message) { super(message); }
    public NeoGymException(String message, Throwable cause) { super(message, cause); }
}
