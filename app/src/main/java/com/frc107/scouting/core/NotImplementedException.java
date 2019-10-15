package com.frc107.scouting.core;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException(String message) {
        super(message + " not implemented.");
    }
}
