package com.habibie.asteroid.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AsteroidCloseApproachData {

    @SerializedName("close_approach_date")
    private String closeApproachDate;
    @SerializedName("close_approach_date_full")
    private String closeApproachDateFull;
    @SerializedName("epoch_date_close_approach")
    private Long epochDateCloseApproach;
    @SerializedName("miss_distance")
    private MissDistance missDistance;
}
