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

import com.android.covidsafe.vo.Priority;

import java.util.ArrayList;
import java.util.List;

public class PriorityAdapter extends ArrayAdapter<Priority> {

    private List<Priority> priorityList;

    public PriorityAdapter(@NonNull Context context, int resource, @NonNull List<Priority> objects) {
        super(context, resource, objects);
        priorityList = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Priority priority = getItem(position);
        textView.setText(priority.name);

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Priority> suggestList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    suggestList.addAll(priorityList);
                } else {
                    String filter = charSequence.toString().toLowerCase().trim();
                    for (Priority priority : priorityList) {
                        if (priority.name.toLowerCase().contains(filter)) {
                            suggestList.add(priority);
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
                return ((Priority) resultValue).name;
            }
        };
    }
}