package com.ormisiclapps.flappydunkermadness.game.screens;

/**
 * Created by OrMisicL on 5/29/2016.
 */
public interface Screen {

    void initialize();
    void activate();
    void deactivate();
    void update();
    void render();
    void postFadeRender();
    void dispose();
}
