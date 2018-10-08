package com.example.mcresswell.project01.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.repo.UserRepository;

import java.util.List;
import java.util.function.Function;

import javax.inject.Inject;

public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<User> m_user = new MutableLiveData<>();
//    private LiveData<List<User>> m_userList;

    UserRepository m_userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        if (m_userRepository == null) {
            m_userRepository = new UserRepository(application);
//            m_userList = m_userRepository.getAllUsers();
            m_user = m_userRepository.getUser();
        }
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

    public User retrieveUser(User user) {
        return m_userRepository.find(user);
    }

    public void deleteUser(User user) {
        m_userRepository.delete(user);
    }

    public LiveData<User> getUser() {
        return m_user;
    }
}
