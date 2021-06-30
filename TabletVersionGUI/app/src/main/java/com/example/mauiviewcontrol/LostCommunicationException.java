package com.example.mauiviewcontrol;

class LostCommunicationException extends RuntimeException {
    LostCommunicationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
