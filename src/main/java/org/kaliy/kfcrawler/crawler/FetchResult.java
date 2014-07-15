package org.kaliy.kfcrawler.crawler;

public class FetchResult {

    private String html;
    private boolean successful = true;

    public static final FetchResult FAILED = new FetchResult(false);

    public FetchResult(String html) {
        this.html = html;
    }

    public FetchResult(boolean successful) {
        this.successful = successful;
    }

    public String getHtml() {
        return html;
    }

    public FetchResult setHtml(String html) {
        this.html = html;
        return this;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public FetchResult setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }
}
