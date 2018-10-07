package com.example.mcresswell.project01.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mcresswell.project01.db.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for querying User table in database.
 * Four scenarios exist where this DAO will be used:
 * 1. An existing user attempts to log in.
 * 2. A new user registers a new account.
 * 3. An existing user updates their account credentials (changes login email or password)
 * 4. An existing user elects to delete their account.
 *
 *  * An Optional<User> is returned in the case where no user account exists
 * for the email that was entered upon login. This prevents an exception from being thrown.
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM User WHERE email = :email")
    Optional<User> findByEmail(String email);

    @Insert
    void createUserAccount(User user);

    @Update
    void updateUserAccount(User user);

    @Delete
    void deleteUserAccount(User user);

    @Insert
    void insertAll(List<User> users);

    @Query("SELECT * FROM User ORDER BY email ASC")
    List<User> getAllUserAccountData();

}
