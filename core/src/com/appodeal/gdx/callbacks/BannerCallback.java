package com.appodeal.gdx.callbacks;

public interface BannerCallback {
    void onBannerLoaded();
    void onBannerFailedToLoad();
    void onBannerShown();
    void onBannerClicked();
}