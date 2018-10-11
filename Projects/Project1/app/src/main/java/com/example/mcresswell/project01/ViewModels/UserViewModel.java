package com.example.mcresswell.project01.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.repo.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<User> m_user = new MutableLiveData<>();
    private LiveData<List<User>> m_userList;

    UserRepository m_userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
//        if (m_userRepository == null) {
            m_userRepository = new UserRepository(application);
            m_userList = m_userRepository.getAllUsers();
            m_user = m_userRepository.getUser();
//        }
    }

    public void init(User user) {
        if (m_user == null) {
            User u = m_userRepository.find(user);
            m_user.setValue(u);
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

    public LiveData<List<User>> retrieveAllUsers() {
        m_userList = m_userRepository.findAll();
        return m_userList;
    }

    public LiveData<User> getUser() {
        return m_user;
    }

    public LiveData<List<User>> getUserList() {
        return m_userList;
    }
}
