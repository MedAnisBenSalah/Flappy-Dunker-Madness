package com.appodeal.gdx;

import com.appodeal.gdx.callbacks.BannerCallback;
import com.appodeal.gdx.callbacks.InterstitialCallback;
import com.appodeal.gdx.callbacks.NonSkippableVideoCallback;
import com.appodeal.gdx.callbacks.PermissionCallback;
import com.appodeal.gdx.callbacks.RewardedVideoCallback;
import com.appodeal.gdx.callbacks.SkippableVideoCallback;
import com.appodeal.gdx.data.UserSettings;

public class GdxAppodeal {

    public static final String TAG = "GdxAppodeal";


    public enum LogLevel{none, debug, verbose};
    public static final int NONE = 0;
    public static final int INTERSTITIAL = 1;
    public static final int SKIPPABLE_VIDEO = 2;
    public static final int REWARDED_VIDEO = 128;
    public static final int NON_SKIPPABLE_VIDEO = 128;
    public static final int BANNER = 4;
    public static final int BANNER_BOTTOM = 8;
    public static final int BANNER_TOP = 16;
    private static AppodealInterface instance;

    private static AppodealInterface getInstance() {
        if (instance == null) instance = GdxAppodealLoader.buildAPI();
        return instance;
    }

    public static void initialize(String appKey, int adType) {
        getInstance().initialize(appKey, adType);
    }

    public static void setInterstitialCallbacks(InterstitialCallback listener) {
        getInstance().setInterstitialCallbacks(listener);
    }

    public static void setBannerCallbacks(BannerCallback listener) {
        getInstance().setBannerCallbacks(listener);
    }

    public static void setSkippableVideoCallbacks(SkippableVideoCallback listener) {
        getInstance().setSkippableVideoCallbacks(listener);
    }

    public static void setNonSkippableVideoCallbacks(NonSkippableVideoCallback listener) {
        getInstance().setNonSkippableVideoCallbacks(listener);
    }

    public static void setRewardedVideoCallbacks(RewardedVideoCallback listener) {
        getInstance().setRewardedVideoCallbacks(listener);
    }

    public static boolean show(int type) {
        return getInstance().show(type);
    }

    public static boolean show(int type, String placement) {
        return getInstance().show(type, placement);
    }

    public static void setAutoCache(int type, boolean autoCache) {
        getInstance().setAutoCache(type, autoCache);
    }

    public static void setOnLoadedTriggerBoth(int type, boolean onLoadedTriggerBoth) {
        getInstance().setOnLoadedTriggerBoth(type, onLoadedTriggerBoth);
    }

    public static boolean isLoaded(int type) {
        return getInstance().isLoaded(type);
    }

    public static boolean isPreCache(int type) {
        return getInstance().isPreCache(type);
    }

    public static void cache(int type) {
        getInstance().cache(type);
    }

    public static  void hide(int type) {
        getInstance().hide(type);
    }
    public static void confirm(int type) {
        getInstance().confirm(type);
    }

    public static void disableLocationPermissionCheck() {
        getInstance().disableLocationPermissionCheck();
    }

    public static void disableNetwork(String name) {
        getInstance().disableNetwork(name);
    }

    public static void disableNetwork(String name, int type) {
        getInstance().disableNetwork(name, type);
    }

    public static String getVersion() {
        return getInstance().getVersion();
    }

    public static void setTesting(boolean testing) {
        getInstance().setTesting(testing);
    }

    public static void setLogLevel(LogLevel level) {
        getInstance().setLogLevel(level);
    }

    public static UserSettings getUserSettings(){
        return getInstance().getUserSettings();
    }

    public static void disableWriteExternalStoragePermissionCheck(){
        getInstance().disableWriteExternalStoragePermissionCheck();
    }

    public static void requestAndroidMPermissions(PermissionCallback permissionCallback) {
        getInstance().requestAndroidMPermissions(permissionCallback);
    }

    public static void set728x90Banners(boolean b){
        getInstance().set728x90Banners(b);
    }

    public static void setBannerAnimation(boolean b){
        getInstance().setBannerAnimation(b);
    }

    public static void setCustomRule(String s, boolean b){
        getInstance().setCustomRule(s, b);
    }

    public static void setCustomRule(String s, double v){
        getInstance().setCustomRule(s, v);
    }

    public static void setCustomRule(String s, int i){
        getInstance().setCustomRule(s, i);
    }

    public static void setCustomRule(String s1, String s2){
        getInstance().setCustomRule(s1, s2);
    }

    public static void setSmartBanners(boolean b){
        getInstance().setSmartBanners(b);
    }

    public static void trackInAppPurchase(double v, String s){
        getInstance().trackInAppPurchase(v, s);
    }
}