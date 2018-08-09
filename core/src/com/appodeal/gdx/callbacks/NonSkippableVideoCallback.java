package com.appodeal.gdx.callbacks;

public interface NonSkippableVideoCallback {
    void onNonSkippableVideoLoaded();
    void onNonSkippableVideoFailedToLoad();
    void onNonSkippableVideoShown();
    void onNonSkippableVideoFinished();
    void onNonSkippableVideoClosed();
}
