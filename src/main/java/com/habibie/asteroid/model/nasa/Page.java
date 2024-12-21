package com.habibie.asteroid.model.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Page {
    private int size;
    @JsonProperty("total_elements")
    private int totalElements;
    @JsonProperty("total_pages")
    private int totalPages;
    private int number;
}
