package com.habibie.asteroid.service;

import com.google.gson.Gson;
import com.habibie.asteroid.exceptions.NasaClientException;
import com.habibie.asteroid.model.nasa.*;
import com.habibie.asteroid.service.interfaces.NasaNeoService;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AsteroidServiceTest {

    @InjectMocks
    private AsteroidService asteroidService;

    @Mock
    private Retrofit retrofit;

    @Mock
    private NasaNeoService nasaNeoService;

    @Mock
    private Call<NeoBrowse> neoBrowseCall;

    @Mock
    private Call<NeoFeed> neoFeedCall;

    @Mock
    private Call<Neo> neoCall;


    private Neo neo;
    private Neo neo1;
    private Neo neo2;

    @BeforeEach
    public void setup() {
        when(retrofit.create(NasaNeoService.class)).thenReturn(nasaNeoService);
        when(nasaNeoService.getAllNeo(any())).thenReturn(neoBrowseCall);

        neo = new Neo();
        neo.setId("2000433");
        neo.setNeoReferenceId("2000433");
        neo.setName("433 Eros (A898 PA)");
        neo.setNameLimited("Eros");
        neo.setDesignation("433");
        neo.setPotentiallyHazardousAsteroid(false);
        neo.setAbsoluteMagnitudeH(10.41);

        neo1 = new Neo();
        neo1.setId("2000719");
        neo1.setNeoReferenceId("2000719");
        neo1.setName("719 Albert (A911 TB)");
        neo1.setNameLimited("Albert");
        neo1.setDesignation("719");
        neo1.setPotentiallyHazardousAsteroid(false);
        neo1.setAbsoluteMagnitudeH(15.59);

        neo2 = new Neo();
        neo2.setId("2001620");
        neo2.setNeoReferenceId("2001620");
        neo2.setName("1620 Geographos (1951 RA)");
        neo2.setNameLimited("Geographos");
        neo2.setDesignation("1620");
        neo2.setPotentiallyHazardousAsteroid(true);
        neo2.setAbsoluteMagnitudeH(15.27);
    }

    @Test
    public void shouldBeAbleToFetchNeoData() throws Exception {
        when(nasaNeoService.getAllNeo(any())).thenReturn(neoBrowseCall);
        NeoBrowse neoBrowse = new NeoBrowse();
        List<Neo> expectedNeos = new ArrayList<>(){
            {
                add(neo);
                add(neo1);
                add(neo2);
            }
        };
        neoBrowse.setNearEarthObjects(expectedNeos);
        Response<NeoBrowse> response = Response.success(neoBrowse);
        when(neoBrowseCall.execute()).thenReturn(response);

        List<Neo> actualNeos = asteroidService.getAllNeo();
        verify(nasaNeoService, times(1)).getAllNeo(any());
        verify(neoBrowseCall, times(1)).execute();
        assertNotNull(actualNeos);
        assertEquals(3, actualNeos.size());
        compareNeoData(expectedNeos, actualNeos);
    }

    @Test
    public void shouldNotBeAbleToFetchNeoWithoutKey() throws Exception {
        NasaErrorResponse.Error error = NasaErrorResponse.Error.builder()
                .code(NasaError.API_KEY_INVALID.name())
                .message("Invalid API key")
                .build();

        NasaErrorResponse nasaErrorResponse = NasaErrorResponse.builder()
                .error(error)
                .build();

        ResponseBody responseBody = ResponseBody.create(MediaType.get("application/json"), new Gson().toJson(nasaErrorResponse));
        Response<NeoBrowse> response = Response.error(HttpStatus.FORBIDDEN.value(),responseBody);
        when(neoBrowseCall.execute()).thenReturn(response);

        assertThrows(NasaClientException.class, () -> asteroidService.getAllNeo());
        verify(nasaNeoService, times(1)).getAllNeo(any());
        verify(neoBrowseCall, times(1)).execute();
    }

    @Test
    public void shouldBeAbleToFetchNeoFeedData() throws Exception {
        String startDate = "2024-12-21";
        String betweenDate = "2024-12-22";
        String endDate = "2024-12-23";
        when(nasaNeoService.getCurrentNeo(eq(startDate), eq(endDate), any())).thenReturn(neoFeedCall);
        NeoFeed neoFeed = new NeoFeed();
        HashMap<String, List<Neo>> neoFeedMap = new HashMap<>();
        List<Neo> expectedNeos = new ArrayList<>(){
            {
                add(neo);
                add(neo1);
                add(neo2);
            }
        };
        neoFeedMap.put(startDate, expectedNeos);
        neoFeedMap.put(betweenDate, expectedNeos);
        neoFeedMap.put(endDate, expectedNeos);
        neoFeed.setNearObjectOnEarth(neoFeedMap);
        neoFeed.setElementCount(3);
        Response<NeoFeed> response = Response.success(neoFeed);
        when(neoFeedCall.execute()).thenReturn(response);

        NeoFeed neoFeedActual = asteroidService.getCurrentNeo(startDate, endDate);
        verify(nasaNeoService, times(1)).getCurrentNeo(eq(startDate), eq(endDate),any());
        verify(neoFeedCall, times(1)).execute();
        assertNotNull(neoFeedActual.getNearObjectOnEarth());
        assertEquals(3, neoFeedActual.getElementCount());
        List<String> listDates = new ArrayList<>(){
            {
                add(startDate);
                add(endDate);
                add(betweenDate);
            }
        };
        for(String date: listDates) {
            List<Neo> actualNeos =  neoFeedActual.getNearObjectOnEarth().get(date);
            compareNeoData(expectedNeos, actualNeos);
        }
    }


    @Test
    public void shouldBeAbleToFetchDetailNeoData() throws Exception {
        when(nasaNeoService.getDetailNeo(eq("2000433"), any())).thenReturn(neoCall);
        Response<Neo> response = Response.success(neo);
        when(neoCall.execute()).thenReturn(response);

        Neo neoActual = asteroidService.getDetailNeo("2000433");
        verify(nasaNeoService, times(1)).getDetailNeo(eq("2000433"), any());
        verify(neoCall, times(1)).execute();
        assertNotNull(neoActual);
        Gson gson = new Gson();
        assertEquals(gson.toJson(neo), gson.toJson(neoActual));
    }

    private void compareNeoData(List<Neo> expected, List<Neo> actuals){
        expected.forEach(expectedNeo -> {
            assertTrue(actuals.contains(expectedNeo));
        });

    }
}
