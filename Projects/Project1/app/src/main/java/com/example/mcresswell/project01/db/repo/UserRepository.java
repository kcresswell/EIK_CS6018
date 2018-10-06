package com.example.mcresswell.project01.db.repo;

import android.arch.lifecycle.LiveData;

import com.example.mcresswell.project01.db.dao.UserDao;
import com.example.mcresswell.project01.db.entity.User;

import java.util.List;

public class UserRepository {
    private UserDao mUserDao;
    private LiveData<List<User>> mAllUsers; //All user account records in User table



    LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }
}
