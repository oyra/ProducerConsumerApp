package com.oyra.pw.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Random;


public class Unit implements Serializable, Comparable<Unit> {
    private int mWeight;
    private String mName;
    private static final int NAME_LENGTH = 4;
    private static final int MAX_WEIGHT = 10;
    private static Random mRandom = new Random();
    static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public Unit() {
        mName = generateName();
        mWeight = generateWeight();
    }

    private static String generateName() {
//        byte[] array = new byte[NAME_LENGTH];
//        mRandom.nextBytes(array);
//        return new String(array);//, Charset.forName("UTF-8"));
        StringBuilder sb = new StringBuilder(NAME_LENGTH);
        for (int i = 0; i < NAME_LENGTH; i++)
            sb.append(CHARS.charAt(mRandom.nextInt(CHARS.length())));
        return sb.toString();
    }

    private static int generateWeight() {
        return mRandom.nextInt(MAX_WEIGHT) + 1;
    }

    public String getName() {
        return (mName == null) ? "" : mName;
    }


    public int getWeight() {
        return mWeight;
    }


    @Override
    public int compareTo(@NonNull Unit another) {
        return (mWeight > another.mWeight ? -1 : 1);
    }
}
