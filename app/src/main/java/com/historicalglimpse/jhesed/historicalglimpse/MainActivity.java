package com.historicalglimpse.jhesed.historicalglimpse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.historicalglimpse.jhesed.historicalglimpse.adapters.GlimpseAdapter;
import com.historicalglimpse.jhesed.historicalglimpse.models.Glimpse;
import com.historicalglimpse.jhesed.historicalglimpse.pojo.DatumDetails;
import com.historicalglimpse.jhesed.historicalglimpse.pojo.DatumList;
import com.historicalglimpse.jhesed.historicalglimpse.pojo.GlimpseDetailsResource;
import com.historicalglimpse.jhesed.historicalglimpse.pojo.GlimpseListResource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    APIInterface apiInterface;
    BottomNavigationView navigation;
    private ListView glimpseListView;
    private GlimpseAdapter glimpseAdapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                // ----------------------------- GLIMPSE TODAY -------------------------------------
                case R.id.navigation_today:
                    setContentView(R.layout.activity_main);
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


                // ------------------------------ GLIMPSE MONTH ------------------------------------

                case R.id.navigation_month:

                    setContentView(R.layout.activity_glimpse_list);

                    final TextView errorMessage = (TextView) findViewById(R.id.error_message);
                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

                    progressBar.setVisibility(View.VISIBLE);

                    // Get Month worth of historical glimpse
                    Call<GlimpseListResource> call = apiInterface.getGlimpseList(
                            getMonthAsString());

                    call.enqueue(new Callback<GlimpseListResource>() {
                        @Override
                        public void onResponse(Call<GlimpseListResource> call,
                                               Response<GlimpseListResource> response) {

                            GlimpseListResource resource = response.body();
                            List<DatumList> data = resource.getData();

                            progressBar.setVisibility(GONE);

                            // Map response to Glimpse model object
                            ArrayList<Glimpse> glimpseListData = new ArrayList<>();
                            for (int i=0; i<data.size(); i++) {

                                glimpseListData.add(new Glimpse(
                                        getDayAsString(data.get(i).getGlimpseDate()),
                                        data.get(i).getGlimpseDate(),
                                        data.get(i).getHeadingWorld(),
                                        data.get(i).getHeadingPhil()));
                            }

                            if (glimpseAdapter == null) {
                                glimpseListView = (ListView) findViewById(R.id.glimpseList);
//                                setListViewHeightBasedOnChildren(glimpseListView);

                                glimpseAdapter = new GlimpseAdapter(MainActivity.this,
                                        glimpseListData);
                                glimpseListView.setAdapter(glimpseAdapter);
                            }
                            glimpseListView.setAdapter(glimpseAdapter);

                            // Hide error message and progress bar
                            errorMessage.setVisibility(View.GONE);
                            progressBar.setVisibility(GONE);
                        }

                        @Override
                        public void onFailure(Call<GlimpseListResource> call, Throwable t) {
                            errorMessage.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(GONE);
                            call.cancel();
                        }
                    });

                    return true;
                case R.id.navigation_about:
                    return true;
            }

            return false;
        }

    };

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        /**** Method for Setting the Height of the ListView dynamically.
         **** Hack to fix the issue of not showing all the items of the ListView
         **** when placed inside a ScrollView
         *  source: https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
         * ****/

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(
                listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(
                        desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void getGlimpse(String date) {

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
        Call<GlimpseDetailsResource> call = apiInterface.getGlimpseToday(date);

        call.enqueue(new Callback<GlimpseDetailsResource>() {
            @Override
            public void onResponse(Call<GlimpseDetailsResource> call, Response<GlimpseDetailsResource> response) {

                GlimpseDetailsResource resource = response.body();

                List<DatumDetails> data = resource.getData();
                DatumDetails whData = null;
                DatumDetails phData = null;
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
            public void onFailure(Call<GlimpseDetailsResource> call, Throwable t) {
                errorMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                call.cancel();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = APIClient.getClient(this).create(APIInterface.class);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // -------------------------------- Load initial view (TODAY) ------------------------------
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
    }

    public String getTodayAsString() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c);
    }

    public String getMonthAsString() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        formattedDate = formattedDate.substring(0, formattedDate.lastIndexOf("-"));
        return formattedDate;
    }

    public String getDayAsString(String date) {
        return date.substring(date.lastIndexOf("-")+1, date.length());
    }
}
