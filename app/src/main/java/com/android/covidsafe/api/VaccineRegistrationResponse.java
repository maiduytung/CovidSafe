package com.android.covidsafe.api;

import androidx.annotation.NonNull;

import com.android.covidsafe.vo.VaccineRegistration;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO to hold VaccineRegistration search responses. This is different from the Entity in the database because
 * we are keeping a search result in 1 row and denormalizing list of results into a single column.
 */
public class VaccineRegistrationResponse {
    @SerializedName("total_count")
    private int total;
    @SerializedName("items")
    private List<VaccineRegistration> items;
    private Integer nextPage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<VaccineRegistration> getItems() {
        return items;
    }

    public void setItems(List<VaccineRegistration> items) {
        this.items = items;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    @NonNull
    public List<String> getVaccineRegistrationIds() {
        List<String> vaccineRegistrationIds = new ArrayList<>();
        for (VaccineRegistration vaccineRegistration : items) {
            vaccineRegistrationIds.add(vaccineRegistration.id);
        }
        return vaccineRegistrationIds;
    }
}
