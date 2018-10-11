package com.example.mcresswell.project01.db.repo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.dao.UserDao;
import com.example.mcresswell.project01.db.entity.User;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository/model class for the User entity that handles all business logic associated
 * handling user account data. This class interfaces with the User entity in the in-memory database.
 * User account data retrieved from the database is passed to the UserViewModel class.
 */
@Singleton
public class UserRepository {
    private static final String LOG = UserRepository.class.getSimpleName();
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private LiveData<List<User>> mAllUsers; //All user account records in User table
    private final UserDao mUserDao;
    private User mUser;


    public UserRepository(Application application) {
//        loadAllUsers();
        InStyleDatabase db = InStyleDatabase.getDatabaseInstance(application);
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAllUserAccountData();
    }

    public MutableLiveData<User> getUser() {
        return userMutableLiveData;
    }

    public LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public void update(User user) {
        mUser = user;
        updateUserData(mUserDao);

    }

    public LiveData<List<User>> findAll() {
        return mUserDao.getAllUserAccountData();
    }

    public User find(User user) {
        mUser = user;
        lookupUserData(mUserDao);
        return userMutableLiveData.getValue();

    }

    public void insert(User user) {
        mUser = user;
        insertUserData(mUserDao);
    }

    public void delete(User user) {
        mUser = user;
        deleteUserData(mUserDao);
    }

    public boolean authenticateUser(User user) {
        if (user == null) {
            return false;
        }
        User userRecordFromDatabase = find(user);
        return user.getPassword().equals(userRecordFromDatabase.getPassword());
    }

    @SuppressLint("StaticFieldLeak")
    private void lookupUserData(UserDao userDao) {
        new AsyncTask<User, Void, User>() {
            @Override
            protected User doInBackground(User... params) {
                Log.d(LOG, "do in background");

                User userToLoad = params[0];
                if (userToLoad != null) {
                    //Load user from database
                    Optional<User> result = userDao.findByEmail(userToLoad.getEmail());
                    if (result.isPresent()) {
                        Log.d(LOG, "User found in database");
                        return result.get();
                    }
                    Log.d(LOG, "USER NOT FOUND");
                }
                return null;
            }

            @Override
            protected void onPostExecute(User user) {
                if (user != null) {
                    Log.d(LOG, "onPostExecute: " + user.getFirstName() + "\t" + user.getLastName() + "\t"
                            + user.getEmail() + "\t" + user.getPassword() + "\t");
                    userMutableLiveData.setValue(user);
                }

            }
        }.execute(mUser);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertUserData(UserDao userDao) {
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... params) {
                User userToInsert = params[0];
                if (userToInsert != null) {
                    //Insert new user record into database
                    userDao.createUserAccount(userToInsert);

                    Log.d(LOG, "NEW USER ACCOUNT CREATED");
                }
                return null;
            }
        }.execute(mUser);
    }

    @SuppressLint("StaticFieldLeak")
    private void updateUserData(UserDao userDao) {
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... params) {
                User userToUpdate = params[0];
                if (userToUpdate != null) {
                    //Insert new user record into database
                    userDao.updateUserAccount(userToUpdate);

                    Log.d(LOG, "USER ACCOUNT UPDATED");
                }
                return null;
            }
        }.execute(mUser);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteUserData(UserDao userDao) {
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... params) {
                User userToDelete = params[0];
                if (userToDelete != null) {
                    //Insert new user record into database
                    userDao.deleteUserAccount(userToDelete);
                    Log.d(LOG, "USER ACCOUNT DELETED");
                }
                return null;
            }
        }.execute(mUser);
    }


}
