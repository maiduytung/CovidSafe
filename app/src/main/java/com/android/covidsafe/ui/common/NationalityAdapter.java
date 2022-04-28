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

import com.android.covidsafe.vo.Nationality;

import java.util.ArrayList;
import java.util.List;

public class NationalityAdapter extends ArrayAdapter<Nationality> {

    private List<Nationality> nationalityList;

    public NationalityAdapter(@NonNull Context context, int resource, @NonNull List<Nationality> objects) {
        super(context, resource, objects);
        nationalityList = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Nationality nationality = getItem(position);
        textView.setText(nationality.getName());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Nationality> suggestList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    suggestList.addAll(nationalityList);
                } else {
                    String filter = charSequence.toString().toLowerCase().trim();
                    for (Nationality nationality : nationalityList) {
                        if (nationality.getName().toLowerCase().contains(filter)) {
                            suggestList.add(nationality);
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
                return ((Nationality) resultValue).getName();
            }
        };
    }
}