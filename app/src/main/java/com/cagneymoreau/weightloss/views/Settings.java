package com.cagneymoreau.weightloss.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.cagneymoreau.weightloss.MainActivity;
import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.logic.GameController;
import com.cagneymoreau.weightloss.logic.Level;

import jp.wasabeef.blurry.Blurry;

public class Settings extends Fragment {

    final private static String TAG = "settingsview";

    private View frag;

    GameController gameController;

    TextView expTv, mornTv, noonTv, eveningTv;

    TimePicker mornPicker, noonPicker, eveningPicker;

    SwitchCompat alarmSwitch;

    ImageView imageView;

    Button restartButton;

    boolean saved;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frag = inflater.inflate(R.layout.settings_fragment, container, false);

        MainActivity activity = (MainActivity) getActivity();
        gameController = activity.getGameController();

        buildView();

        saved = false;

        return frag;
    }


    private void buildView()
    {
        imageView = frag.findViewById(R.id.setting_imageView);

        expTv = frag.findViewById(R.id.settingExplain_textview);
        expTv.setText(getString(R.string.settings_explain));

        String set = "Set the reminder for 2 hours before ";
        mornTv = frag.findViewById(R.id.morn_textview);
        mornTv.setText(set + "lunch");
        noonTv = frag.findViewById(R.id.noon_textview);
        noonTv.setText(set + "dinner");
        eveningTv = frag.findViewById(R.id.evening_textview);
        eveningTv.setText(set + "bed");

        mornPicker = frag.findViewById(R.id.morn_picker);
        Pair<Integer, Integer> morn = gameController.getMornAlarm();
        mornPicker.setHour(morn.first);
        mornPicker.setMinute(morn.second);
        noonPicker = frag.findViewById(R.id.noon_picker);
        Pair<Integer, Integer> noon = gameController.getNoonAlarm();
        noonPicker.setHour(noon.first);
        noonPicker.setMinute(noon.second);
        eveningPicker = frag.findViewById(R.id.evening_picker);
        Pair<Integer, Integer> evening = gameController.getEveningAlarm();
        eveningPicker.setHour(evening.first);
        eveningPicker.setMinute(evening.second);

        alarmSwitch = frag.findViewById(R.id.alarm_switch);
        alarmSwitch.setChecked(gameController.getAlarmActive());

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        restartButton = frag.findViewById(R.id.restartGameButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Information_Dialog("Do you really wish to reset the entire game? You will start from the beginning and lose all progress.", gameController).show(getChildFragmentManager(), "info dialog");
            }
        });

    }



    private void saveInput()
    {
        if (saved) return;
        saved = true;

        Pair<Integer, Integer> mornV = new Pair<>(mornPicker.getHour(),mornPicker.getMinute());
        Pair<Integer, Integer> noonV = new Pair<>(noonPicker.getHour(),noonPicker.getMinute());
        Pair<Integer, Integer> eveningV = new Pair<>(eveningPicker.getHour(),eveningPicker.getMinute());

        gameController.setMornAlarm(mornV);
        gameController.setNoonAlarm(noonV);
        gameController.setEveningAlarm(eveningV);

        gameController.setAlarmActive(alarmSwitch.isChecked());

        gameController.manageAlarm();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveInput();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveInput();
    }
}
