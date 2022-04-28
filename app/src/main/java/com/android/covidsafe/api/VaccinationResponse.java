package com.android.covidsafe.api;

import androidx.annotation.NonNull;

import com.android.covidsafe.vo.Vaccination;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO to hold Vaccination search responses. This is different from the Entity in the database because
 * we are keeping a search result in 1 row and denormalizing list of results into a single column.
 */
public class VaccinationResponse {
    @SerializedName("total_count")
    private int total;
    @SerializedName("items")
    private List<Vaccination> items;
    private Integer nextPage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Vaccination> getItems() {
        return items;
    }

    public void setItems(List<Vaccination> items) {
        this.items = items;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    @NonNull
    public List<String> getVaccinationIds() {
        List<String> vaccinationIds = new ArrayList<>();
        for (Vaccination vaccination : items) {
            vaccinationIds.add(vaccination.id);
        }
        return vaccinationIds;
    }
}
