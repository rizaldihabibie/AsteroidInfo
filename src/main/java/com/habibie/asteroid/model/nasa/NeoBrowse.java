package com.habibie.asteroid.model.nasa;

import lombok.Data;

import java.util.List;

@Data
public class NeoBrowse {
    private Links links;
    private Page page;
    private List<Neo> nearEarthObjects;
}
