package com.example.mcresswell.project01;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FitnessScoreListParcelable implements Parcelable {

    private ArrayList<FitnessScore> m_fitnessScores;

    public FitnessScoreListParcelable(ArrayList<FitnessScore> m_fitnessScores) {
        this.m_fitnessScores = m_fitnessScores;
    }

    public ArrayList<FitnessScore> getm_fitnessScores() {
        return m_fitnessScores;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(m_fitnessScores);
    }

    protected FitnessScoreListParcelable(Parcel in) {
        in.readList(getm_fitnessScores(), FitnessScore.class.getClassLoader());
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
