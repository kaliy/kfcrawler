package org.kaliy.kfcrawler.data;

public class Url {
    //TODO: split elements
    public String url;

    public static Url fromString(String url) {
        return new Url().setUrl(url);
    }

    public static Url fromString(String url, Url parent) {
        if (url.startsWith("http")) {
            return new Url().setUrl(url);
        } else {
            return new Url().setUrl(parent.toString().split("http(s)?://.*?/")[0] + url);
        }
    }

    public String getUrl() {
        return url;
    }

    public Url setUrl(String url) {
        this.url = url;
        return this;
    }

    public String toString() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Url)) return false;

        Url url1 = (Url) o;

        if (url != null ? !url.equals(url1.url) : url1.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
