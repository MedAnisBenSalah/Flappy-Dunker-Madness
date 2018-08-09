package com.appodeal.gdx;

import com.appodeal.gdx.callbacks.BannerCallback;
import com.appodeal.gdx.callbacks.InterstitialCallback;
import com.appodeal.gdx.callbacks.NonSkippableVideoCallback;
import com.appodeal.gdx.callbacks.PermissionCallback;
import com.appodeal.gdx.callbacks.RewardedVideoCallback;
import com.appodeal.gdx.callbacks.SkippableVideoCallback;
import com.appodeal.gdx.data.UserSettings;
import com.badlogic.gdx.Gdx;

import static com.appodeal.gdx.GdxAppodeal.TAG;

public class DisabledGdxAppodeal implements AppodealInterface {

    @Override
    public void initialize(String appId, int type) {
        disabledMessage();
    }

    @Override
    public void setInterstitialCallbacks(InterstitialCallback listener) {
        disabledMessage();
    }

    @Override
    public void setBannerCallbacks(BannerCallback listener) {
        disabledMessage();
    }

    @Override
    public void setSkippableVideoCallbacks(SkippableVideoCallback listener) {
        disabledMessage();
    }

    @Override
    public void setNonSkippableVideoCallbacks(NonSkippableVideoCallback listener) {
        disabledMessage();
    }

    @Override
    public void setRewardedVideoCallbacks(RewardedVideoCallback listener) {
        disabledMessage();
    }

    @Override
    public boolean show(int type) {
        disabledMessage();
        return false;
    }

    @Override
    public boolean show(int type, String placement) {
        disabledMessage();
        return false;
    }

    @Override
    public void setAutoCache(int type, boolean autoCache) {
        disabledMessage();
    }

    @Override
    public void setOnLoadedTriggerBoth(int type, boolean onLoadedTriggerBoth) {
        disabledMessage();
    }

    @Override
    public boolean isLoaded(int type) {
        disabledMessage();
        return false;
    }

    @Override
    public boolean isPreCache(int type) {
        disabledMessage();
        return false;
    }

    @Override
    public void cache(int type) {
        disabledMessage();
    }

    @Override
    public void hide(int type) {
        disabledMessage();
    }

    @Override
    public void confirm(int type) {
        disabledMessage();
    }

    @Override
    public void disableLocationPermissionCheck() {
        disabledMessage();
    }

    @Override
    public void disableNetwork(String name) {
        disabledMessage();
    }

    @Override
    public void disableNetwork(String name, int type) {
        disabledMessage();
    }

    @Override
    public String getVersion() {
        disabledMessage();
        return "invalid";
    }

    @Override
    public void setTesting(boolean testing) {
        disabledMessage();
    }

    @Override
    public void setLogLevel(GdxAppodeal.LogLevel level) {
        disabledMessage();
    }

    @Override
    public UserSettings getUserSettings() {
        disabledMessage();
        return null;
    }

    @Override
    public void disableWriteExternalStoragePermissionCheck() {
        disabledMessage();
    }

    @Override
    public void requestAndroidMPermissions(PermissionCallback permissionCallback) {
        disabledMessage();
    }

    @Override
    public void set728x90Banners(boolean b) {
        disabledMessage();
    }

    @Override
    public void setBannerAnimation(boolean b) {
        disabledMessage();
    }

    @Override
    public void setCustomRule(String s, boolean b) {
        disabledMessage();
    }

    @Override
    public void setCustomRule(String s, double v) {
        disabledMessage();
    }

    @Override
    public void setCustomRule(String s, int i) {
        disabledMessage();
    }

    @Override
    public void setCustomRule(String s1, String s2) {
        disabledMessage();
    }

    @Override
    public void setSmartBanners(boolean b) {
        disabledMessage();
    }

    @Override
    public void trackInAppPurchase(double v, String s) {
        disabledMessage();
    }


    private void disabledMessage() {
        Gdx.app.debug(TAG, "GdxAppodeal not loaded or disabled!");
    }

}
