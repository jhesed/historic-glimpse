package com.historicalglimpse.jhesed.historicalglimpse;

/**
 * Created by jhesed on 8/4/2018.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.historicalglimpse.jhesed.historicalglimpse.Common.getTodayAsString;

public class FragmentDaily extends Fragment {

    public static FragmentDaily newInstance() {
        FragmentDaily fragment = new FragmentDaily();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        Common.getGlimpse(getTodayAsString(), view, MainActivity.getAPIInterface());

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

}