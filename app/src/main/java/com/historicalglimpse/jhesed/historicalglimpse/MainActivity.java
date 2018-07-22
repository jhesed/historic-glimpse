package com.historicalglimpse.jhesed.historicalglimpse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.historicalglimpse.jhesed.historicalglimpse.pojo.Datum;
import com.historicalglimpse.jhesed.historicalglimpse.pojo.GlimpseResource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    APIInterface apiInterface;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    getGlimpse(getTodayAsString());

                    // Pull to refresh event
                    final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout)
                            findViewById(R.id.pull_to_refresh);
                    pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getGlimpse(getTodayAsString());
                            pullToRefresh.setRefreshing(false);
                        }
                    });
                    return true;
                case R.id.navigation_month:
                    return true;
                case R.id.navigation_about:
                    return true;
            }
            return false;
        }

    };

    protected void getGlimpse(String date) {

        // Progress bar spinner
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Container layouts
        final LinearLayout rContainerWh = (LinearLayout)this.findViewById(R.id.container_wh);
        final LinearLayout rContainerPh = (LinearLayout)this.findViewById(R.id.container_ph);
        final Button rShareButton = (Button)this.findViewById(R.id.share_button);
        rContainerWh.setVisibility(GONE);
        rContainerPh.setVisibility(GONE);

        // Retrieve layout objects (world history)
        final TextView rHeadingWh = (TextView)this.findViewById(R.id.title_wh);
        final TextView rFeaturedVerseWh = (TextView)this.findViewById(R.id.featured_verse_wh);
        final TextView rContentWh = (TextView)this.findViewById(R.id.content_wh);
        final TextView rPrayerFocusWh = (TextView)this.findViewById(R.id.prayer_focus_wh);
        final TextView rFeaturedQuoteWh = (TextView)this.findViewById(R.id.featured_quote_wh);

        // Retrieve layout objects (philippinne history)
        final TextView rHeadingPhil = (TextView)this.findViewById(R.id.title_phil);
        final TextView rFeaturedVersePhil = (TextView)this.findViewById(R.id.featured_verse_phil);
        final TextView rContentPhil = (TextView)this.findViewById(R.id.content_phil);
        final TextView rPrayerFocusPhil = (TextView)this.findViewById(R.id.prayer_focus_phil);
        final TextView rFeaturedQuotePhil = (TextView)this.findViewById(R.id.featured_quote_phil);

        final TextView errorMessage = (TextView)this.findViewById(R.id.error_message);

        // Get today's historical glimpse
        Call<GlimpseResource> call = apiInterface.getGlimpseToday(date);

        call.enqueue(new Callback<GlimpseResource>() {
            @Override
            public void onResponse(Call<GlimpseResource> call, Response<GlimpseResource> response) {

                GlimpseResource resource = response.body();

                List<Datum> data = resource.getData();
                Datum whData = null;
                Datum phData = null;
                if (data != null) {
                    if (data.size() >= 1)
                        whData = data.get(0);
                    if (data.size() == 2)
                        phData = data.get(1);
                }

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
                    errorMessage.setVisibility(View.GONE);
                }

                // -----------------------  SECTION :: Philippine History --------------------------

                if (phData != null) {
                    String headingPhil = phData.getHeading();
                    String featuredVersePhil = phData.getFeaturedVerse();
                    String contentPhil = phData.getContent();
                    String prayerFocusPhil = phData.getPrayerFocus();
                    String featuredQuotePhil = phData.getFeaturedQuote();

                    // Update dynamic content
                    rHeadingPhil.setText(headingPhil);
                    rFeaturedVersePhil.setText(featuredVersePhil);
                    rContentPhil.setText(contentPhil);
                    rPrayerFocusPhil.setText(prayerFocusPhil);
                    rFeaturedQuotePhil.setText(featuredQuotePhil);

                    rContainerPh.setVisibility(View.VISIBLE);
                    rShareButton.setVisibility(View.VISIBLE);
                    errorMessage.setVisibility(View.GONE);
                }

                // -----------------------  SECTION :: Error Message -------------------------------

                if (whData == null && phData == null) {
                    errorMessage.setVisibility(View.VISIBLE);
                    rShareButton.setVisibility(GONE);
                }

                progressBar.setVisibility(GONE);
            }

            @Override
            public void onFailure(Call<GlimpseResource> call, Throwable t) {
                errorMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                call.cancel();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTodayAsString();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected String getTodayAsString() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c);
    }

    protected String getMonthAsString() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        formattedDate = formattedDate.substring(0, formattedDate.lastIndexOf("-"));
        return formattedDate;
    }

}
