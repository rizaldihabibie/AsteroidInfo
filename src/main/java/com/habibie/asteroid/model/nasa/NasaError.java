package com.habibie.asteroid.model.nasa;

public enum NasaError {

    UNKNOWN_ERROR("N000", "Unknown Error"),
    API_KEY_INVALID("N001", "Invalid API Key"),
    INVALID_SPK_ID("N002", "Invalid SPK ID");

    private final String code;
    private final String message;


    NasaError(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
