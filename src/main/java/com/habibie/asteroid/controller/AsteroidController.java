package com.habibie.asteroid.controller;

import com.habibie.asteroid.model.ApplicationResponse;
import com.habibie.asteroid.model.Neo;
import com.habibie.asteroid.model.NeoBrowse;
import com.habibie.asteroid.service.interfaces.AsteroidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/asteroids")
public class AsteroidController {

    @Autowired
    private AsteroidService asteroidService;

    @GetMapping
    public ResponseEntity<ApplicationResponse> getAllData(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "distance", required = false) Double kilometers
    ) {
        ApplicationResponse response = new ApplicationResponse();
        try {
            if(startDate != null && endDate != null) {
                HashMap<String, List<Neo>> listNeos= asteroidService.getCurrentNeo(startDate, endDate, kilometers);
                response.setData(listNeos);
            } else {
                List<Neo> listNeos = asteroidService.getAllNeo();
                response.setData(listNeos);
            }
            response.setStatus("Success");
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus("Error");
            response.setMessage("Failed to fetch data. please contact administrator");
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{spkID}")
    public ResponseEntity<ApplicationResponse> getDetailAsteroid(
            @PathVariable("spkID") String spkID
    ) {
        ApplicationResponse response = new ApplicationResponse();
        try {
            Neo neo = asteroidService.getDetailNeo(spkID);
            response.setStatus("Success");
            response.setData(neo);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus("Error");
            response.setMessage("Failed to fetch detail data. please contact administrator");
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
