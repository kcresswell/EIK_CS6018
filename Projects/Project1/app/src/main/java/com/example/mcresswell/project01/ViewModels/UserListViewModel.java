package com.example.mcresswell.project01.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.mcresswell.project01.InStyleApp;
import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.repo.UserRepository;

import java.util.List;

public class UserListViewModel extends AndroidViewModel {

    private LiveData<List<User>> m_userList;

    private final MediatorLiveData<List<User>> mObservableUsers;

    private final UserRepository m_userRepository;

    public UserListViewModel(@NonNull Application application) {
        super(application);
        InStyleDatabase database = InStyleDatabase.getDatabaseInstance(application);
        m_userRepository = UserRepository.getInstance(database);

        mObservableUsers = new MediatorLiveData<>();
        mObservableUsers.setValue(null);


        LiveData<List<User>> users = m_userRepository.getUsers();

        mObservableUsers.addSource(users, mObservableUsers::setValue);

    }

    public void deleteAllUsers() {
        m_userRepository.deleteAll();
    }

    public LiveData<List<User>> getUserList() {
        return mObservableUsers;
//        return m_userList;
    }
}
