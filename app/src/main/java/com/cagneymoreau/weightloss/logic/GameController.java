package com.cagneymoreau.weightloss.logic;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.cagneymoreau.weightloss.MainActivity;
import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.data.SimpleData;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;

import static android.os.Build.VERSION_CODES.R;

/**
 * This game consists of multiple levels in series
 *
 * Each level adds a new rule. The user must perform that rule along with all previous levels rules
 * Each level requires a user response if the action was performed for that day
 *   response can be yes, no or backslide(breaking old rules)
 *   Each level includes a short video cheat to help
 *
 *
 *   user advances to next level if 60 days with no backslide and less than 3 no
 *
 *
 */

// TODO: 11/17/2021 start screen explain

public class GameController {

    final private static String TAG = "GameController";

    Context context;
    SimpleData simpleData;
    ArrayList<Level> levels;
    private final static int daysReq = 60;

    PlayIntegration playIntegration;

    public GameController(Context context) {
        this.context = context;
        simpleData = new SimpleData(context);

        levels = simpleData.getLevels();
    }

    //region --------   loading data

    public ArrayList<Level> getLevelsList() {
        return levels;
    }


    public Level getLevel(int p) {
        return levels.get(p);
    }



    public void save() {
        simpleData.saveLevels(levels);
    }

    //endregion


    // TODO: 12/1/2021 return a quote or encouragement
    public void setResponse(int level, Level.Status input) {
        levels.get(level).addStatus(input);

        checkLevelEarned(levels.get(level).levelNum);

        setNextDue();

        manageAlarm();
    }


    public void checkLevelEarned(int pos) {
        if (levels.get(pos).getCurrProgress().equals(Level.Progress.complete) && pos == levels.size() - 1) {
            // TODO: 12/2/2021 game is beat

            simpleData.setAlarm(false);
        }

        if (levels.get(pos).getCurrProgress().equals(Level.Progress.complete)) {
            levels.get(pos + 1).setCurrProgress(Level.Progress.inProgress);

        }

    }

    public void restartThisLevel(int position)
    {
        for (int i = levels.size()-1; i >= position ; i--) {
            if (position == i){
                levels.get(position).resetLevel(true);
            }else {
                levels.get(position).resetLevel(false);
            }
        }

    }



    //region --------- alarms

    public Pair<Integer, Integer> currLevelAlarm()
    {

        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getCurrProgress().equals(Level.Progress.inProgress)){

                switch (levels.get(i).alarmTime)
                {

                    case 0:
                        return getMornAlarm();

                    case 1:
                        return getNoonAlarm();

                    case 2:
                        return getEveningAlarm();

                    default:
                        return new Pair<>(-1,0);

                }

            }
        }

       return null;
    }

    public void manageAlarm()
    {
        boolean active = simpleData.getAlarm();

        //open app, no need for intent as app should check for user input requirment
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,10784,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //if the user plays with the alarm before starting the game of if the game is over the alarm will be deactive
        Pair<Integer, Integer> t = currLevelAlarm();
        if (active && t.first != -1){

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY + 1, t.first); //alarm is set for 1 hour after reporting is due and 1 hour before next epoch
            calendar.set(Calendar.MINUTE, t.second);

            //nothing due today so start tomorrow -> ex. user has entered todays data
            if (!isDue().second){
                calendar.add(Calendar.DATE, 1);
            }

            alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }else{

            alarmManager.cancel(pendingIntent);

        }

    }

    public boolean getAlarmActive() {
        return simpleData.getAlarm();
    }

    public void setAlarmActive(boolean b)
    {
        simpleData.setAlarm(b);
    }

    public void setMornAlarm(Pair<Integer, Integer> val)
    {
        simpleData.setMorn(val);
    }

    public Pair<Integer, Integer> getMornAlarm()
    {
       return simpleData.getMorn();
    }

    public void setNoonAlarm(Pair<Integer, Integer> val)
    {
       simpleData.setNoon(val);
    }

    public Pair<Integer, Integer> getNoonAlarm()
    {
      return   simpleData.getNoon();
    }

    public void setEveningAlarm(Pair<Integer, Integer> val)
    {
        simpleData.setEvening(val);
    }

    public Pair<Integer, Integer> getEveningAlarm()
    {
       return simpleData.getEvening();

    }

    //endregion

    //true will disable the button as nothing due
    public Pair<String, Boolean> isDue()
    {
        LocalDateTime due = simpleData.getNextEntry();

        LocalDateTime now = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (now.isAfter(due)){

            return new Pair<>("", true);

        }

        int hours = (int) now.until(due, ChronoUnit.HOURS);

        return new Pair<>( hours + " hours till next entry", false);

    }

    public void setNextDue()
    {
        Pair<Integer, Integer> hourmin = currLevelAlarm();

        LocalDateTime next = Instant.ofEpochMilli(System.currentTimeMillis() ).atZone(ZoneId.systemDefault()).toLocalDateTime();
        next = next.plusDays(1);
        next = next.withHour(hourmin.first);
        next = next.withMinute(hourmin.second);

        simpleData.setNextEntry(next);

    }




    public void setPlayStoreIntegration(PlayIntegration pl)
    {
        playIntegration = pl;
    }

    public void rate(Activity a)
    {
        playIntegration.goToRating(a);
    }

    public boolean showRating()
    {
        return playIntegration.showRating();
    }

    public boolean allowAccess(Fragment fragment)
    {
        return playIntegration.grantAccess(fragment);
    }

}
