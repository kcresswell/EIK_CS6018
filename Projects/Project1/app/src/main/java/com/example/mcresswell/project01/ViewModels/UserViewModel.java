package com.example.mcresswell.project01.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.repo.UserRepository;

import java.util.List;


public class UserViewModel extends AndroidViewModel {

    private static final String LOG = UserViewModel.class.getSimpleName();

    private final MediatorLiveData<User> mObservableUser;

    private final UserRepository m_userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        InStyleDatabase database = InStyleDatabase.getDatabaseInstance(application);
        m_userRepository = UserRepository.getInstance(database);

        mObservableUser = new MediatorLiveData<>();
        mObservableUser.setValue(null);

        LiveData<User> userLiveData = m_userRepository.getUser();
        mObservableUser.addSource(userLiveData, user -> {
            Log.d(LOG, "m_userRepository.getUser() listener onChanged");
            if (user == null) {
                Log.d(LOG, "BUT ALAS IT IS NULL :(");
                return;
            }
            mObservableUser.setValue(user);

        });


    }

    public boolean authenticateUser(User user) {
        return m_userRepository.authenticateUser(user);
    }

    public void createUser(User user) {
        m_userRepository.insert(user);
    }

    public void updateUser(User user) {
        m_userRepository.update(user);
    }



    public void deleteUser(User user) {
        m_userRepository.delete(user);
    }

    public LiveData<User> findUser(String email) {
        LiveData<User> result = m_userRepository.find(email);
        if (result.getValue() != null) {
            Log.d(LOG, "User found.");
        }
        return result;
    }

    public LiveData<User> getUser() {
        return mObservableUser;
    }

}
