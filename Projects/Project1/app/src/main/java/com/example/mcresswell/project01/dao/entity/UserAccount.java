package com.example.mcresswell.project01.dao.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * UserAccount entity representing the UserAccount database table that will
 * exclusively store user account login data.
 */
@Entity(foreignKeys = @ForeignKey(entity = FitnessProfile.class,
                        parentColumns = "id",
                        childColumns = "profile_id"))
public class UserAccount {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "email")
    private String firstName;

    @ColumnInfo(name = "password")
    private String lastName;

    @ColumnInfo(name = "profile_id")
    private int userProfileId; //FK for record in UserProfile database

    @ColumnInfo(name = "join_date")
    private Date joinDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(int userProfileId) {
        this.userProfileId = userProfileId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
