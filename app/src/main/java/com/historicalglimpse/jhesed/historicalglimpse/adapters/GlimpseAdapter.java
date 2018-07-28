package com.historicalglimpse.jhesed.historicalglimpse.adapters;

/**
 * Created by jhesed on 7/28/2018.
 */


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.historicalglimpse.jhesed.historicalglimpse.R;
import com.historicalglimpse.jhesed.historicalglimpse.models.Glimpse;

import java.util.ArrayList;

public class GlimpseAdapter extends ArrayAdapter<Glimpse> {

    /* SECTION: Variable Declarations */

    private Context context;

    public GlimpseAdapter(Context adapterContext, ArrayList<Glimpse> glimpseList) {
        super(adapterContext, R.layout.glimpse_entry, glimpseList);
        context = adapterContext;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        /*
         * Overrides parent for retrieving view
         * */

        View view = convertView;
        final ViewHolder viewHolder;
        Glimpse glimpse = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.glimpse_entry, null);
            viewHolder = new ViewHolder();
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.glimpseDate = glimpse.glimpseDate;

        viewHolder.glimpseDay = (Button) view.findViewById(R.id.glimpse_day);
        viewHolder.glimpseDay.setText(glimpse.glimpseDay);

        viewHolder.headingWorld = (TextView) view.findViewById(R.id.title_world);
        viewHolder.headingWorld.setText(glimpse.headingWorld);

        viewHolder.headingPhil = (TextView) view.findViewById(R.id.title_phil);
        viewHolder.headingPhil.setText(glimpse.headingPhil);

        view.setTag(viewHolder);
        view.setLongClickable(true);

        /* SECTION : Events */

        view.setOnClickListener(new View.OnClickListener() {
        // TODO

            @Override
            public void onClick(View view) {

//                setContentView(R.layout.activity_main);
//                getGlimpse(getTodayAsString());
//
//                // Pull to refresh event
//                final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout)
//                        findViewById(R.id.pull_to_refresh);
//                pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        getGlimpse(getTodayAsString());
//                        pullToRefresh.setRefreshing(false);
//                    }
//                });
//
//                Intent intent = new Intent(context, VerseDetailsActivity.class);
//                intent.putExtra("verseId", viewHolder.verseId);
//                context.startActivity(intent);
            }
        });
        return view;
    }

    private static class ViewHolder {
        /**
         * Private class for recyclable information
         */
        String glimpseDate;

        Button glimpseDay;
        TextView headingWorld;
        TextView headingPhil;

    }
}