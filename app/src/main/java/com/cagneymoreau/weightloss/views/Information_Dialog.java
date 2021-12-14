package com.cagneymoreau.weightloss.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.logic.GameController;


public class Information_Dialog extends DialogFragment {

    String text;
    GameController gameController;

    public Information_Dialog(String text, GameController gc) {

        this.text = text;
        gameController = gc;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v;


        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v = inflater.inflate(R.layout.info_dialog, null));
        TextView tv = v.findViewById(R.id.infoDialog_TV);
        tv.setText(text);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gameController.restartThisLevel(0);
                Information_Dialog.this.getDialog().cancel();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Information_Dialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

}
