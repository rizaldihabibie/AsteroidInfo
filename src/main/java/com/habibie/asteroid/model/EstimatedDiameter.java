package com.habibie.asteroid.model;

import lombok.Data;

@Data
public class EstimatedDiameter {
    private ObjectDiameter kilometers;
    private ObjectDiameter meters;
    private ObjectDiameter miles;
    private ObjectDiameter feet;
}
