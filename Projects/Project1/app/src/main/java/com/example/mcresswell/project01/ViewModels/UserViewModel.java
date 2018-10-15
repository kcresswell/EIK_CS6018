package com.example.mcresswell.project01.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.repo.UserRepository;


public class UserViewModel extends AndroidViewModel {

    private static final String LOG = UserViewModel.class.getSimpleName();

    private final MediatorLiveData<User> m_observableUser;

    private final UserRepository m_userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        InStyleDatabase database = InStyleDatabase.getDatabaseInstance(application);
        m_userRepository = UserRepository.getInstance(database);

        m_observableUser = new MediatorLiveData<>();

        configureMediatorLiveData();
    }

    private void configureMediatorLiveData() {
        m_observableUser.setValue(null);

        LiveData<User> userLiveData = m_userRepository.getUser();
        m_observableUser.addSource(userLiveData, user -> {
            Log.d(LOG, "m_userRepository.getUser() listener onChanged");
            if (user == null) {
                Log.d(LOG, "BUT ALAS IT IS NULL :(");
                return;
            }
            m_observableUser.setValue(user);

        });
    }

    public boolean authenticateUser(User user) {
        if (user == null) {
            return false;
        }
        LiveData<User> result = findUser(user.getEmail());
        return result.getValue().getEmail().equals(user.getEmail()) &&
                result.getValue().getPassword().equals(user.getPassword());
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
//        if (result.getValue() != null) {
//            Log.d(LOG, "User found.");
//        }
        return result;
    }

    public LiveData<User> getUser() {
        return m_observableUser;
    }

}
