package com.appodeal.gdx.callbacks;

public interface SkippableVideoCallback {
    void onSkippableVideoLoaded();
    void onSkippableVideoFailedToLoad();
    void onSkippableVideoShown();
    void onSkippableVideoFinished();
    void onSkippableVideoClosed();
}
