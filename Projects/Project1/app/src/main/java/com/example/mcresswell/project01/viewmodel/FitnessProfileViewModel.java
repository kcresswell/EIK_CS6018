package com.example.mcresswell.project01.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.db.repo.FitnessProfileRepository;

public class FitnessProfileViewModel extends AndroidViewModel {

    private static final String LOG = FitnessProfileViewModel.class.getSimpleName();

    private LiveData<FitnessProfile> m_fitnessProfile;
    private FitnessProfileRepository m_fitnessProfileRepository;

    public FitnessProfileViewModel(@NonNull Application application) {
        super(application);
        m_fitnessProfileRepository = FitnessProfileRepository.getInsance(application.getApplicationContext());
    }

    public LiveData<FitnessProfile> getFitnessProfile(int userID) {
        m_fitnessProfile = m_fitnessProfileRepository.getFitnessProfileData(userID);
        return m_fitnessProfile;
    }

    public void updateFitnessProfile(FitnessProfile fitnessProfile){
        m_fitnessProfileRepository.updateFitnessProfile(fitnessProfile);
    }

    public void insertNewFitnessProfile(FitnessProfile fitnessProfile) {
        m_fitnessProfileRepository.insertNewFitnessProfile(fitnessProfile);
    }
}
