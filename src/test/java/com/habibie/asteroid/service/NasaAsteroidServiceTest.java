package com.habibie.asteroid.service;

import com.google.gson.Gson;
import com.habibie.asteroid.exceptions.NasaClientException;
import com.habibie.asteroid.model.*;
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

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NasaAsteroidServiceTest {

    @InjectMocks
    private NasaAsteroidService nasaAsteroidService;

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

        AsteroidCloseApproachData[] asteroidCloseApproachDataArray = getCloseApproachData();

        neo = new Neo();
        neo.setId("2000433");
        neo.setNeoReferenceId("2000433");
        neo.setName("433 Eros (A898 PA)");
        neo.setNameLimited("Eros");
        neo.setDesignation("433");
        neo.setPotentiallyHazardousAsteroid(false);
        neo.setAbsoluteMagnitudeH(10.41);
        neo.setCloseApproachData(asteroidCloseApproachDataArray);

        neo1 = new Neo();
        neo1.setId("2000719");
        neo1.setNeoReferenceId("2000719");
        neo1.setName("719 Albert (A911 TB)");
        neo1.setNameLimited("Albert");
        neo1.setDesignation("719");
        neo1.setPotentiallyHazardousAsteroid(false);
        neo1.setAbsoluteMagnitudeH(15.59);
        neo1.setCloseApproachData(asteroidCloseApproachDataArray);

        neo2 = new Neo();
        neo2.setId("2001620");
        neo2.setNeoReferenceId("2001620");
        neo2.setName("1620 Geographos (1951 RA)");
        neo2.setNameLimited("Geographos");
        neo2.setDesignation("1620");
        neo2.setPotentiallyHazardousAsteroid(true);
        neo2.setAbsoluteMagnitudeH(15.27);
        neo2.setCloseApproachData(asteroidCloseApproachDataArray);
    }

    private static AsteroidCloseApproachData[] getCloseApproachData() {
        AsteroidCloseApproachData asteroidCloseApproachData = new AsteroidCloseApproachData();
        MissDistance missDistance = new MissDistance();
        missDistance.setAstronomical("0.3902033349");
        missDistance.setLunar("151.7890972761");
        missDistance.setKilometers("58373587.767936663");
        missDistance.setMiles("36271665.4995174294");
        asteroidCloseApproachData.setCloseApproachDate(	"2024-12-22");
        asteroidCloseApproachData.setCloseApproachDate(	"2024-Dec-22 16:29");
        asteroidCloseApproachData.setMissDistance(missDistance);
        return new AsteroidCloseApproachData[]{asteroidCloseApproachData};

    }

    private static AsteroidCloseApproachData createAsteroidData(
            String closeApproachDate, String closeApproachFullDate,
            String kilometers, String astronomical, String lunar, String miles) {

        MissDistance missDistance = new MissDistance();
        missDistance.setKilometers(kilometers);
        missDistance.setAstronomical(astronomical);
        missDistance.setLunar(lunar);
        missDistance.setMiles(miles);

        AsteroidCloseApproachData asteroidData = new AsteroidCloseApproachData();
        asteroidData.setCloseApproachDate(closeApproachDate);
        asteroidData.setCloseApproachDateFull(closeApproachFullDate);
        asteroidData.setMissDistance(missDistance);

        return asteroidData;
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

        List<Neo> actualNeos = nasaAsteroidService.getAllNeo();
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

        assertThrows(NasaClientException.class, () -> nasaAsteroidService.getAllNeo());
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
        neoFeed.setNearEarthObjects(neoFeedMap);
        neoFeed.setElementCount(3);
        Response<NeoFeed> response = Response.success(neoFeed);
        when(neoFeedCall.execute()).thenReturn(response);

        HashMap<String, List<Neo>> listNeos = nasaAsteroidService.getCurrentNeo(startDate, endDate);
        verify(nasaNeoService, times(1)).getCurrentNeo(eq(startDate), eq(endDate),any());
        verify(neoFeedCall, times(1)).execute();
        assertNotNull(listNeos);
        assertEquals(3, listNeos.size());
        List<String> listDates = new ArrayList<>(){
            {
                add(startDate);
                add(endDate);
                add(betweenDate);
            }
        };
        for(String date: listDates) {
            List<Neo> actualNeos =  listNeos.get(date);
            compareNeoData(expectedNeos, actualNeos);
        }
    }

    @Test
    public void shouldBeAbleToFetchDetailNeoData() throws Exception {
        when(nasaNeoService.getDetailNeo(eq("2000433"), any())).thenReturn(neoCall);
        Response<Neo> response = Response.success(neo);
        when(neoCall.execute()).thenReturn(response);

        Neo neoActual = nasaAsteroidService.getDetailNeo("2000433");
        verify(nasaNeoService, times(1)).getDetailNeo(eq("2000433"), any());
        verify(neoCall, times(1)).execute();
        assertNotNull(neoActual);
        Gson gson = new Gson();
        assertEquals(gson.toJson(neo), gson.toJson(neoActual));
    }

    @Test
    public void getCurrentNeoShouldShow10dataSortByTheClosest() throws Exception {

        String startDate = "2024-12-24";
        String endDate = "2024-12-27";
        when(nasaNeoService.getCurrentNeo(eq(startDate), eq(endDate), any())).thenReturn(neoFeedCall);
        URL url = getClass().getClassLoader().getResource("neo_feed_response_example.json");
        assert (url != null);
        String jsonFilePath = url.getPath();
        String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        Gson gson = new Gson();
        NeoFeed neoFeed = gson.fromJson(jsonContent, NeoFeed.class);
        Response<NeoFeed> response = Response.success(neoFeed);
        when(neoFeedCall.execute()).thenReturn(response);

        HashMap<String, List<Neo>> listNeos = nasaAsteroidService.getCurrentNeo(startDate, endDate);
        verify(nasaNeoService, times(1)).getCurrentNeo(eq(startDate), eq(endDate),any());
        verify(neoFeedCall, times(1)).execute();
        assertNotNull(listNeos);
        assertEquals(4, listNeos.size());
        List<String> listDates = new ArrayList<>(){
            {
                add(startDate);
                add(endDate);
                add("2024-12-25");
                add("2024-12-26");
            }
        };
        for(String date: listDates) {
            List<Neo> actualNeos =  listNeos.get(date);
            for(int i = 0; i<actualNeos.size(); i++) {
                if((i+1) < actualNeos.size() ){
                    MissDistance currentMissDistance = actualNeos.get(i).getCloseApproachData()[0].getMissDistance();
                    MissDistance nextCurrentMissDistance = actualNeos.get(i+1).getCloseApproachData()[0].getMissDistance();
                    double currentDistance = Double.parseDouble(currentMissDistance.getKilometers());
                    double nextDistance = Double.parseDouble(nextCurrentMissDistance.getKilometers());
                    assertTrue(currentDistance <= nextDistance);
                }
            }
        }
    }

    private void compareNeoData(List<Neo> expected, List<Neo> actuals){
        expected.forEach(expectedNeo -> {
            assertTrue(actuals.contains(expectedNeo));
        });

    }
}
