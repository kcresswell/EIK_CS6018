package com.example.mcresswell.project01.db.repo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.dao.UserDao;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.util.UserGenerator;

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

    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final UserDao mUserDao;

    private InStyleDatabase db;
    private static UserRepository userRepository;

    private MediatorLiveData<List<User>> mObservableUsers;

    private MediatorLiveData<User> mObservableUser;

    private static final int NUM_USERS = 20;
    private static final int MAX_USERS = 500;


    private UserRepository(final InStyleDatabase database) {
        db = database;
        mUserDao = db.userDao();

        //Clear out  User database table if number of records exceeds allowable max
        asyncReset();

        //Populate with test data
        List<User> testUsers = UserGenerator.generateUserData(NUM_USERS);

        asyncPopulateUsers(testUsers);

        Log.d(LOG, "Users generated and added to database.");

        addLiveDataListenerSources();

    }

    private void addLiveDataListenerSources() {
        mObservableUsers = new MediatorLiveData<>();
        mObservableUsers.setValue(null);

        //Add listener for livedata source for List<User>
        mObservableUsers.addSource(db.userDao().loadAllUsers(),
                users -> {
                    Log.d(LOG, "LiveData<List<<User>> loadAllUsers onChanged");
                    if (db.isDatabaseCreated().getValue() != null) {
                        mObservableUsers.setValue(users);
                    }
                });

        mObservableUser = new MediatorLiveData<>();
        mObservableUser.setValue(null);

        //Add listener for livedata source for User
        mObservableUser.addSource(mUserDao.findFirstUser(), user -> { //FIXME:
//            Log.d(LOG, "LiveData<User> onChanged");
            if (db.isDatabaseCreated().getValue() != null) {
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

    public LiveData<User> getUser() {
//        return userMutableLiveData;
        return mObservableUser;
    }

    public void deleteAll() {
        asyncDeleteAllUsers();

    }

    public LiveData<List<User>> getUsers() {
        return mObservableUsers;
    }

//    public LiveData<List<User>> getAllUsers() {
//        return mAllUsers;
//    }

    public void update(User user) {
//        mUser = user;
        asyncUpdateUser(user);

    }

    public LiveData<User> find(String userEmail) {
        mObservableUser.addSource(mUserDao.findByEmail(userEmail), user -> {
            Log.d(LOG, "FindByEmail LiveData<User> onChanged");
            if (db.isDatabaseCreated().getValue() != null) {
                Log.d(LOG, "Broadcasting findByEmail() result to observers... ");

                mObservableUser.setValue(user);

            }
        });

        asyncLoadUser(userEmail);

        return mObservableUser;

    }

    public void insert(User user) {
//        asyncInsertUser(mUserDao);
        mUserDao.insertUser(user);
    }

    public void delete(User user) {
        mUserDao.deleteUser(user);
//        asyncDeleteUser(mUserDao);
    }

    public boolean authenticateUser(User user) {
        if (user == null) {
            return false;
        }
        LiveData<User> result = find(user.getEmail());

        return result.getValue() != null &&
                result.getValue().getPassword().equals(user.getPassword());
    }

    //////////// ASYNC TASKS FOR LIST<USER> //////////

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    public void asyncReset() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                LiveData<Integer> numRecords = mUserDao.getUserCount();
                if (numRecords.getValue() != null && numRecords.getValue() >= MAX_USERS) {
                    Log.d(LOG, "Number of records in User table exceeds max. Resetting database contents");
                    mUserDao.deleteAllUsers();
                }
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
                mUserDao.insertAllUsers(params[0]);
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
                mObservableUsers.setValue(userList);
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
                mObservableUsers.setValue(Collections.emptyList());
            }
        }.execute();
    }


    //////////// ASYNC TASKS FOR SINGLE USER /////////




    @SuppressLint("StaticFieldLeak")
    private void asyncLoadUser(String email) {
        new AsyncTask<String, Void, User>() {
            @Override
            protected User doInBackground(String... params) {
                String userToLoad = params[0];
                Log.d(LOG, String.format("Retrieving user record with email %s", userToLoad));

                LiveData<User> user = mUserDao.findByEmail(userToLoad);
                if (user.getValue() != null) {
                    Log.d(LOG, String.format("FindByEmail for %s record lookup successful. ", userToLoad));
                }
                return user.getValue();
            }

            @Override
            protected void onPostExecute(User user) {
                Log.d(LOG, "onPostExecute");
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
                if (userToInsert != null) {
                    //Insert new user record into database
                    mUserDao.insertUser(userToInsert);


                    Log.d(LOG, "Inserting User data . . .");
                }
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
                if (userToUpdate != null) {
                    //Insert new user record into database
                    mUserDao.updateUser(userToUpdate);

                    Log.d(LOG, "Updating User data . . .");
                }
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
                if (userToDelete != null) {
                    //Insert new user record into database
                    mUserDao.deleteUser(userToDelete);
                    Log.d(LOG, "Deleting User data . . .");
                }
                return null;
            }
        }.execute(user);
    }
}
