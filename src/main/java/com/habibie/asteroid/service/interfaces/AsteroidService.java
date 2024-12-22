package com.habibie.asteroid.service.interfaces;

import com.habibie.asteroid.model.Neo;
import com.habibie.asteroid.model.NeoFeed;

import java.util.HashMap;
import java.util.List;

public interface AsteroidService {
    List<Neo> getAllNeo() throws Exception;
    HashMap<String, List<Neo>> getCurrentNeo(String startDate, String endDate, Double kilometers) throws Exception;
    Neo getDetailNeo(String spkID) throws Exception;
}
