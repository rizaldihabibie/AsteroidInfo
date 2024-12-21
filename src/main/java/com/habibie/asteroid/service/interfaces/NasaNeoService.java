package com.habibie.asteroid.service.interfaces;

import com.habibie.asteroid.model.NeoBrowse;
import com.habibie.asteroid.model.Neo;
import com.habibie.asteroid.model.NeoFeed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NasaNeoService {

    /**
     * Retrieves a list of near-earth objects (asteroids) from the NEO (Near-Earth Object) feed API.
     *
     * <p>This method sends a GET request to the endpoint "/rest/v1/feed" and expects a response containing a list of
     * asteroids. The request includes the following query parameters:</p>
     *
     * <ul>
     *   <li><b>start_date</b>: The start date of the range for which asteroids should be retrieved, formatted as "YYYY-MM-DD".</li>
     *   <li><b>end_date</b>: The end date of the range for which asteroids should be retrieved, formatted as "YYYY-MM-DD".</li>
     *   <li><b>api_key</b>: Your API key for authenticating the request.</li>
     * </ul>
     *
     * @param startDate the start date of the range, formatted as "YYYY-MM-DD"
     * @param endDate the end date of the range, formatted as "YYYY-MM-DD"
     * @param apiKey the API key for authentication
     * @return a {@link Call} object that can be used to execute the request asynchronously or synchronously,
     *         returning a list of {@link NeoBrowse} objects
     */
    @GET("/rest/v1/feed")
    Call<NeoFeed> getCurrentNeo(
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("api_key") String apiKey
    );

    /**
     * Retrieves detailed information about a specific near-earth object (asteroid) using its SPK-ID.
     *
     * <p>This method sends a GET request to the endpoint "/rest/v1/neo/{asteroidSPKID}" and expects a response containing
     * detailed data about the specified asteroid. The request includes the following parameters:</p>
     *
     * <ul>
     *   <li><b>asteroidSPKID</b>: The unique SPK-ID of the asteroid whose details are to be retrieved.</li>
     *   <li><b>api_key</b>: Your API key for authenticating the request.</li>
     * </ul>
     *
     * @param asteroidSPKID the unique SPK-ID of the asteroid
     * @param apiKey the API key for authentication
     * @return a {@link Call} object that can be used to execute the request asynchronously or synchronously,
     *         returning a {@link Neo} object containing the asteroid's details
     */
    @GET("/rest/v1/neo/{asteroidSPKID}")
    Call<Neo> getDetailNeo(
            @Path("asteroidSPKID") String asteroidSPKID,
            @Query("api_key") String apiKey
    );

    /**
     * Retrieves a paginated list of all near-earth objects (NEOs) from the NEO database.
     *
     * <p>This method sends a GET request to the endpoint "/rest/v1/neo/browse" and expects a response containing
     * a paginated list of NEOs. The request includes the following parameter:</p>
     *
     * <ul>
     *   <li><b>api_key</b>: Your API key for authenticating the request.</li>
     * </ul>
     *
     * @param apiKey the API key for authentication
     * @return a {@link Call} object that can be used to execute the request asynchronously or synchronously,
     *         returning a {@link NeoBrowse} object containing the paginated list of NEOs
     */
    @GET("/rest/v1/neo/browse")
    Call<NeoBrowse> getAllNeo(
            @Query("api_key") String apiKey
    );


}
