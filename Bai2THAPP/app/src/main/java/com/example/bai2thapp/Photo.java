package com.example.bai2thapp;

import android.net.Uri;

public class Photo {
    private int resourceId = -1;
    private Uri uri = null;
    private String name;
    private boolean isFavorite = false;

    public Photo(int resourceId, String name) {
        this.resourceId = resourceId;
        this.name = name;
    }

    public Photo(Uri uri, String name) {
        this.uri = uri;
        this.name = name;
    }

    public int getResourceId() { return resourceId; }
    public Uri getUri() { return uri; }
    public String getName() { return name; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public boolean isFromUri() { return uri != null; }
}
