package com.application.nick.crappybird.entity;

/**
 * Created by Nick on 6/10/2015.
 */
public class MarketBird {
    private int birdNum;
    private String birdName, achievementSKU;
    private boolean unlocked;

    public MarketBird(String name, String achievementSKU, int num, boolean unlocked) {
        this.birdName = name;
        this.achievementSKU = achievementSKU;
        this.birdNum = num;
        this.unlocked = unlocked;
    }

    public int getBirdNum() {
        return birdNum;
    }

    public String getAchievementID() {
        return achievementSKU;
    }

    public String getBirdName() {
        return birdName;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }


    public boolean equals(MarketBird bird) {
        return this.getAchievementID().equals(bird.getAchievementID());

    }
}
