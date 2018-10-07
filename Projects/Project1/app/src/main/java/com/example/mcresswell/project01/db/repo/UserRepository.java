package com.example.mcresswell.project01.db.repo;

import android.arch.lifecycle.LiveData;

import com.example.mcresswell.project01.db.dao.UserDao;
import com.example.mcresswell.project01.db.entity.User;

import java.util.List;

/**
 * Repository/model class for the User entity that handles all business logic associated
 * handling user account data. This class interfaces with the User entity in the in-memory database.
 * User account data retrieved from the database is passed to the UserViewModel class.
 */

public class UserRepository {
    private UserDao mUserDao;
    private LiveData<List<User>> mAllUsers; //All user account records in User table



    LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }
}
