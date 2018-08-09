package com.appodeal.gdx.data;

public abstract class UserSettings {
    public enum Gender{FEMALE, MALE, OTHER};
    public enum Occupation{OTHER, SCHOOL, UNIVERSITY, WORK};
    public enum Relation{SINGLE, SEARCHING, DATING, MARRIED, ENGAGED, OTHER};
    public enum Alcohol{POSITIVE, NEGATIVE, NEUTRAL};
    public enum Smoking{POSITIVE, NEGATIVE, NEUTRAL};
    abstract public void setAge(int age);
    abstract public void setBirthday(String bday);
    abstract public void setEmail(String email);
    abstract public void setGender(Gender gender);
    abstract public void setInterests(String interests);
    abstract public void setOccupation(Occupation occupation);
    abstract public void setRelation(Relation relation);
    abstract public void setAlcohol(Alcohol alcohol);
    abstract public void setSmoking(Smoking smoking);
}
