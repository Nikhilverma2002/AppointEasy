package com.nikhil.appointeasy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nikhil.appointeasy.Adapter.IntroViewPagerAdapter;
import com.nikhil.appointeasy.Model.intro_ScreenItem;

import java.util.ArrayList;
import java.util.List;

public class Onboarding extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    LinearLayout btnNext;
    int position = 0;
    FirebaseAuth auth;
    FirebaseUser user;
    LinearLayout btnGetStarted;
    Animation btnAnim;
    TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // when this activity is about to be launch we need to check if its openened before or not
        setContentView(R.layout.activity_onboarding);
        Window window = Onboarding.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Onboarding.this, R.color.white));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // ini views
        btnNext = findViewById(R.id.next);
        btnGetStarted = findViewById(R.id.getstarted);
        tabIndicator = findViewById(R.id.tab_indicator);
        //btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);
        // fill list screen
        final List<intro_ScreenItem> mList = new ArrayList<>();
        mList.add(new intro_ScreenItem("Get to know about Doctors", "About doctor's experience, specialization, availability etc...", R.drawable.ic_1));
            mList.add(new intro_ScreenItem("Locate Hospitals", "Navigate to Hospitals with location feature.", R.drawable.ic_2));
        mList.add(new intro_ScreenItem("Easy Appointment Booking", "Appointment to any doctor is reduced to a touch", R.drawable.ic_3));
        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);
        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);
        // next button click Listner


        btnNext.setOnClickListener(v -> {
            position = screenPager.getCurrentItem();
            if (position < mList.size()) {
                position++;
                screenPager.setCurrentItem(position);
            }
            if (position == mList.size() - 1) { // when we rech to the last screen
                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen();
            }
        });
        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loaddLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // Get Started button click listener
        btnGetStarted.setOnClickListener(v -> {
            //open main activity
            Intent mainActivity = new Intent(getApplicationContext(), login.class);
            startActivity(mainActivity);
            // also we need to save a boolean value to storage so next time when the user run the app
            // we could know that he is already checked the intro screen activity
            // i'm going to use shared preferences to that process
            //savePrefsData();
            finish();
        });
        // skip button click listener
        tvSkip.setOnClickListener(v -> screenPager.setCurrentItem(mList.size()));

    }


    private void loaddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        //tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        //btnGetStarted.setAnimation(btnAnim);
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        boolean is_authorized = getSharedPreferences("Authorized_for_Access", MODE_PRIVATE)
                .getBoolean("is_Authorized_to_access_the_app", false);

        boolean is_student = getSharedPreferences("our_user?", MODE_PRIVATE)
                .getBoolean("student", true);

        Log.e("sharing val", String.valueOf(is_authorized));
        Log.e("sharing val2", String.valueOf(is_student));

        if (user != null) {

            startActivity(new Intent(Onboarding.this, Home.class));
            finish();
        }

    }
}