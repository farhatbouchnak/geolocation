package com.gps.geolocation.repository;


import com.gps.geolocation.model.GpsLocation;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface GeoLocationRepository extends MongoRepository<GpsLocation, String> {

}
