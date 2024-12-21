package com.habibie.asteroid.service.interfaces;

import com.habibie.asteroid.model.Neo;
import com.habibie.asteroid.model.NeoFeed;

import java.util.List;

public interface AsteroidService {
    List<Neo> getAllNeo() throws Exception;
    NeoFeed getCurrentNeo(String startDate, String endDate) throws Exception;
    Neo getDetailNeo(String spkID) throws Exception;
}
