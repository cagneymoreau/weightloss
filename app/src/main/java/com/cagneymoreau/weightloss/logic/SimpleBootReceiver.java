package com.cagneymoreau.weightloss.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SimpleBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            GameController gameController = new GameController(context);
            gameController.manageAlarm();
        }
    }
}