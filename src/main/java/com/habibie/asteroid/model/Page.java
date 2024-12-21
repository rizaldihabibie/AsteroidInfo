package com.habibie.asteroid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Page {
    private int size;
    @SerializedName("total_elements")
    private int totalElements;
    @SerializedName("total_pages")
    private int totalPages;
    private int number;
}
