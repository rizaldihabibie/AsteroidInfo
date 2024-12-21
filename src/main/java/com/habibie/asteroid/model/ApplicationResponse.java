package com.habibie.asteroid.model;

import lombok.Data;

@Data
public class ApplicationResponse {
    private String status;
    private String message;
    private Object data;
}
