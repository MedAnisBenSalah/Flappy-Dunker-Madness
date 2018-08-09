package com.appodeal.gdx;

import com.appodeal.gdx.callbacks.BannerCallback;
import com.appodeal.gdx.callbacks.InterstitialCallback;
import com.appodeal.gdx.callbacks.NonSkippableVideoCallback;
import com.appodeal.gdx.callbacks.PermissionCallback;
import com.appodeal.gdx.callbacks.RewardedVideoCallback;
import com.appodeal.gdx.callbacks.SkippableVideoCallback;
import com.appodeal.gdx.data.UserSettings;

public interface AppodealInterface {

    void initialize(String appId, int type);
    void setInterstitialCallbacks(InterstitialCallback listener);
    void setBannerCallbacks(BannerCallback listener);
    void setSkippableVideoCallbacks(SkippableVideoCallback listener);
    void setNonSkippableVideoCallbacks(NonSkippableVideoCallback listener);
    void setRewardedVideoCallbacks(RewardedVideoCallback listener);
    boolean show(int type);
    boolean show(int type, String placement);
    void setAutoCache(int type, boolean autoCache);
    void setOnLoadedTriggerBoth(int type, boolean onLoadedTriggerBoth);
    boolean isLoaded(int type);
    boolean isPreCache(int type);
    void cache(int type);
    void hide(int type);
    void confirm(int type);
    void disableLocationPermissionCheck();
    void disableNetwork(String name);
    void disableNetwork(String name, int type);
    String getVersion();
    void setTesting(boolean testing);
    void setLogLevel(GdxAppodeal.LogLevel level);
    UserSettings getUserSettings();
    void disableWriteExternalStoragePermissionCheck();
    void requestAndroidMPermissions(PermissionCallback permissionCallback);
    void set728x90Banners(boolean b);
    void setBannerAnimation(boolean b);
    void setCustomRule(String s, boolean b);
    void setCustomRule(String s, double v);
    void setCustomRule(String s, int i);
    void setCustomRule(String s1, String s2);
    void setSmartBanners(boolean b);
    void trackInAppPurchase(double v, String s);
}
