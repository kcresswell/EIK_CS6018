package com.example.mcresswell.project01.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * User @Entity representing the User table that will
 * exclusively store user account login data.
 */
@Entity(foreignKeys = @ForeignKey(entity = UserProfile.class,
                        parentColumns = "m_userID",
                        childColumns = "profile_id"))
public class User {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "pass")
    private String password;

    @ColumnInfo(name = "profile_id")
    private int userProfileId; //FK to id of user in UserProfile table

    @ColumnInfo(name = "join_date")
    private Date joinDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(int fitnessProfileId) {
        this.userProfileId = fitnessProfileId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
