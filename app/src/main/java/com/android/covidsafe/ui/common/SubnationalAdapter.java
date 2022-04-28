package com.android.covidsafe.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.covidsafe.vo.Subnational;

import java.util.ArrayList;
import java.util.List;

public class SubnationalAdapter extends ArrayAdapter<Subnational> {

    private List<Subnational> subnationalList;

    public SubnationalAdapter(@NonNull Context context, int resource, @NonNull List<Subnational> objects) {
        super(context, resource, objects);
        subnationalList = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Subnational subnational = getItem(position);
        textView.setText(subnational.getName());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Subnational> suggestList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    suggestList.addAll(subnationalList);
                } else {
                    String filter = charSequence.toString().toLowerCase().trim();
                    for (Subnational subnational : subnationalList) {
                        if (subnational.getName().toLowerCase().contains(filter)) {
                            suggestList.add(subnational);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestList;
                filterResults.count = suggestList.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clear();
                addAll((List) filterResults.values);
                notifyDataSetInvalidated();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Subnational) resultValue).getName();
            }
        };
    }
}