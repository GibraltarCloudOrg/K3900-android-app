package com.example.mauiviewer;

class ImageNotAvailableException extends RuntimeException {
    ImageNotAvailableException(String errorMessage) {
        super(errorMessage);
    }
}
