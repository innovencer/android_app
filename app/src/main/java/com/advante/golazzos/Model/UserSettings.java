package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 5/26/2016.
 */
public class UserSettings extends Model {
    boolean won_notification;
    boolean lose_notification;
    boolean new_bet_notification;
    boolean friendship_notification;
    boolean closed_match_notification;

    public UserSettings() {
    }

    public UserSettings(boolean won_notification, boolean lose_notification, boolean new_bet_notification, boolean friendship_notification, boolean closed_match_notification) {
        this.won_notification = won_notification;
        this.lose_notification = lose_notification;
        this.new_bet_notification = new_bet_notification;
        this.friendship_notification = friendship_notification;
        this.closed_match_notification = closed_match_notification;
    }

    public boolean isWon_notification() {
        return won_notification;
    }

    public void setWon_notification(boolean won_notification) {
        this.won_notification = won_notification;
    }

    public boolean isLose_notification() {
        return lose_notification;
    }

    public void setLose_notification(boolean lose_notification) {
        this.lose_notification = lose_notification;
    }

    public boolean isNew_bet_notification() {
        return new_bet_notification;
    }

    public void setNew_bet_notification(boolean new_bet_notification) {
        this.new_bet_notification = new_bet_notification;
    }

    public boolean isFriendship_notification() {
        return friendship_notification;
    }

    public void setFriendship_notification(boolean friendship_notification) {
        this.friendship_notification = friendship_notification;
    }

    public boolean isClosed_match_notification() {
        return closed_match_notification;
    }

    public void setClosed_match_notification(boolean closed_match_notification) {
        this.closed_match_notification = closed_match_notification;
    }
}
