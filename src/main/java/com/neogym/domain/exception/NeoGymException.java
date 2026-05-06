package com.neogym.domain.exception;

/** Exceção base de negócio do NeoGym. */
public class NeoGymException extends RuntimeException {
    public NeoGymException(String message) { super(message); }
    public NeoGymException(String message, Throwable cause) { super(message, cause); }
}
