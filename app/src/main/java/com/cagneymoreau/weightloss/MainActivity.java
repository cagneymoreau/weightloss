package com.cagneymoreau.weightloss;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.cagneymoreau.weightloss.logic.GameController;
import com.cagneymoreau.weightloss.logic.PlayIntegration;

/**
 *
 *
 */
public class MainActivity extends AppCompatActivity {


    GameController gameController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameController = new GameController(this);
        PlayIntegration pl = new PlayIntegration(this);
        gameController.setPlayStoreIntegration(pl);


    }



    public GameController getGameController()
    {
        return gameController;
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameController.save();
    }


}