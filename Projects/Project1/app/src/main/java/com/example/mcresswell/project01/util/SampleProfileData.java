package com.example.mcresswell.project01.util;

import com.example.mcresswell.project01.db.entity.FitnessProfile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleProfileData {

//    private static final FitnessProfile();

    private static Date getDate(int diff){
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, diff);

        return cal.getTime();
    }

    public static List<FitnessProfile> getUserProfiles(){
        List<FitnessProfile> fitnessProfiles = new ArrayList<>();

        fitnessProfiles.add(new FitnessProfile(
                1,
                getDate(0),
                "Flynn",
                "White",
                "07/07/1970",
                "M",
                "Albuquerque",
                "US",
                "Active",
                "Maintain",
                0,
                185,
                6,
                0));

        fitnessProfiles.add(new FitnessProfile(
                2,
                getDate(-1),
                "Norma",
                "Jones",
                "01/01/1980",
                "F",
                "Nashville",
                "US",
                "Active",
                "Maintain",
                0,
                128,
                5,
                7));

        fitnessProfiles.add(new FitnessProfile(
                3,
                getDate(-2),
                "Omar",
                "Akmed",
                "03/03/1940",
                "M",
                "Salt Lake City",
                "US",
                "Active",
                "Loose",
                1,
                220,
                5,
                10));

        return fitnessProfiles;
    }
}
