package com.appodeal.gdx.android;

import android.app.Activity;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NonSkippableVideoCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.utils.Log;
import com.appodeal.ads.utils.PermissionsHelper;
import com.appodeal.gdx.AppodealInterface;
import com.appodeal.gdx.GdxAppodeal;
import com.appodeal.gdx.callbacks.BannerCallback;
import com.appodeal.gdx.callbacks.InterstitialCallback;
import com.appodeal.gdx.callbacks.NonSkippableVideoCallback;
import com.appodeal.gdx.callbacks.PermissionCallback;
import com.appodeal.gdx.callbacks.RewardedVideoCallback;
import com.appodeal.gdx.callbacks.SkippableVideoCallback;
import com.appodeal.gdx.data.UserSettings;
import com.badlogic.gdx.Gdx;

public class AndroidGdxAppodeal  implements AppodealInterface, BannerCallbacks, InterstitialCallbacks, RewardedVideoCallbacks, NonSkippableVideoCallbacks, PermissionsHelper.AppodealPermissionCallbacks {
    private final Activity activity;
    private InterstitialCallback interstitialListener;
    private BannerCallback bannerListener;
    private SkippableVideoCallback skippableVideoListener;
    private NonSkippableVideoCallback nonSkippableVideoListener;
    private RewardedVideoCallback rewardedVideoListener;
    private PermissionCallback permissionListener;
    private AndroidUserSettings userSettings;

    private boolean isShow;

    public AndroidGdxAppodeal(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void initialize(String appId, final int type) {
        Appodeal.initialize(activity, appId, type);
    }

    @Override
    public void setInterstitialCallbacks(InterstitialCallback listener) {
        Appodeal.setInterstitialCallbacks(this);
        interstitialListener = listener;
    }

    @Override
    public void setBannerCallbacks(BannerCallback listener) {
        Appodeal.setBannerCallbacks(this);
        bannerListener = listener;
    }

    @Override
    public void setSkippableVideoCallbacks(SkippableVideoCallback listener) {
        //Appodeal.setSkippableVideoCallback(this);
        skippableVideoListener = listener;
    }

    @Override
    public void setNonSkippableVideoCallbacks(NonSkippableVideoCallback listener) {
        Appodeal.setNonSkippableVideoCallbacks(this);
        nonSkippableVideoListener = listener;
    }

    @Override
    public void setRewardedVideoCallbacks(RewardedVideoCallback listener) {
        Appodeal.setRewardedVideoCallbacks(this);
        rewardedVideoListener = listener;
    }

    @Override
    public boolean show(final int type) {
        isShow = false;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isShow = Appodeal.show(activity, type);
            }
        });
        return isShow;
    }

    @Override
    public boolean show(final int type, final String placement) {
        isShow = false;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isShow = Appodeal.show(activity, type, placement);
            }
        });
        return isShow;
    }

    @Override
    public void setAutoCache(int type, boolean autoCache) {
        Appodeal.setAutoCache(type, autoCache);
    }

    @Override
    public void setOnLoadedTriggerBoth(int type, boolean onLoadedTriggerBoth) {
       // Appodeal.setOnLoadedTriggerBoth(type, onLoadedTriggerBoth);
    }

    @Override
    public boolean isLoaded(int type) {
        return Appodeal.isLoaded(type);
    }

    @Override
    public boolean isPreCache(int type) {
        return Appodeal.isPrecache(type);
    }

    @Override
    public void cache(int type) {
        Appodeal.cache(activity, type);
    }

    @Override
    public void hide(final int type) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Appodeal.hide(activity, type);
            }
        });
    }

    @Override
    public void confirm(int type) {

    }

    @Override
    public void disableLocationPermissionCheck() {
        Appodeal.disableLocationPermissionCheck();
    }

    @Override
    public void disableNetwork(String name) {
        Appodeal.disableNetwork(activity, name);
    }

    @Override
    public void disableNetwork(String name, int type) {
        Appodeal.disableNetwork(activity, name, type);
    }

    @Override
    public String getVersion() {
        return Appodeal.getVersion();
    }

    @Override
    public void setTesting(boolean testing) {
        Appodeal.setTesting(testing);
    }

    @Override
    public void setLogLevel(GdxAppodeal.LogLevel level) {
        Log.LogLevel appodealLevel = Log.LogLevel.valueOf(level.name());
        Appodeal.setLogLevel(appodealLevel);
    }


    @Override
    public void onBannerLoaded(int height, boolean isPrecache) {
        bannerListener.onBannerLoaded();
    }

    @Override
    public void onBannerFailedToLoad() {
        bannerListener.onBannerFailedToLoad();
    }

    @Override
    public void onBannerShown() {
        bannerListener.onBannerShown();
    }

    @Override
    public void onBannerClicked() {
        bannerListener.onBannerClicked();
    }

    @Override
    public void onInterstitialLoaded(boolean b) {
        interstitialListener.onInterstitialLoaded(b);
    }

    @Override
    public void onInterstitialFailedToLoad() {
        interstitialListener.onInterstitialFailedToLoad();
    }

    @Override
    public void onInterstitialShown() {
        interstitialListener.onInterstitialShown();
    }

    @Override
    public void onInterstitialClicked() {
        interstitialListener.onInterstitialClicked();
    }

    @Override
    public void onInterstitialClosed() {
        interstitialListener.onInterstitialClosed();
    }

    @Override
    public void onNonSkippableVideoLoaded(boolean loaded) { nonSkippableVideoListener.onNonSkippableVideoLoaded(); }

    @Override
    public void onNonSkippableVideoFailedToLoad() { nonSkippableVideoListener.onNonSkippableVideoFailedToLoad(); }

    @Override
    public void onNonSkippableVideoShown() {
        nonSkippableVideoListener.onNonSkippableVideoShown();
    }

    @Override
    public void onNonSkippableVideoFinished() { nonSkippableVideoListener.onNonSkippableVideoFinished(); }

    @Override
    public void onNonSkippableVideoClosed(boolean isFinished) { nonSkippableVideoListener.onNonSkippableVideoClosed(); }

    @Override
    public void onRewardedVideoLoaded(boolean loaded) {
        rewardedVideoListener.onRewardedVideoLoaded();
    }

    @Override
    public void onRewardedVideoFailedToLoad() { rewardedVideoListener.onRewardedVideoFailedToLoad(); }

    @Override
    public void onRewardedVideoShown() {
        rewardedVideoListener.onRewardedVideoShown();
    }

    @Override
    public void onRewardedVideoFinished(int i, String s) {
        rewardedVideoListener.onRewardedVideoFinished(i, s);
        Gdx.app.debug("GDXAppodeal", "Reward from appodeal:" + i+" "+s);
    }

    @Override
    public void onRewardedVideoClosed(boolean isFinished) {
        rewardedVideoListener.onRewardedVideoClosed();
    }

    public UserSettings getUserSettings(){
        if(userSettings == null)
            userSettings = new AndroidUserSettings(activity);
        return userSettings;
    }

    @Override
    public void disableWriteExternalStoragePermissionCheck() {
        Appodeal.disableWriteExternalStoragePermissionCheck();
    }

    @Override
    public void requestAndroidMPermissions(PermissionCallback permissionCallback) {
        permissionListener = permissionCallback;
        Appodeal.requestAndroidMPermissions(activity, this);
    }

    @Override
    public void writeExternalStorageResponse(int i) {
        if(permissionListener != null)
            permissionListener.writeExternalStorageResponse(i);
    }

    @Override
    public void accessCoarseLocationResponse(int i) {
        if(permissionListener != null)
            permissionListener.accessCoarseLocationResponse(i);
    }

    @Override
    public void set728x90Banners(boolean b) {
        Appodeal.set728x90Banners(b);
    }

    @Override
    public void setBannerAnimation(boolean b) {
        Appodeal.setBannerAnimation(b);
    }

    @Override
    public void setCustomRule(String s, boolean b) {
        Appodeal.setCustomRule(s, b);
    }

    @Override
    public void setCustomRule(String s, int i) {
        Appodeal.setCustomRule(s, i);
    }

    @Override
    public void setCustomRule(String s, double v) {
        Appodeal.setCustomRule(s, v);
    }

    @Override
    public void setCustomRule(String s1, String s2) {
        Appodeal.setCustomRule(s1, s2);
    }

    @Override
    public void setSmartBanners(boolean b) {
        Appodeal.setSmartBanners(b);
    }

    @Override
    public void trackInAppPurchase(double v, String s) {
        Appodeal.trackInAppPurchase(activity, v, s);
    }
}