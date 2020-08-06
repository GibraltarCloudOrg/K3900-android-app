package com.example.mauiviewer;

class LostCommunicationException extends RuntimeException {
    LostCommunicationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
