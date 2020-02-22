package com.example.androidhellomvi.View;

public class MainViewState {


    boolean isLoading;
    boolean isImageViewShow;
    String imagelink;
    Throwable error;

    public MainViewState(boolean isLoading, boolean isImageViewShow, String imagelink, Throwable error) {
        this.isLoading = isLoading;
        this.isImageViewShow = isImageViewShow;
        this.imagelink = imagelink;
        this.error = error;
    }
}
