package com.frc107.vanguard.core;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException(String message) {
        super(message + " not implemented.");
    }
}
