package com.example.devicesApi.devicesApi.exceptions;


public class DeviceInUseException extends RuntimeException {
    public DeviceInUseException(String message) {
        super(message);
    }
}