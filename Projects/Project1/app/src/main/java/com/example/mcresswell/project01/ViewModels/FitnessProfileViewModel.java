package com.example.mcresswell.project01.ViewModels;

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

import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.db.repo.FitnessProfileRepository;

import static android.support.constraint.Constraints.TAG;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.printUserProfileData;

public class FitnessProfileViewModel extends AndroidViewModel {

    private static final String LOG = FitnessProfileViewModel.class.getSimpleName();

    private MutableLiveData<FitnessProfile> m_fitnessProfile;
    private FitnessProfileRepository m_fitnessProfileRepository;

    public FitnessProfileViewModel(@NonNull Application application) {
        super(application);
        m_fitnessProfileRepository = FitnessProfileRepository.getInsance(application.getApplicationContext());
        m_fitnessProfile = m_fitnessProfileRepository.getFitnessProfileData();
    }

    public MutableLiveData<FitnessProfile> getFitnessProfile() {
        return m_fitnessProfile;
    }

    public void updateFitnessProfile(){
        m_fitnessProfileRepository.updateFitnessProfile();
    }

    public void addNewFitnessProfile() {m_fitnessProfileRepository.addNewFitnessProfile();}
}
