package com.historicalglimpse.jhesed.historicalglimpse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.historicalglimpse.jhesed.historicalglimpse.pojo.Datum;
import com.historicalglimpse.jhesed.historicalglimpse.pojo.GlimpseResource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    APIInterface apiInterface;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    return true;
                case R.id.navigation_month:
                    return true;
                case R.id.navigation_about:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        // Progress bar spinner
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Container layouts
        final LinearLayout rContainerWh = (LinearLayout)this.findViewById(R.id.container_wh);
        final LinearLayout rContainerPh = (LinearLayout)this.findViewById(R.id.container_ph);
        final Button rShareButton = (Button)this.findViewById(R.id.share_button);
        rContainerWh.setVisibility(View.GONE);
        rContainerPh.setVisibility(View.GONE);

        // Retrieve layout objects (world history)
        final TextView rHeadingWh = (TextView)this.findViewById(R.id.title_wh);
        final TextView rFeaturedVerseWh = (TextView)this.findViewById(R.id.featured_verse_wh);
        final TextView rContentWh = (TextView)this.findViewById(R.id.content_wh);
        final TextView rPrayerFocusWh = (TextView)this.findViewById(R.id.prayer_focus_wh);
        final TextView rFeaturedQuoteWh = (TextView)this.findViewById(R.id.featured_quote_wh);

        // Retrieve layout objects (philippinen history)
        final TextView rHeadingPhil = (TextView)this.findViewById(R.id.title_phil);
        final TextView rFeaturedVersePhil = (TextView)this.findViewById(R.id.featured_verse_phil);
        final TextView rContentPhil = (TextView)this.findViewById(R.id.content_phil);
        final TextView rPrayerFocusPhil = (TextView)this.findViewById(R.id.prayer_focus_phil);
        final TextView rFeaturedQuotePhil = (TextView)this.findViewById(R.id.featured_quote_phil);

        final TextView errorMessage = (TextView)this.findViewById(R.id.error_message);

        // Get today's historical glimpse
        Call<GlimpseResource> call = apiInterface.getGlimpseToday();

        call.enqueue(new Callback<GlimpseResource>() {
            @Override
            public void onResponse(Call<GlimpseResource> call, Response<GlimpseResource> response) {

                GlimpseResource resource = response.body();

                List<Datum> data = resource.getData();
                Datum whData = null;
                Datum phData = null;
                if (data.size() >= 1)
                    whData = data.get(0);
                if (data.size() == 2)
                    phData = data.get(1);

                // -----------------------  SECTION :: World History -------------------------------

                if (whData != null) {
                    String headingWh = whData.getHeading();
                    String featuredVerseWh = whData.getFeaturedVerse();
                    String contentWh = whData.getContent();
                    String prayerFocusWh = whData.getPrayerFocus();
                    String featuredQuoteWh = whData.getFeaturedQuote();

                    // Update dynamic content
                    rHeadingWh.setText(headingWh);
                    rFeaturedVerseWh.setText(featuredVerseWh);
                    rContentWh.setText(contentWh);
                    rPrayerFocusWh.setText(prayerFocusWh);
                    rFeaturedQuoteWh.setText(featuredQuoteWh);

                    rContainerWh.setVisibility(View.VISIBLE);
                    rShareButton.setVisibility(View.VISIBLE);
                }

                // -----------------------  SECTION :: Philippine History --------------------------

                if (phData != null) {
                    String headingPhil = phData.getHeading();
                    String featuredVersePhil = phData.getFeaturedVerse();
                    String contentPhil = phData.getContent();
                    String prayerFocusPhil = phData.getPrayerFocus();
                    String featuredQuotePhil = phData.getFeaturedQuote();

                    // Update dynamic content
                    rHeadingWh.setText(headingPhil);
                    rFeaturedVersePhil.setText(featuredVersePhil);
                    rContentPhil.setText(contentPhil);
                    rPrayerFocusPhil.setText(prayerFocusPhil);
                    rFeaturedQuotePhil.setText(featuredQuotePhil);

                    rContainerPh.setVisibility(View.VISIBLE);
                    rShareButton.setVisibility(View.VISIBLE);
                }

                // -----------------------  SECTION :: Error Message -------------------------------

                if (whData == null && phData == null) {
                    errorMessage.setVisibility(View.VISIBLE);
                    rShareButton.setVisibility(View.GONE);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GlimpseResource> call, Throwable t) {
                errorMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                call.cancel();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
