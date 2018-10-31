package com.example.mcresswell.project01.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.db.repo.FitnessProfileRepository;
import com.example.mcresswell.project01.fragments.FitnessDetailsFragment;
<<<<<<< HEAD

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
=======
>>>>>>> b0e54e5f654342fc002ac4f68415a85096e80ecc

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class FitnessProfileViewModel extends AndroidViewModel {

    private LiveData<FitnessProfile> m_fitnessProfile;
    private FitnessProfileRepository m_fitnessProfileRepository;

    public FitnessProfileViewModel(@NonNull Application application) {
        super(application);
        m_fitnessProfileRepository = FitnessProfileRepository.getInstance(application.getApplicationContext());

    }

    public LiveData<FitnessProfile> getFitnessProfile(int userID) {
        m_fitnessProfile = m_fitnessProfileRepository.getFitnessProfileData(userID);
        return m_fitnessProfile;
    }

    public void updateFitnessProfile(FitnessProfile fitnessProfile){
        m_fitnessProfileRepository.updateFitnessProfile(fitnessProfile);
    }

    public void setStepCount(Float m_numberOfSteps) {
        m_fitnessProfile.getValue().setM_stepCount(m_numberOfSteps);
        m_fitnessProfile.getValue().setM_dateLastUpdated(new Date(2018, 10, 31));
        insertNewFitnessProfile(m_fitnessProfile.getValue());
    }

    public void insertNewFitnessProfile(FitnessProfile fitnessProfile) {
        m_fitnessProfileRepository.insertNewFitnessProfile(fitnessProfile);
    }

//    public LiveData<FitnessProfile> getFitnessProfile() {
//        return m_fitnessProfile;
//    }

}
