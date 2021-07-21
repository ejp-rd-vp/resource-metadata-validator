package org.ejprd;

public class Welcome {
    private final long id;
    private final String content;

    public Welcome(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
