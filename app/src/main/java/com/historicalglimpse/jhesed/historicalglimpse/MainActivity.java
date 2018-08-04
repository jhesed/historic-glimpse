package com.historicalglimpse.jhesed.historicalglimpse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    static APIInterface apiInterface;

    public static APIInterface getAPIInterface() {
        return apiInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {

                // ----------------------------- GLIMPSE TODAY -------------------------------------

                case R.id.navigation_today:
                    selectedFragment = new FragmentDaily();
                    break;

                // ------------------------------ GLIMPSE MONTH ------------------------------------

                case R.id.navigation_month:
                    selectedFragment = FragmentMonthly.newInstance();
                    break;

                // ------------------------------ GLIMPSE ABOUT ------------------------------------
                case R.id.navigation_about:
                    selectedFragment = FragmentAbout.newInstance();
                    break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;

                }
            });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, FragmentDaily.newInstance());
        transaction.commit();
    }
}
