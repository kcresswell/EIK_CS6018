package com.example.mcresswell.project01;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ImageButtonListParcelable implements Parcelable {

    private ArrayList<DashButton> m_buttons;

    public ImageButtonListParcelable(ArrayList<DashButton> m_buttons) {
        this.m_buttons = m_buttons;
    }

    public ArrayList<DashButton> getM_buttons() {
        return m_buttons;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(m_buttons);
    }

    protected ImageButtonListParcelable(Parcel in) {
        in.readList(getM_buttons(), DashButton.class.getClassLoader());
    }

    public static final Creator<ImageButtonListParcelable> CREATOR = new Creator<ImageButtonListParcelable>() {
        @Override
        public ImageButtonListParcelable createFromParcel(Parcel in) {
            return new ImageButtonListParcelable(in);
        }

        @Override
        public ImageButtonListParcelable[] newArray(int size) {
            return new ImageButtonListParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
