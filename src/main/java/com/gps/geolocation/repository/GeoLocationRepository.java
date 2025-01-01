package com.gps.geolocation.repository;

import java.util.Optional;

import com.gps.geolocation.model.GpsLocation;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface GeoLocationRepository extends MongoRepository<GpsLocation, String> {

  //long distanceBetween(final GpsLocation first, final GpsLocation second);
}
