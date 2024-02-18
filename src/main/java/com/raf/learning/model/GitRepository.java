package com.raf.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GitRepository {
    @Id
    private String id;

    private String url;

    public GitRepository(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
