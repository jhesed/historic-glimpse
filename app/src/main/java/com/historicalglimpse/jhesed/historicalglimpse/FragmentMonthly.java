package com.historicalglimpse.jhesed.historicalglimpse;

/**
 * Created by jhesed on 8/4/2018.
 */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.historicalglimpse.jhesed.historicalglimpse.adapters.GlimpseAdapter;
import com.historicalglimpse.jhesed.historicalglimpse.models.Glimpse;
import com.historicalglimpse.jhesed.historicalglimpse.pojo.DatumList;
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

public class FragmentMonthly extends Fragment {

    APIInterface apiInterface;
    Context context;
    private ListView glimpseListView;
    private GlimpseAdapter glimpseAdapter;

    public static FragmentMonthly newInstance() {
        FragmentMonthly fragment = new FragmentMonthly();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Make this common to fragments
        this.context = this.getContext();
        apiInterface = APIClient.getClient(context).create(APIInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monthly, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        final TextView errorMessage = view.findViewById(R.id.error_message);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);

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
                for (int i = 0; i < data.size(); i++) {

                    glimpseListData.add(new Glimpse(
                            getDayAsString(data.get(i).getGlimpseDate()),
                            data.get(i).getGlimpseDate(),
                            data.get(i).getHeadingWorld(),
                            data.get(i).getHeadingPhil()));
                }

                if (glimpseAdapter == null) {
                    glimpseListView = view.findViewById(R.id.glimpseList);

                    glimpseAdapter = new GlimpseAdapter(context, glimpseListData);
                    glimpseListView.setAdapter(glimpseAdapter);
                }
                glimpseListView.setAdapter(glimpseAdapter);

                // Hide error message and progress bar
                errorMessage.setVisibility(GONE);
                progressBar.setVisibility(GONE);
            }

            @Override
            public void onFailure(Call<GlimpseListResource> call, Throwable t) {
                errorMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                call.cancel();
            }
        });
    }


    public String getMonthAsString() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        formattedDate = formattedDate.substring(0, formattedDate.lastIndexOf("-"));
        return formattedDate;
    }

    public String getDayAsString(String date) {
        return date.substring(date.lastIndexOf("-") + 1, date.length());
    }
}
