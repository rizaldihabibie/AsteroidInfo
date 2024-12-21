package com.habibie.asteroid.model.nasa;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NasaErrorResponse {
    private Error error;

    @Data
    @Builder
    public static class Error {
        private String code;
        private String message;
    }
}
