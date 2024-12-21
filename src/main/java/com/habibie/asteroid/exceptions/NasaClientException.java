package com.habibie.asteroid.exceptions;

import com.habibie.asteroid.model.nasa.NasaError;
import lombok.Getter;

@Getter
public class NasaClientException extends Exception{

    private final String code;
    private final NasaError nasaError;

    public NasaClientException(NasaError nasaError){
        super(nasaError.getMessage());
        this.code = nasaError.getCode();
        this.nasaError = nasaError;
    }

}
