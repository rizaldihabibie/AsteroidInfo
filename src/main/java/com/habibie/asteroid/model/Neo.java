package com.habibie.asteroid.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Neo {
    private String id;
    @SerializedName("neo_reference_id")
    private String neoReferenceId;
    private String name;
    @SerializedName("name_limited")
    private String nameLimited;
    private String designation;
    @SerializedName("absolute_magnitude_h")
    private Double absoluteMagnitudeH;
    private Links links;
    @SerializedName("is_potentially_hazardous_asteroid")
    private boolean isPotentiallyHazardousAsteroid;
    @SerializedName("estimated_diameter")
    private EstimatedDiameter estimatedDiameter;
    @SerializedName("close_approach_data")
    private AsteroidCloseApproachData[] closeApproachData;
}
