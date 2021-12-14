package com.cagneymoreau.weightloss.views.recycleviewpathway;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.weightloss.MainActivity;
import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.logic.GameController;
import com.cagneymoreau.weightloss.logic.Level;
import com.cagneymoreau.weightloss.views.Information_Dialog;
import com.cagneymoreau.weightloss.views.PathWayVisual;

class ViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView levelTv, titleTv, daysToGoTv;
    Button userInputBtn, rulesBtn, videoBtn;
    View view;

    PathWayVisual pathWayVisual;

    int position;

    public ViewHolder(@NonNull View itemView, PathWayVisual pathWayVisual) {
        super(itemView);

        this.pathWayVisual = pathWayVisual;

        view = itemView;

        imageView = itemView.findViewById(R.id.card_imageView);



        levelTv = itemView.findViewById(R.id.level_TextView);
        titleTv = itemView.findViewById(R.id.title_TextView);
        daysToGoTv = itemView.findViewById(R.id.daysToGo_TextView);

        userInputBtn = itemView.findViewById(R.id.button_userInput);
        rulesBtn = itemView.findViewById(R.id.button_rules);
        videoBtn = itemView.findViewById(R.id.button_VideoHelp);

    }

    public void setVals(Level level)
    {
        position = level.levelNum;

        imageView.setImageResource(level.imageResource);



        if (level.getCurrProgress().equals(Level.Progress.notYet)){

            //image
           imageView.setColorFilter(R.color.cardview_dark_background);

           //buttons
            userInputBtn.setVisibility(View.INVISIBLE);
            rulesBtn.setVisibility(View.INVISIBLE);
            videoBtn.setVisibility(View.INVISIBLE);

        }else{

            //image
            imageView.setColorFilter(null);

            //buttons
            userInputBtn.setVisibility(View.VISIBLE);
            rulesBtn.setVisibility(View.VISIBLE);
            videoBtn.setVisibility(View.VISIBLE);

        }

        String l = "Level " + level.levelNum;
        levelTv.setText(l);
        titleTv.setText(level.title);
        daysToGoTv.setText(level.daysToGo());




        if (level.getCurrProgress().equals(Level.Progress.complete)) {

            if (level.levelNum == 0){
                userInputBtn.setText("Settings");
                userInputBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Navigation.findNavController(pathWayVisual.getView()).navigate(R.id.action_global_settings);
                    }
                });

            }else {
                userInputBtn.setText("Level Reward");
                userInputBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog alertDialog = new AlertDialog.Builder(pathWayVisual.getContext()).create();
                        alertDialog.setTitle("Reward");
                        alertDialog.setMessage(level.getReward(pathWayVisual.getContext()));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.show();
                    }
                });
            }

            videoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAction(R.id.action_global_videoButton);
                }
            });

            rulesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAction(R.id.action_global_explanationView);
                }
            });


             }

        else if (level.getCurrProgress().equals(Level.Progress.inProgress)) {

                Pair<String, Boolean> buttonOverride = ((MainActivity) pathWayVisual.getActivity()).getGameController().isDue();

                if (!buttonOverride.second) {
                    // no button data entry
                    userInputBtn.setText(buttonOverride.first);
                    //userInputBtn.setOnClickListener(null); // TODO: 12/13/2021 debug

                    userInputBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (level.levelNum == 0) {
                                level.setCurrProgress(Level.Progress.complete);
                                MainActivity mainActivity = (MainActivity) pathWayVisual.getActivity();
                                GameController g = mainActivity.getGameController();
                                g.checkLevelEarned(level.levelNum);
                                pathWayVisual.updateRecycleView();

                            } else {

                                    clickAction(R.id.action_global_userInput);

                            }

                        }
                    });




                } else {
                    //button press allowed
                    userInputBtn.setText(level.getButtonText());
                    userInputBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (level.levelNum == 0) {
                                level.setCurrProgress(Level.Progress.complete);
                                MainActivity mainActivity = (MainActivity) pathWayVisual.getActivity();
                                GameController g = mainActivity.getGameController();
                                g.checkLevelEarned(level.levelNum);
                                pathWayVisual.updateRecycleView();

                            } else {
                                clickAction(R.id.action_global_userInput);
                            }

                        }
                    });
                }

            videoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAction(R.id.action_global_videoButton);
                }
            });

            rulesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAction(R.id.action_global_explanationView);
                }
            });

        }

    }

    private void clickAction(int p)
    {
        pathWayVisual.popUpReturn(position, p);
    }




}
