package com.example.mcresswell.project01.userProfile;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.repo.UserRepository;

import java.util.List;

import javax.inject.Inject;

public class UserViewModel extends AndroidViewModel {

    private LiveData<List<User>> mUserList = new MutableLiveData<>();

    @Inject
    UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<User>> getmUserList() {
        return mUserList;
    }

}
