package br.edu.imd.apidetectorplagio.detector.model;

import com.google.gson.JsonObject;

public class MetaDataPage {

    private String kind;
    private String title;
    private String link;
    private String snippet;
    private String htmlSnippet;

    public MetaDataPage(String kind, String title, String link, String snippet, String htmlSnippet) {
        this.kind = kind;
        this.title = title;
        this.link = link;
        this.snippet = snippet;
        this.htmlSnippet = htmlSnippet;
    }

    public String getKind() {
        return kind;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getHtmlSnippet() {
        return htmlSnippet;
    }

    public static class Builder {
        private String kind;
        private String title;
        private String link;
        private String snippet;
        private String htmlSnippet;

        public Builder kind(String kind) {
            this.kind = kind;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public Builder snippet(String snippet) {
            this.snippet = snippet;
            return this;
        }

        public Builder htmlSnippet(String htmlSnippet) {
            this.htmlSnippet = htmlSnippet;
            return this;
        }

        public MetaDataPage build(){
            return new MetaDataPage(kind, title, link, snippet, htmlSnippet);
        }

    }
}
