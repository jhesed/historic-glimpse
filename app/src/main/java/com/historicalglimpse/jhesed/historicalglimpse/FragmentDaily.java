package com.historicalglimpse.jhesed.historicalglimpse;

/**
 * Created by jhesed on 8/4/2018.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.historicalglimpse.jhesed.historicalglimpse.pojo.DatumDetails;
import com.historicalglimpse.jhesed.historicalglimpse.pojo.GlimpseDetailsResource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class FragmentDaily extends Fragment {

    APIInterface apiInterface;

    public static FragmentDaily newInstance() {
        FragmentDaily fragment = new FragmentDaily();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Make this common to fragments
        apiInterface = APIClient.getClient(this.getContext()).create(APIInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        getGlimpse(getTodayAsString(), view);

        // Pull to refresh event
//        final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout)
//                view.findViewById(R.id.pull_to_refresh);

        //  TODO: Fix me
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getGlimpse(getTodayAsString(), view);
//                pullToRefresh.setRefreshing(false);
//            }
//        });

    }

    public void getGlimpse(String date, View view) {

        // Progress bar spinner
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Container layouts
        final LinearLayout rContainerWh = view.findViewById(R.id.container_wh);
        final LinearLayout rContainerPh = view.findViewById(R.id.container_ph);
        final Button rShareButton = view.findViewById(R.id.share_button);
        rContainerWh.setVisibility(GONE);
        rContainerPh.setVisibility(GONE);

        // Retrieve layout objects (world history)
        final TextView rHeadingWh = view.findViewById(R.id.title_wh);
        final TextView rFeaturedVerseWh = view.findViewById(R.id.featured_verse_wh);
        final TextView rContentWh = view.findViewById(R.id.content_wh);
        final TextView rPrayerFocusWh = view.findViewById(R.id.prayer_focus_wh);
        final TextView rFeaturedQuoteWh = view.findViewById(R.id.featured_quote_wh);

        // Retrieve layout objects (philippinne history)
        final TextView rHeadingPhil = view.findViewById(R.id.title_phil);
        final TextView rFeaturedVersePhil = view.findViewById(R.id.featured_verse_phil);
        final TextView rContentPhil = view.findViewById(R.id.content_phil);
        final TextView rPrayerFocusPhil = view.findViewById(R.id.prayer_focus_phil);
        final TextView rFeaturedQuotePhil = view.findViewById(R.id.featured_quote_phil);

        final TextView errorMessage = view.findViewById(R.id.error_message);
        errorMessage.setVisibility(GONE);

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


    public String getTodayAsString() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c);
    }

}