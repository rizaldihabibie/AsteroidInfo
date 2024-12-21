package com.habibie.asteroid.service;

import com.google.gson.Gson;
import com.habibie.asteroid.exceptions.NasaClientException;
import com.habibie.asteroid.model.*;
import com.habibie.asteroid.service.interfaces.AsteroidService;
import com.habibie.asteroid.service.interfaces.NasaNeoService;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.*;

@Service
public class NasaAsteroidService implements AsteroidService {

    @Value("${nasa.api.key}")
    private String nasaApiKey;

    private final Logger LOGGER = LogManager.getLogger(NasaAsteroidService.class);

    private NasaNeoService nasaNeoService;

    @Autowired
    private Retrofit retrofit;

    @Override
    public List<Neo> getAllNeo() throws Exception {

        nasaNeoService = retrofit.create(NasaNeoService.class);
        Call<NeoBrowse> nasaCaller = nasaNeoService.getAllNeo(nasaApiKey);

        try {
            Response<NeoBrowse> response = nasaCaller.execute();
            if (response.isSuccessful()) {
                NeoBrowse neoBrowse = response.body();
                if (neoBrowse == null || neoBrowse.getNearEarthObjects() == null) {
                    return new ArrayList<>();
                }
                return neoBrowse.getNearEarthObjects();
            } else {
                checkErrorRequest(response);
            }
            return null;
        } catch (NasaClientException e) {
            throw new NasaClientException(e.getNasaError());
        }catch (Exception ex) {
            LOGGER.error("Error occurred when fetching neo data. {}", ex.getMessage());
            throw new Exception(ex);
        }
    }

    @Override
    public HashMap<String, List<Neo>> getCurrentNeo(String startDate, String endDate) throws Exception {

        nasaNeoService = retrofit.create(NasaNeoService.class);
        Call<NeoFeed> nasaCaller = nasaNeoService.getCurrentNeo(startDate, endDate,nasaApiKey);

        try {
            Response<NeoFeed> response = nasaCaller.execute();
            if (response.isSuccessful()) {
                NeoFeed neoFeed = response.body();
                if(neoFeed != null && neoFeed.getNearEarthObjects() != null) {
                    // Sorting based on closest distance from earth
                    for (Map.Entry<String, List<Neo>> entry : neoFeed.getNearEarthObjects().entrySet()) {
                        entry.getValue().sort(
                                Comparator.comparingDouble(a -> Double.parseDouble(
                                        a.getCloseApproachData()[0].getMissDistance().getKilometers()
                                ))
                        );
                    }

                    return neoFeed.getNearEarthObjects();
                } else {
                    LOGGER.warn("empty data from NASA.");
                    return null;
                }
            } else {
                checkErrorRequest(response);
            }
            return null;
        } catch (NasaClientException e) {
            throw new NasaClientException(e.getNasaError());
        }catch (Exception ex) {
            LOGGER.error("Error occurred when fetching current neo data. {}", ex.getMessage());
            throw new Exception(ex);
        }
    }

    @Override
    public Neo getDetailNeo(String spkID) throws Exception {
        nasaNeoService = retrofit.create(NasaNeoService.class);
        Call<Neo> nasaCaller = nasaNeoService.getDetailNeo(spkID,nasaApiKey);

        try {
            Response<Neo> response = nasaCaller.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                checkErrorRequest(response);
            }
            return null;
        } catch (NasaClientException e) {
            throw new NasaClientException(e.getNasaError());
        }catch (Exception ex) {
            LOGGER.error("Error occurred when fetching detail neo data. {}", ex.getMessage());
            throw new Exception(ex);
        }
    }

    private void checkErrorRequest(Response<?> response) throws NasaClientException {
        try (ResponseBody errorBody = response.errorBody()) {
            if (errorBody != null) {
                Gson gson = new Gson();
                NasaErrorResponse nasaErrorResponse = gson.fromJson(errorBody.string(), NasaErrorResponse.class);
                if (nasaErrorResponse.getError().getCode().equals(NasaError.API_KEY_INVALID.name())) {
                    LOGGER.error("Nasa API call return forbidden error. {}", gson.toJson(nasaErrorResponse));
                    throw new NasaClientException(NasaError.API_KEY_INVALID);
                } else {
                    LOGGER.error("Nasa API call return unknown error. {}", gson.toJson(nasaErrorResponse));
                    throw new NasaClientException(NasaError.UNKNOWN_ERROR);
                }
            } else {
                LOGGER.error(
                        "Nasa API call return unknown error. code : {}, response : {}",
                        response.code(),
                        response.body()
                );
                throw new NasaClientException(NasaError.UNKNOWN_ERROR);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
