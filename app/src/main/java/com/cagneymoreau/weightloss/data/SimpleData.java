package com.cagneymoreau.weightloss.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.logic.Level;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Store simple key value pairs with shared preferences
 *
 *
 */


public class SimpleData {

    final private static String TAG = "SimpleData";


    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    Gson gson;

    Retriever mornRetreiver = new Retriever(KEY_MORN);
    Retriever noonRetreiver = new Retriever(KEY_NOON);
    Retriever eveningRetreiver = new Retriever(KEY_EVENING);

    /**
     * using this key plus integervalue to retrieve level data
     */
    final private static String key_level = "level-";

    final private static String KEY_MORN = "morn";
    final private static String KEY_NOON = "noon";
    final private static String KEY_EVENING = "evening";
    final private static String KEY_HOUR = "hour";
    final private static String KEY_MIN = "min";
    final private static String KEY_ALARM = "alarm";
    final private static String KEY_LASTENTRY= "lastentry";

    private int level = -1;

    public SimpleData(Context context)
    {

        gson = new Gson();
        sharedPref = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

    }


    //region ---------- levels
    public void saveLevels(ArrayList<Level> levels)
    {

        for (int i = 0; i < 7; i++) {


            ArrayList<String> toSave = new ArrayList<>();
            toSave.add(levels.get(i).getCurrProgress().toString());

            ArrayList<Level.Status> vals = levels.get(i).getStatusForSave();
            for (int j = 0; j < vals.size(); j++) {
                toSave.add(vals.get(j).toString());
            }

            String json = gson.toJson(toSave);
            editor.putString(key_level + i, json);

        }
        editor.commit();

    }


    public ArrayList<Level> getLevels()
    {
        //generate blank history or pull history
        ArrayList<Level> levels = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            levels.add(generateLevel(i));
        }

        //each level is manually designed here
        //0 Game introduction/ starting point/ no associated task

        int a = 0;
        levels.get(a).title = R.string.title_zero;
        levels.get(a).rules = R.string.rules_zero;
        levels.get(a).reward = R.string.reward_zero;
        levels.get(a).link = R.string.link_zero;
        levels.get(a).imageResource = R.drawable.level_0;

        //1
        int b = a +1;
        levels.get(b).title = R.string.title_one;
        levels.get(b).rules = R.string.rules_one;
        levels.get(b).reward = R.string.reward_one;
        levels.get(b).link = R.string.link_one;
        levels.get(b).imageResource = R.drawable.level_one;

        //2
        int c = b + 1;
        levels.get(c).title = R.string.title_two;
        levels.get(c).rules = R.string.rules_two;
        levels.get(c).reward = R.string.reward_two;
        levels.get(c).link = R.string.link_two;
        levels.get(c).imageResource = R.drawable.level_2;

        //3
        int d = c+1;
        levels.get(d).title = R.string.title_three;
        levels.get(d).rules = R.string.rules_three;
        levels.get(d).reward = R.string.reward_three;
        levels.get(d).link = R.string.link_three;
        levels.get(d).imageResource = R.drawable.level_three;

        //4
        int e = d+1;
        levels.get(e).title = R.string.title_four;
        levels.get(e).rules = R.string.rules_four;
        levels.get(e).reward = R.string.reward_four;
        levels.get(e).link = R.string.link_four;
        levels.get(e).imageResource = R.drawable.level_four;

        //5
        int f = e+1;
        levels.get(f).title = R.string.title_five;
        levels.get(f).rules = R.string.rules_five;
        levels.get(f).reward = R.string.reward_five;
        levels.get(f).link = R.string.link_five;
        levels.get(f).imageResource = R.drawable.level_five;

        //6
        int g = f+1;
        levels.get(g).title = R.string.title_six;
        levels.get(g).rules = R.string.rules_six;
        levels.get(g).reward = R.string.reward_six;
        levels.get(g).link = R.string.link_six;
        levels.get(g).imageResource = R.drawable.level_six;


        return levels;


    }


    private Level generateLevel(int position)
    {
        Level level = new Level();

        level.levelNum = position;

        String json = sharedPref.getString(key_level + position, "");
        if (json.isEmpty()){
            level.firstLaunch(position);
        }else {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> arrPackageData = gson.fromJson(json, type);
            level.setCurrProgress(Level.Progress.valueOf(arrPackageData.remove(0)));
            ArrayList<Level.Status> ss = new ArrayList<>();
            for (int i = 0; i < arrPackageData.size(); i++) {
                ss.add(Level.Status.valueOf(arrPackageData.get(i)));
            }
            level.setStatusFromMemory(ss);
        }

        return level;
    }

    //endregion



    //region ======= alarms

    public void setMorn(Pair<Integer, Integer> p)
    {        mornRetreiver.setTime(p);
    }

    public Pair<Integer, Integer> getMorn()
    {        return mornRetreiver.getTime();
    }


    public void setNoon(Pair<Integer, Integer> p)
    {
        noonRetreiver.setTime(p);

    }

    public Pair<Integer, Integer> getNoon()
    {
        return noonRetreiver.getTime();
    }

    public void setEvening(Pair<Integer, Integer> p)
    {
        eveningRetreiver.setTime(p);

    }

    public Pair<Integer, Integer> getEvening()
    {
        return eveningRetreiver.getTime();
    }

    public void setAlarm(boolean b)
    {
        editor.putBoolean(KEY_ALARM, b);
        editor.commit();
    }

    public boolean getAlarm()
    {
        return sharedPref.getBoolean(KEY_ALARM, false);
    }

    /**
     * first = hour, second = minute
     */
    class Retriever{

        private String mTime;

        Retriever(String time){
            mTime = time;
        }

        private void setTime(Pair<Integer, Integer> i)
        {
            editor.putInt(mTime + KEY_HOUR, i.first);
            editor.putInt(mTime + KEY_MIN, i.second);
            editor.commit();
        }


        public Pair<Integer, Integer> getTime()
        {
            return new Pair<>(sharedPref.getInt(mTime + KEY_HOUR, 10), sharedPref.getInt(mTime + KEY_MIN, 0) );
        }


    }




    //endregion


    public void setNextEntry(LocalDateTime ld)
    {
        editor.putString(KEY_LASTENTRY, ld.toString());
        editor.commit();
    }


    public LocalDateTime getNextEntry()
    {
      String v =  sharedPref.getString(KEY_LASTENTRY, "");
        LocalDateTime dt;
        if (v.isEmpty()){
            //this should only come up on first level and should matter
            long tf = 24*60*60*1000;
            return Instant.ofEpochMilli(System.currentTimeMillis() -tf).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        dt = LocalDateTime.parse(v);
        return dt;
    }


}
