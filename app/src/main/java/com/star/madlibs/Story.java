package com.star.madlibs;


import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Story {

    private Context mContext;

    private String mStory;

    private int mPos = 0;

    private Set<String> mDictionary;

    public Story(Context context) {
        this(context, new Random().nextInt(5));
    }

    public Story(Context context, int position) {

        this.mContext = context;

        String[] stories = context.getResources().getStringArray(R.array.stories);
        int id = context.getResources().getIdentifier(
                stories[position], "raw", context.getPackageName());

        try {
            InputStream inputStream = context.getResources().openRawResource(id);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(" ");
            }

            this.mStory = stringBuilder.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadDictionary();
    }

    public String getStory() {
        return mStory;
    }

    public int getCount() {
        int pos = 0;
        int count = 0;

        while ((pos = mStory.indexOf("<", pos) + 1) != 0) {
            count++;
        }

        return count;
    }

    public String findNextWordToReplace() {
        int startPos = mStory.indexOf("<", mPos);
        int endPos = mStory.indexOf(">", mPos);
        if (startPos != -1) {
            return mStory.substring(startPos + 1, endPos);
        } else {
            return "";
        }
    }

    public boolean isInDictionary(String word) {
        if (mDictionary.contains(word)) {
            return true;
        } else {
            return true;
        }
    }

    public void replaceWord(String word) {
        int startPos = mStory.indexOf("<", mPos);
        int endPos = mStory.indexOf(">", mPos);
        if (startPos != -1) {
            mStory = mStory.substring(0, startPos) + "<b>" + word + "</b>" +
                    mStory.substring(endPos + 1);
            mPos = mStory.indexOf("</b>", mPos) + 4;
        }
    }

    private void loadDictionary() {
        mDictionary = new HashSet<>();

        Scanner scanner = new Scanner(mContext.getResources().openRawResource(R.raw.dict));

        while (scanner.hasNext()) {
            mDictionary.add(scanner.nextLine());
        }
    }

}
