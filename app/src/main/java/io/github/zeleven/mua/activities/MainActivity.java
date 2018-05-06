package io.github.zeleven.mua.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;


import gr.net.maroulis.library.EasySplashScreen;
import io.github.zeleven.mua.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EasySplashScreen config = new EasySplashScreen(MainActivity.this)
                .withFullScreen()
                .withTargetActivity(Main2Activity.class)
                .withSplashTimeOut(2000)
                .withHeaderText("")
                .withFooterText("")
                .withBeforeLogoText("")
                .withLogo(R.drawable.bg1)
                .withAfterLogoText("");


        //set your own animations
        myCustomTextViewAnimation(config.getFooterTextView());

        //customize all TextViews
        Typeface pacificoFont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        config.getAfterLogoTextView().setTypeface(pacificoFont);

        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.WHITE);

        //create the view
        View easySplashScreenView = config.create();

        setContentView(easySplashScreenView);
    }

    private void myCustomTextViewAnimation(TextView tv){
        Animation animation=new TranslateAnimation(0,0,480,0);
        animation.setDuration(1200);
        tv.startAnimation(animation);
    }
}
