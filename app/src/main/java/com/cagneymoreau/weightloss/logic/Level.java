package com.cagneymoreau.weightloss.logic;

import android.content.Context;
import android.util.Log;

import com.cagneymoreau.weightloss.R;

import java.util.ArrayList;

/**
 * Representation of each level
 * progress and dailystatus must be saved and updated from memory
 * other are hardcoded
 */


public class Level {

    private final static String TAG = "Level";
    private final static int DAYSREQ = 60;

    public int levelNum;
    public int title;
    public int rules;
    public int reward;

    //0 = morn, 1 = noon, 2 = evening & -1 for no alarm
    int alarmTime;

    public int imageResource;

    public int link;
    public String buttonText;


    public void firstLaunch(int p)
    {

        if (p == 0){
            currProgress = Progress.inProgress;
        }   else{
            currProgress = Level.Progress.notYet;
        }
        dailyStatus = new ArrayList<>();

    }

    public String getButtonText()
    {
        if (levelNum == 0){
            return "Start!";
        }
        return "Add Today's Results";
    }

    public String daysToGo()
    {
        if (currProgress.equals(Progress.notYet)) return "";

        if (currProgress.equals(Progress.complete)) return "Complete!";

        if (levelNum == 0) return "Are you ready?";

        return String.valueOf(daysLeft()) + " days to beat level!";

    }


    public void resetLevel(boolean inProg)
    {
        dailyStatus = new ArrayList<>();
        if (inProg){
            currProgress = Progress.inProgress;
        }else{
            currProgress = Progress.notYet;
        }
    }

    public String getReward(Context c)
    {
        return c.getString(reward);
    }

    public String getLink(Context c)
    {
        return c.getString(link);
    }

    //region ---------------------------  progress

    public enum Progress {notYet, inProgress, complete}
    private Progress currProgress;

    public void setCurrProgress(Progress p)
    {
        currProgress = p;
    }

    public Progress getCurrProgress() {
        return currProgress;
    }

    //endregion

    /**
     * Status is saved fifo with recent at index 0
     *
     */

    //region ------------ status

    public enum Status {Yes, No, BackSlide}
    private ArrayList<Status> dailyStatus;

    public void addStatus(Status s)
    {
        while (dailyStatus.size() >= DAYSREQ){
            dailyStatus.remove(0);
        }
        dailyStatus.add(s);

        if (daysLeft() == 0){
            currProgress = Progress.complete;
            dailyStatus.clear(); //no longer needed
        }

    }

    public void setStatusFromMemory(ArrayList<Status> s){
        if (dailyStatus!= null) {
            Log.e(TAG, "setStatusFromMemory: ", null);
            return;
        }

        dailyStatus = s;
    }

    public ArrayList<Status> getStatusForSave()
    {
        return dailyStatus;
    }




    //sum days with a yes and only allow for 1 backslide and 3 no
    public int daysLeft()
    {
        int backslide = 0;
        int no = 0;
        int daysComplete = 0;

        for (int i = dailyStatus.size()-1; i >= 0; i--) {
            if (dailyStatus.get(i).equals(Status.BackSlide)) backslide++;
            if (dailyStatus.get(i).equals(Status.No)) no++;
            if (dailyStatus.get(i).equals(Status.Yes)) daysComplete++;

            if (backslide == 2) break;
            if (no == 4) break;

        }

        return DAYSREQ - daysComplete;
    }


    //endregion


}
