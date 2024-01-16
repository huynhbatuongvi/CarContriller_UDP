package com.example.final_exam;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    // khai báo biến
    private FrameLayout frameLayout;
    private int currentTab = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) { // khởi tạo sự kiện
        super.onCreate(savedInstanceState); // khởi tạo sự kiện cua Activity
        setContentView(R.layout.activity_main);


        // ánh xạ biến
        frameLayout = findViewById(R.id.containers);
        final LinearLayout hubLayout = findViewById(R.id.hublyt);
        final LinearLayout gyroLayout = findViewById(R.id.gyrolyt);
        final LinearLayout settingLayout = findViewById(R.id.settinglyt);
        final LinearLayout logoutLayout = findViewById(R.id.logoutlyt);

        final ImageView hubImage = findViewById(R.id.hubImg);
        final ImageView gyroImage = findViewById(R.id.gyroImg);
        final ImageView settingImage = findViewById(R.id.settingImg);
        final ImageView logoutImage = findViewById(R.id.logoutImg);

        final TextView hubText = findViewById(R.id.hubTxt);
        final TextView gyroText = findViewById(R.id.gyroTxt);
        final TextView settingText = findViewById(R.id.settingTxt);
        final TextView logoutText = findViewById(R.id.logoutTxt);

        getSupportFragmentManager().beginTransaction()
               .setReorderingAllowed(true).replace(R.id.containers, SettingFragment.class, null)
                .commit();

            hubLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTab != 2) {
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true).replace(R.id.containers, JoystickFragment.class, null)
                                .commit();

                        settingText.setVisibility(View.GONE);
                        logoutText.setVisibility(View.GONE);
                        gyroText.setVisibility(View.GONE);

                        settingImage.setImageResource(R.drawable.nav_settings);
                        logoutImage.setImageResource(R.drawable.drawer_logout);
                        gyroImage.setImageResource(R.drawable.baseline_api_24);

                        settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        logoutLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        gyroLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                        hubText.setVisibility(View.VISIBLE);
                        hubImage.setImageResource(R.drawable.baseline_games_24);
                        hubLayout.setBackgroundResource(R.drawable.roundback4);

                        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                        scaleAnimation.setDuration(500);
                        scaleAnimation.setFillAfter(true);
                        hubLayout.startAnimation(scaleAnimation);

                        currentTab = 2;
                    }
                }
            });

            gyroLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTab != 3) {
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true).replace(R.id.containers, HubFragment.class, null)
                                .commit();

                        settingText.setVisibility(View.GONE);
                        logoutText.setVisibility(View.GONE);
                        hubText.setVisibility(View.GONE);

                        settingImage.setImageResource(R.drawable.nav_settings);
                        logoutImage.setImageResource(R.drawable.drawer_logout);
                        hubImage.setImageResource(R.drawable.baseline_games_24);

                        settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        logoutLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        hubLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                        gyroText.setVisibility(View.VISIBLE);
                        gyroImage.setImageResource(R.drawable.baseline_api_24);
                        gyroLayout.setBackgroundResource(R.drawable.roundback);

                        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                        scaleAnimation.setDuration(500);
                        scaleAnimation.setFillAfter(true);
                        gyroLayout.startAnimation(scaleAnimation);

                        currentTab = 3;
                    }
                }
            });

            settingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTab != 1) {
                        // Chuyển về tab home khi nhấn back
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true).replace(R.id.containers, SettingFragment.class, null)
                                .commit();

                        hubText.setVisibility(View.GONE);
                        logoutText.setVisibility(View.GONE);
                        gyroText.setVisibility(View.GONE);

                        hubImage.setImageResource(R.drawable.baseline_games_24);
                        logoutImage.setImageResource(R.drawable.drawer_logout);
                        gyroImage.setImageResource(R.drawable.baseline_api_24);


                        hubLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        logoutLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        gyroLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                        settingText.setVisibility(View.VISIBLE);
                        settingImage.setImageResource(R.drawable.nav_settings);
                        settingLayout.setBackgroundResource(R.drawable.roundback2);

                        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                        scaleAnimation.setDuration(500);
                        scaleAnimation.setFillAfter(true);
                        settingLayout.startAnimation(scaleAnimation);

                        currentTab = 1;
                    }

                }
            });

            logoutLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTab != 4) {

                        hubText.setVisibility(View.GONE);
                        settingText.setVisibility(View.GONE);
                        gyroText.setVisibility(View.GONE);

                        hubImage.setImageResource(R.drawable.baseline_games_24);
                        settingImage.setImageResource(R.drawable.nav_settings);
                        gyroImage.setImageResource(R.drawable.baseline_api_24);

                        hubLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        settingLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        gyroLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                        logoutText.setVisibility(View.VISIBLE);
                        logoutImage.setImageResource(R.drawable.drawer_logout);
                        logoutLayout.setBackgroundResource(R.drawable.roundback3);

                        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                        scaleAnimation.setDuration(500);
                        scaleAnimation.setFillAfter(true);
                        logoutLayout.startAnimation(scaleAnimation);

                        currentTab = 4;

                    } else {
                        logout();
                    } // Nếu đang ở tab logout và nhấn back một lần nữa
                }
            });
        }

    boolean backPressedOnce = false; // khai báo giá trị
    private void logout() {
        if (backPressedOnce){  // nếu đã nhấn 2 lần
            super.onBackPressed();// thực hiện thoát ứng dụng
        } else {
            backPressedOnce = true;// nếu đã nhấn 1 lần
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedOnce = false;
                }//trả giá trị false nếu cách nhau quá 2s
            }, 2000);
        }
    }
}