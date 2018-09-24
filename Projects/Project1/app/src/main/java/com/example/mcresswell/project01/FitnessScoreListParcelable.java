package com.example.mcresswell.project01;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FitnessScoreListParcelable implements Parcelable {

    private ArrayList<UserProfile> m_userProfiles;

    public FitnessScoreListParcelable(ArrayList<UserProfile> m_userProfiles) {
        this.m_userProfiles = m_userProfiles;
    }

    public ArrayList<UserProfile> getm_fitnessScores() {
        return m_userProfiles;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(m_userProfiles);
    }

    protected FitnessScoreListParcelable(Parcel in) {
        in.readList(getm_fitnessScores(), UserProfile.class.getClassLoader());
    }

    public static final Creator<FitnessScoreListParcelable> CREATOR = new Creator<FitnessScoreListParcelable>() {
        @Override
        public FitnessScoreListParcelable createFromParcel(Parcel in) {
            return new FitnessScoreListParcelable(in);
        }

        @Override
        public FitnessScoreListParcelable[] newArray(int size) {
            return new FitnessScoreListParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
