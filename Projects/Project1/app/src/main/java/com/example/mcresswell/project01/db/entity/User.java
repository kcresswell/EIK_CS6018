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
@Entity(foreignKeys = @ForeignKey(entity = FitnessProfile.class,
                        parentColumns = "id",
                        childColumns = "profile_id"))
public class User {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "pass")
    private String password;

    @ColumnInfo(name = "profile_id")
    private int fitnessProfileId; //FK to id of user in FitnessProfile table

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

    public int getFitnessProfileId() {
        return fitnessProfileId;
    }

    public void setFitnessProfileId(int fitnessProfileId) {
        this.fitnessProfileId = fitnessProfileId;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
