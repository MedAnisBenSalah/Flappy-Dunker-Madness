package com.appodeal.gdx.android;

import android.content.Context;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.UserSettings;


public class AndroidUserSettings extends com.appodeal.gdx.data.UserSettings{
    private UserSettings settings;
    public AndroidUserSettings(Context ctx){
        settings = Appodeal.getUserSettings(ctx);
    }
    @Override
    public void setAge(int age) {
        settings.setAge(age);
    }

    @Override
    public void setBirthday(String bday) {
        //settings.setBirthday(bday);
    }

    @Override
    public void setEmail(String email) {
        //settings.setEmail(email);
    }

    @Override
    public void setGender(com.appodeal.gdx.data.UserSettings.Gender gender) {
        /*UserSettings.Gender genderAndroid = UserSettings.Gender.valueOf(gender.name());
        settings.setGender(genderAndroid);*/
    }

    @Override
    public void setInterests(String interests) {
        //settings.setInterests(interests);
    }

    @Override
    public void setOccupation(com.appodeal.gdx.data.UserSettings.Occupation occupation) {
        /*UserSettings.Occupation occupationAndroid = UserSettings.Occupation.valueOf(occupation.name());
        settings.setOccupation(occupationAndroid);*/
    }

    @Override
    public void setRelation(com.appodeal.gdx.data.UserSettings.Relation relation) {
        /*UserSettings.Relation relationAndroid = UserSettings.Relation.valueOf(relation.name());
        settings.setRelation(relationAndroid);*/

    }

    @Override
    public void setAlcohol(Alcohol alcohol) {
        /*UserSettings.Alcohol alcoholAndroid = UserSettings.Alcohol.valueOf(alcohol.name());
        settings.setAlcohol(alcoholAndroid);*/
    }

    @Override
    public void setSmoking(Smoking smoking) {
        /*UserSettings.Smoking smokingAndroid = UserSettings.Smoking.valueOf(smoking.name());
        settings.setSmoking(smokingAndroid);*/
    }
}
