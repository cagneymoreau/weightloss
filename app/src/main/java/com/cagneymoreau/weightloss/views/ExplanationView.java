package com.cagneymoreau.weightloss.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.PorterDuff;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cagneymoreau.weightloss.MainActivity;
import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.logic.GameController;
import com.cagneymoreau.weightloss.logic.Level;

import jp.wasabeef.blurry.Blurry;

/**
 * Wall of text
 *
 */


public class ExplanationView extends Fragment {


    final private static String TAG = "explanationview";

    private View frag;
    GameController gameController;
    TextView titleTv, contentTv;
    Button rateButton;

    ImageView imageView, imgRate;

    int levelVal = -10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frag = inflater.inflate(R.layout.explanation_fragment, container, false);

       titleTv = frag.findViewById(R.id.explanation_title_textview);
       contentTv = frag.findViewById(R.id.explanation_body_textView);
       imageView = frag.findViewById(R.id.explanation_imageView);

       imgRate = frag.findViewById(R.id.imageview_expl_rateApp);

       rateButton = frag.findViewById(R.id.buttonRate);

        MainActivity activity = (MainActivity) getActivity();
        gameController = activity.getGameController();

        levelVal = getArguments().getInt("level", -10);

        if (levelVal == -10){
            activity.onBackPressed();
            Log.e(TAG, "onCreateView: " );
        }

        buildView();

        buildRating();

        return frag;

    }


    private void buildView()
    {

      Level level = gameController.getLevel(levelVal);

      titleTv.setText(level.title);
      contentTv.setText(level.rules);
      //imageView.setImageResource(level.imageResource);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap blurTemplate = BitmapFactory.decodeResource(getResources(), level.imageResource, options);
        Blurry.with(getContext()).from(blurTemplate).into(imageView);



    }


    private void buildRating()
    {

        if (gameController.showRating()){
            imgRate.setVisibility(View.VISIBLE);
            imgRate.setImageResource(R.drawable.rate_req);

            rateButton.setVisibility(View.VISIBLE);
            rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameController.rate(getActivity());
                }
            });

        }else{
            rateButton.setVisibility(View.INVISIBLE);
        }
    }



}
