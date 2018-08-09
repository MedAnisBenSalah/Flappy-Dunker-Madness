package com.appodeal.gdx.callbacks;

public interface InterstitialCallback {
    void onInterstitialLoaded(boolean isPrecache);
    void onInterstitialFailedToLoad();
    void onInterstitialShown();
    void onInterstitialClicked();
    void onInterstitialClosed();
}
