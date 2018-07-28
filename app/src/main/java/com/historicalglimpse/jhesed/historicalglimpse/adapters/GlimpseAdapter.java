package com.historicalglimpse.jhesed.historicalglimpse.adapters;

/**
 * Created by jhesed on 7/28/2018.
 */


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.historicalglimpse.jhesed.historicalglimpse.R;
import com.historicalglimpse.jhesed.historicalglimpse.models.Glimpse;

import java.util.ArrayList;

public class GlimpseAdapter extends ArrayAdapter<Glimpse> {

    /* SECTION: Variable Declarations */

    private Context context;

    public GlimpseAdapter(Context adapterContext, ArrayList<Glimpse> glimpseList) {
        super(adapterContext, R.layout.activity_glimpse_list, glimpseList);
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_glimpse_list, null);
            viewHolder = new ViewHolder();
        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.glimpseDate = glimpse.glimpseDate;

        viewHolder.glimpseDateLabel = (TextView) view.findViewById(R.id.label_date);
        viewHolder.glimpseDateLabel.setText(glimpse.glimpseDate);

        viewHolder.headingWorld = (TextView) view.findViewById(R.id.title_world);
        viewHolder.headingWorld.setText(glimpse.headingWorld);

        viewHolder.headingPhil = (TextView) view.findViewById(R.id.title_phil);
        viewHolder.headingPhil.setText(glimpse.headingPhil);

        view.setTag(viewHolder);
        view.setLongClickable(true);

        /* SECTION : Events */

//        view.setOnClickListener(new View.OnClickListener() {
        // TODO
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, VerseDetailsActivity.class);
//                intent.putExtra("verseId", viewHolder.verseId);
//                context.startActivity(intent);
//            }
//        });
        return view;
    }

    private static class ViewHolder {
        /**
         * Private class for recyclable information
         */
        String glimpseDate;

        TextView glimpseDateLabel;
        TextView headingWorld;
        TextView headingPhil;

    }
}