package com.android.covidsafe.binding;

import android.util.Base64;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;

import com.android.covidsafe.R;
import com.android.covidsafe.ui.common.EthnicAdapter;
import com.android.covidsafe.ui.common.NationalityAdapter;
import com.android.covidsafe.ui.common.PriorityAdapter;
import com.android.covidsafe.ui.common.SubnationalAdapter;
import com.android.covidsafe.vo.Ethnic;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Priority;
import com.android.covidsafe.vo.Subnational;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {
    final Fragment fragment;

    @Inject
    public FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {
        Glide.with(fragment).load(url).into(imageView);
    }

    @BindingAdapter("base64Image")
    public void bindShapeableImage(ShapeableImageView shapeableImageView, String base64ImageString) {
        if (base64ImageString != null) {
            Glide.with(fragment)
                    .load(Base64.decode(base64ImageString, Base64.DEFAULT))
                    .centerCrop()
                    .into(shapeableImageView);
        } else Glide.with(fragment).load(R.drawable.covid_safe_logo).into(shapeableImageView);
    }

    @BindingAdapter("date")
    public void bindDate(TextInputEditText textInputEditText, Date date) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = format.format(date);
            textInputEditText.setText(formattedDate);
        }
    }

    @BindingAdapter("birthday")
    public void bindBirthday(TextView textView, Date date) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = format.format(date);
            textView.setText(formattedDate);
        }
    }

    @BindingAdapter("priorityAdapter")
    public void bindPriorityAdapter(AutoCompleteTextView autoCompleteTextView, List<Priority> priorityList) {
        if (priorityList != null) {
            PriorityAdapter adapter = new PriorityAdapter(autoCompleteTextView.getContext(), android.R.layout.simple_list_item_1, priorityList);
            autoCompleteTextView.setAdapter(adapter);
        }
    }

    @BindingAdapter("subnationalAdapter")
    public void bindSubnationalAdapter(AutoCompleteTextView autoCompleteTextView, List<Subnational> subnationalList) {
        if (subnationalList != null) {
            subnationalList.sort(Comparator.comparing(Subnational::getName));
            SubnationalAdapter adapter = new SubnationalAdapter(autoCompleteTextView.getContext(), android.R.layout.simple_list_item_1, subnationalList);
            autoCompleteTextView.setAdapter(adapter);
        }
    }

    @BindingAdapter("nationalityAdapter")
    public void bindNationalityAdapter(AutoCompleteTextView autoCompleteTextView, List<Nationality> nationalityList) {
        if (nationalityList != null) {
            nationalityList.sort(Comparator.comparing(Nationality::getName));
            NationalityAdapter adapter = new NationalityAdapter(autoCompleteTextView.getContext(), android.R.layout.simple_list_item_1, nationalityList);
            autoCompleteTextView.setAdapter(adapter);
        }
    }

    @BindingAdapter("ethnicAdapter")
    public void bindEthnicAdapter(AutoCompleteTextView autoCompleteTextView, List<Ethnic> ethnicList) {
        if (ethnicList != null) {
            ethnicList.sort(Comparator.comparing(Ethnic::getName));
            EthnicAdapter adapter = new EthnicAdapter(autoCompleteTextView.getContext(), android.R.layout.simple_list_item_1, ethnicList);
            autoCompleteTextView.setAdapter(adapter);
        }
    }
}
