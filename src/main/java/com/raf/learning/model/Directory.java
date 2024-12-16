package com.raf.learning.model;

public class Directory {
    private String path;
    private boolean isCreated;

    // Constructors
    public Directory() {}

    public Directory(String path, boolean isCreated) {
        this.path = path;
        this.isCreated = isCreated;
    }

    // Getters and Setters
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
}
