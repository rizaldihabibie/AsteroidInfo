package com.habibie.asteroid.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class NeoBrowse {
    private Links links;
    private Page page;
    @SerializedName("near_earth_objects")
    private List<Neo> nearEarthObjects;
}
