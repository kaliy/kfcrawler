package org.kaliy.kfcrawler.data;

public enum Scheme {
    HTTP, HTTPS;

    public String toString() {
        return name().toLowerCase();
    }
}
