package com.habibie.asteroid.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.habibie.asteroid.model.Neo;
import com.habibie.asteroid.model.NeoFeed;
import com.habibie.asteroid.service.interfaces.AsteroidService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AsteroidControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AsteroidController asteroidController;

    @Mock
    private AsteroidService asteroidService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(asteroidController).build();
    }

    @Test
    public void shouldBeAbleToFetchAllNeoData() throws Exception {
        URL url = getClass().getClassLoader().getResource("neo_get_all_api_success_response.json");
        URL urlResult = getClass().getClassLoader().getResource("neo_get_all_success_response.json");

        assert (url != null);
        assert (urlResult != null);
        String jsonFilePath = url.getPath();
        String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));


        String jsonFileResultPath = urlResult.getPath();
        String jsonResultContent = new String(Files.readAllBytes(Paths.get(jsonFileResultPath)));
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Neo>>() {}.getType();
        List<Neo> listNeos = gson.fromJson(jsonContent, listType);

        when(asteroidService.getAllNeo()).thenReturn(listNeos);

        ResultActions resultActions = mockMvc.perform(get("/asteroids"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        resultActions.andExpect(content().json(jsonResultContent));

        verify(asteroidService, times(1)).getAllNeo();

    }

    @Test
    public void shouldNotBeAbleToFetchAllNeoData() throws Exception {

        when(asteroidService.getAllNeo()).thenReturn(null);

        ResultActions resultActions = mockMvc.perform(get("/asteroids"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        resultActions.andExpect(content().json("""
                {
                    "status": "Success",
                    "message": null,
                    "data": null
                }
                """));

        verify(asteroidService, times(1)).getAllNeo();

    }

    @Test
    public void shouldBeAbleToFetchCurrentClosestNeoData() throws Exception {
        URL url = getClass().getClassLoader().getResource("neo_get_current_closest_api_success_response.json");
        URL urlResult = getClass().getClassLoader().getResource("neo_get_current_closest_api_expected_success_response.json");

        assert (url != null);
        assert (urlResult != null);
        String jsonFilePath = url.getPath();
        String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));


        String jsonFileResultPath = urlResult.getPath();
        String jsonResultContent = new String(Files.readAllBytes(Paths.get(jsonFileResultPath)));
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, List<Neo>>>() {}.getType();
        HashMap<String, List<Neo>> mapResult = gson.fromJson(jsonContent, listType);

        when(asteroidService.getCurrentNeo(anyString(), anyString())).thenReturn(mapResult);

        ResultActions resultActions = mockMvc.perform(get("/asteroids?startDate=2024-12-24&endDate=2024-12-27"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        resultActions.andExpect(content().json(jsonResultContent));

        verify(asteroidService, times(1)).getCurrentNeo(anyString(), anyString());

    }

    @Test
    public void shouldNotBeAbleToFetchCurrentClosestNeoData() throws Exception {
        URL url = getClass().getClassLoader().getResource("neo_get_current_closest_api_success_response.json");

        assert (url != null);
        String jsonFilePath = url.getPath();
        String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, List<Neo>>>() {}.getType();
        HashMap<String, List<Neo>> mapResult = gson.fromJson(jsonContent, listType);

        when(asteroidService.getCurrentNeo(eq("2024-12-24"), eq("2024-12-27"))).thenReturn(mapResult);

        ResultActions resultActions = mockMvc.perform(get("/asteroids?startDate=2024-11-25&endDate=2024-11-28"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        resultActions.andExpect(content().json("""
                {
                    "status": "Success",
                    "message": null,
                    "data": {}
                }
                """));

        verify(asteroidService, times(1)).getCurrentNeo("2024-11-25", "2024-11-28");

    }

    @Test
    public void shouldBeAbleToFetchDetailNeoData() throws Exception {
        URL url = getClass().getClassLoader().getResource("neo_get_detail_api_success_response.json");
        URL urlResult = getClass().getClassLoader().getResource("neo_get_detail_api_expected_success_response.json");

        assert (url != null);
        assert (urlResult != null);
        String jsonFilePath = url.getPath();
        String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));


        String jsonFileResultPath = urlResult.getPath();
        String jsonResultContent = new String(Files.readAllBytes(Paths.get(jsonFileResultPath)));
        Gson gson = new Gson();
        Neo result = gson.fromJson(jsonContent, Neo.class);

        when(asteroidService.getDetailNeo(anyString())).thenReturn(result);

        ResultActions resultActions = mockMvc.perform(get("/asteroids/3794987"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
        resultActions.andExpect(content().json(jsonResultContent));

        verify(asteroidService, times(1)).getDetailNeo(anyString());

    }

    @Test
    public void shouldNotBeAbleToFetchDetailNeoData() throws Exception {
        URL url = getClass().getClassLoader().getResource("neo_get_detail_api_success_response.json");

        assert (url != null);
        String jsonFilePath = url.getPath();
        String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

        Gson gson = new Gson();
        Neo result = gson.fromJson(jsonContent, Neo.class);

        when(asteroidService.getDetailNeo("3794987")).thenReturn(result);

        ResultActions resultActions = mockMvc.perform(get("/asteroids/3794988"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        resultActions.andExpect(content().json("""
                {
                    "status": "Success",
                    "message": null,
                    "data": null
                }
                """));

        verify(asteroidService, times(1)).getDetailNeo("3794988");

    }
}
