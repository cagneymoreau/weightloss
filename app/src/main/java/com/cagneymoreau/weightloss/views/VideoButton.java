package com.cagneymoreau.weightloss.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.cagneymoreau.weightloss.MainActivity;
import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.logic.GameController;
import com.cagneymoreau.weightloss.logic.Level;

import jp.wasabeef.blurry.Blurry;

/**
 * Show a video explaining something about this level
 * button takes you back to pathway
 *
 */

public class VideoButton extends Fragment {

    final private static String TAG = "videobutton";

    private View frag;
    GameController gameController;
    WebView videoWebView;
    Button videoButton;

    ImageView imageView;

    int levelVal = -10;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frag = inflater.inflate(R.layout.video_button_fragment, container, false);

        videoWebView = frag.findViewById(R.id.video_webview);

        //videoButton = frag.findViewById(R.id.videoButton);
        imageView = frag.findViewById(R.id.videobutton_imageView);
        //imgRate = frag.findViewById(R.id.imageview_video_rateApp);

        MainActivity activity = (MainActivity) getActivity();
        gameController = activity.getGameController();

        levelVal = getArguments().getInt("level", -10);

        if (levelVal == -10){
            activity.onBackPressed();
            Log.e(TAG, "onCreateView: " );
        }

        Level level = gameController.getLevel(levelVal);
        background(level);

        //buildVidOne(level);

        buildVidTwo(level);



        return frag;
    }

    private void background(Level level)
    {

        //imageView.setImageResource(level.imageResource);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap blurTemplate = BitmapFactory.decodeResource(getResources(), level.imageResource, options);
        Blurry.with(getContext()).from(blurTemplate).into(imageView);
    }



    private void buildVidTwo(Level level)
    {

        videoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = videoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        videoWebView.loadData(level.getLink(getContext()), "text/html", "utf-8");

    }





}
