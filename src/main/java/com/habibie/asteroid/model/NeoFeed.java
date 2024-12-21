package com.habibie.asteroid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class NeoFeed {
    private Links links;
    @SerializedName("element_count")
    private int elementCount;
    @SerializedName("near_earth_objects")
    private HashMap<String, List<Neo>> nearEarthObjects;
}
