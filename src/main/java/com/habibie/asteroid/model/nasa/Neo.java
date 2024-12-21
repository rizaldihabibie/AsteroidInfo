package com.habibie.asteroid.model.nasa;

import lombok.Data;

@Data
public class Neo {
    private String id;
    private String neoReferenceId;
    private String name;
    private String nameLimited;
    private String designation;
    private Double absoluteMagnitudeH;
    private Links links;
    private boolean isPotentiallyHazardousAsteroid;
    private EstimatedDiameter estimatedDiameter;
}
