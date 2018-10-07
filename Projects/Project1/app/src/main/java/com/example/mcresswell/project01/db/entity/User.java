package com.example.mcresswell.project01.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.SET_NULL;

/**
 * User @Entity representing the User table that will
 * store user data that is infrequently modified after the account has been
 * created, as well as less frequently accessed
 * in comparison to all of the fitness data for the user.
 */
@Entity(foreignKeys = @ForeignKey(entity = FitnessProfile.class,
                                  parentColumns = "m_userID",
                                  childColumns = "profile_id",
                                  onDelete = SET_NULL),
        indices = {@Index(value = {"email"}, unique = true)})
public class User {

    @PrimaryKey 
    private int id;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "pass")
    private String password;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

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

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getFitnessProfileId() {
        return fitnessProfileId;
    }

    public void setFitnessProfileId(int fitnessProfileId) { this.fitnessProfileId = fitnessProfileId; }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
