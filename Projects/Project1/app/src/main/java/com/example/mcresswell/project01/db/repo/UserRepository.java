package com.example.mcresswell.project01.db.repo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.dao.UserDao;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.util.UserGenerator;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

/**
 * This class exposes User account data to the UI layer via the UserListViewModel.
 *
 * Repository/model class for the User entity that handles all business logic associated
 * handling user account data. This class interfaces with the User entity in the in-memory database.
 * User account data retrieved from the database is passed to the UserListViewModel class.
 */
@SuppressWarnings("ALL")
@Singleton
public class UserRepository {
    private static final String LOG = UserRepository.class.getSimpleName();

    private final UserDao mUserDao;

    private InStyleDatabase inStyleDatabase;
    private static UserRepository userRepository;

    private MediatorLiveData<List<User>> mObservableUserList;

    private MediatorLiveData<User> mObservableUser;

    private static final int NUM_TEST_USERS = 10; //Number of random test users to generate
    private static final int MAX_USERS = 500; //Maximum allowable of users in table


    private UserRepository(final InStyleDatabase database) {
        inStyleDatabase = database;
        mUserDao = inStyleDatabase.userDao();

        //Clear out User database
        asyncReset();

        List<User> testUsers = UserGenerator.generateUserData(NUM_TEST_USERS);

        //Populate with randomly generated test data
        asyncPopulateUsers(testUsers);

        Log.d(LOG, "Users generated and added to database.");

        addLiveDataListenerSources();

    }

    private void addLiveDataListenerSources() {
        mObservableUserList = new MediatorLiveData<>();
        mObservableUserList.setValue(null);

        //Add listener for livedata source for List<User>
        mObservableUserList.addSource(mUserDao.loadAllUsers(),
                users -> {
                    Log.d(LOG, "LiveData<List<<User>> loadAllUsers onChanged");
                    if (inStyleDatabase.isDatabaseCreated().getValue() != null) {
                        mObservableUserList.setValue(users);
                    }
                });

        mObservableUser = new MediatorLiveData<>();
        mObservableUser.setValue(null);

        //Add listener for livedata source for User
        mObservableUser.addSource(mUserDao.findFirstUser(), user -> { //FIXME: ADDED THIS IN PURELY FOR DEBUGGING, REMOVE THIS LATER
            if (inStyleDatabase.isDatabaseCreated().getValue() != null) {

                Log.d(LOG, "Broadcasting updated value of LiveData<User> to observers");

                mObservableUser.setValue(user);
            }
        });
    }

    /**
     * Static method to ensure only one instance of the UserRepository is instantiated.
     * @param database
     * @return
     */
    public static UserRepository getInstance(final InStyleDatabase database) {
        if (userRepository == null) {
            synchronized (UserRepository.class) {
                if (userRepository == null) {
                    userRepository = new UserRepository(database);
                }
            }
        }
        return userRepository;
    }

    ///////// Getters ///////////

    public LiveData<User> getUser() {
        return mObservableUser;
    }

    public LiveData<List<User>> getUsers() {
        return mObservableUserList;
    }

    ///////// CRUD Operations ///////////

    public void insert(User user) {
        asyncInsertUser(user);
    }

    public LiveData<User> find(String userEmail) {
        mObservableUser.addSource(mUserDao.findByEmail(userEmail), user -> {
            Log.d(LOG, String.format("findByEmail() for email  %s LiveData<User> onChanged", userEmail));
            if (inStyleDatabase.isDatabaseCreated().getValue() != null) {
                Log.d(LOG, "Broadcasting findByEmail() result to observers... ");
                mObservableUser.setValue(user);
            }
        });
        asyncLoadUser(userEmail);

        return mObservableUser;
    }


    public void update(User user) {
        asyncUpdateUser(user);
    }

    public void delete(User user) {
        mUserDao.deleteUser(user);
//        asyncDeleteUser(mUserDao);
    }

    public void deleteAll() {
        asyncDeleteAllUsers();
    }

    public boolean authenticateUser(User user) {
        if (user == null) {
            return false;
        }
        LiveData<User> result = find(user.getEmail());
        return result.getValue().getPassword().equals(user.getPassword());
    }


    //////////// ASYNC TASKS FOR A SINGLE USER /////////////


    @SuppressLint("StaticFieldLeak")
    private void asyncLoadUser(String email) {
        new AsyncTask<String, Void, User>() {
            @Override
            protected User doInBackground(String... params) {
                String userToLoad = params[0];
                Log.d(LOG, String.format("Retrieving user record with email %s", userToLoad));

                LiveData<User> user = mUserDao.findByEmail(userToLoad);

                return user.getValue();
            }

            @Override
            protected void onPostExecute(User user) {
                mObservableUser.setValue(user);
            }
        }.execute(email);
    }

    @SuppressLint("StaticFieldLeak")
    private void asyncInsertUser(User user) {
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... params) {
                User userToInsert = params[0];
                Log.d(LOG, String.format("Inserting new user record with email %s into database", userToInsert.getEmail()));
                //Insert new user record into database
                mUserDao.insertUser(userToInsert);

                Log.d(LOG, "Inserting User data . . .");
                return null;
            }
        }.execute(user);
    }

    @SuppressLint("StaticFieldLeak")
    private void asyncUpdateUser(User user) {
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... params) {
                User userToUpdate = params[0];
                Log.d(LOG, String.format("Updating existing user record with email %s in database", userToUpdate.getEmail()));

                //Updating existing user record in database
                mUserDao.updateUser(userToUpdate);

                Log.d(LOG, "Updating User data . . .");
                return null;
            }
        }.execute(user);
    }

    @SuppressLint("StaticFieldLeak")
    private void asyncDeleteUser(User user) {
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... params) {
                User userToDelete = params[0];
                Log.d(LOG, String.format("Deleting existing user record with email %s from database", userToDelete.getEmail()));

                //Delete user record from database
                mUserDao.deleteUser(userToDelete);
                Log.d(LOG, "Deleting User data . . .");
                return null;
            }
        }.execute(user);
    }


    ////////////////// ASYNC TASKS FOR A LIST OF USERS //////////////////

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    public void asyncReset() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
//                LiveData<Integer> numRecords = mUserDao.getUserCount();
//                if (numRecords.getValue() != null && numRecords.getValue() >= MAX_USERS) {
//                    Log.d(LOG, "Number of records in User table exceeds max. Resetting database contents");
                    mUserDao.deleteAllUsers();
//                }
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    public void asyncPopulateUsers(List<User> users) {
        new AsyncTask<List<User>, Void, Void>() {
            @Override
            protected Void doInBackground(List<User>... params) {

                Log.d(LOG, "Inserting test data into database table to populate.");
                //Insert randomly generated user data
                mUserDao.insertAllUsers(params[0]);

                //Insert specific test user record for facilitating manual testing of app
                insertTestUser();


                return null;
            }
        }.execute(users);
    }

    @SuppressLint("StaticFieldLeak")
    private void asyncLoadUsers() {
        new AsyncTask<Void, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Void... params) {
                Log.d(LOG, "Loading users from database");
                LiveData<List<User>> listLiveData = mUserDao.loadAllUsers();

                if (listLiveData.getValue() != null) {
                    return listLiveData.getValue();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<User> userList) {
                mObservableUserList.setValue(userList);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void asyncDeleteAllUsers() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.d(LOG, "Deleting all users from database");
                mUserDao.deleteAllUsers();
                return null;
            }

            @Override
            protected void onPostExecute(Void voidResult) {
                mObservableUserList.setValue(Collections.emptyList());
            }
        }.execute();
    }

    private void insertTestUser() {
        User testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setPassword("password");
        testUser.setFirstName("Hello");
        testUser.setLastName("Kitty");
        testUser.setJoinDate(Date.valueOf("2018-01-01"));
        mUserDao.insertUser(testUser);
        Log.d(LOG, "GENERIC TEST USER successfully inserted into User database");
    }
}
