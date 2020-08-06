package com.example.mauiviewer;

class Image {
    private byte[] data;
    private long time;

    Image(byte[] data, long time) {
        this.data = data;
        this.time = time;
    }

    long getTime() {
        return this.time;
    }

    byte[] getData() {
        return this.data;
    }
}
