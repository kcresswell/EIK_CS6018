package com.example.mcresswell.project01;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mcresswell.project01.userProfile.UserProfile;

import java.util.ArrayList;

public class UserProfileListParcelable implements Parcelable {

    private ArrayList<UserProfile> m_userProfiles;

    public UserProfileListParcelable(ArrayList<UserProfile> m_userProfiles) {
        this.m_userProfiles = m_userProfiles;
    }

    public ArrayList<UserProfile> getm_fitnessScores() {
        return m_userProfiles;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(m_userProfiles);
    }

    protected UserProfileListParcelable(Parcel in) {
        in.readList(getm_fitnessScores(), UserProfile.class.getClassLoader());
    }

    public static final Creator<UserProfileListParcelable> CREATOR = new Creator<UserProfileListParcelable>() {
        @Override
        public UserProfileListParcelable createFromParcel(Parcel in) {
            return new UserProfileListParcelable(in);
        }

        @Override
        public UserProfileListParcelable[] newArray(int size) {
            return new UserProfileListParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
