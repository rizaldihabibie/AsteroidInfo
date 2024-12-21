package com.habibie.asteroid.model.nasa;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class NeoFeed {
    private Links links;
    private int elementCount;
    private HashMap<String, List<Neo>> nearObjectOnEarth;
}
