package com.example.mcresswell.project01.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.repo.FitnessProfileRepository;

public class FitnessProfileViewModel extends AndroidViewModel {

    private static final String LOG = FitnessProfileViewModel.class.getSimpleName();

    private LiveData<FitnessProfile> m_fitnessProfile;
    private FitnessProfileRepository m_fitnessProfileRepository;
    private FitnessProfile fitnessProfile;
    private User m_user;

    public FitnessProfileViewModel(@NonNull Application application) {
        super(application);
        m_fitnessProfileRepository = FitnessProfileRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<FitnessProfile> getLDFitnessProfile(int userID) {
        m_fitnessProfile = m_fitnessProfileRepository.getFitnessProfileData(userID);
        return m_fitnessProfile;
    }

    public LiveData<FitnessProfile> getLDFitnessProfile() {
        if (m_user != null) {
            m_fitnessProfile = m_fitnessProfileRepository.getFitnessProfileData(m_user.getId());
        }
        return m_fitnessProfile;
    }

    public void insertNewFitnessProfile(FitnessProfile fitnessProfile) {
        m_fitnessProfileRepository.insertNewFitnessProfile(fitnessProfile);
    }

    public void setUser(User user){
        m_user = user;
    }

    public User getUser(){
        return m_user;
    }

    public void setFitnessProfile(FitnessProfile fitnessProfile) {
        this.fitnessProfile = fitnessProfile;
    }

    public FitnessProfile getFitnessProfile(){
        return fitnessProfile;
    }
}
