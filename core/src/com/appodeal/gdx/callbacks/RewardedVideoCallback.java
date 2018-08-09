package com.appodeal.gdx.callbacks;

public interface RewardedVideoCallback {
    void onRewardedVideoLoaded();
    void onRewardedVideoFailedToLoad();
    void onRewardedVideoShown();
    void onRewardedVideoFinished(int amount, String name);
    void onRewardedVideoClosed();
}
