package com.example.mcresswell.project01.userProfile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mcresswell.project01.database.AppRepository;

public class UserProfileViewModel extends AndroidViewModel {

    private static final String LOG = UserProfileViewModel.class.getSimpleName();

    private MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();

    private UserProfile m_userProfile;

    private AppRepository m_appRepository;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);

        m_appRepository = AppRepository.getInstance();
        m_userProfile = m_appRepository.getUserProfile();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }
}
