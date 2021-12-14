package com.cagneymoreau.weightloss.views;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.cagneymoreau.weightloss.MainActivity;
import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.logic.GameController;
import com.cagneymoreau.weightloss.logic.Level;

/**
 * Askes question and collects response. Gives encouragment
 */

public class UserInput extends Fragment {


    final private static String TAG = "userinput";

    private View frag;
    GameController gameController;
    TextView textView, backslideTv;
    Button yesButton, noButton, backslideButton;

    ImageView imageView;

    int levelVal = -10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frag = inflater.inflate(R.layout.user_input_fragment, container, false);

        textView = frag.findViewById(R.id.user_input_textview);
        backslideTv = frag.findViewById(R.id.backslide_exp_TextView);
        backslideTv.setText(R.string.input_instructions);

        imageView = frag.findViewById(R.id.userinput_imageView);

        yesButton = frag.findViewById(R.id.yes_buton);
        noButton = frag.findViewById(R.id.no_button);
        backslideButton = frag.findViewById(R.id.backslide_button);


        MainActivity activity = (MainActivity) getActivity();
        gameController = activity.getGameController();

        levelVal = getArguments().getInt("level", -10);

        if (levelVal == -10){
            activity.onBackPressed();
            Log.e(TAG, "onCreateView: " );
        }
        buildTtitleandBack();
        buildView();

        return frag;
    }

    private void buildTtitleandBack()
    {
        imageView.setImageResource(R.drawable.inputbackground);
        textView.setText("Did you meet the goal?");
    }

    private void buildView()
    {

        noButton.setText("Nope");
        noButton.setTextAppearance(R.style.bannerTextRed);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.no_sound);
                mp.start();
                gameController.setResponse(levelVal, Level.Status.No);
                getActivity().onBackPressed();
            }
        });

        yesButton.setText("Yes!");
        yesButton.setTextAppearance(R.style.bannerTextGreen);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.yes_sound);
                mp.start();
                // TODO: 12/13/2021 debug

                gameController.setResponse(levelVal, Level.Status.Yes);
                for (int i = 0; i < 50; i++) {
                   //gameController.setResponse(levelVal, Level.Status.Yes);
                }
                getActivity().onBackPressed();
            }
        });

        backslideButton.setText("Backslide");
        backslideButton.setTextAppearance(R.style.bannerText);
       backslideButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.backside_sound);
               mp.start();
               gameController.setResponse(levelVal, Level.Status.BackSlide);
               getActivity().onBackPressed();
           }
       });


    }






}
