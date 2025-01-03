package com.gps.geolocation.controller;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.gps.geolocation.exceptionHandler.ResourceNotFoundException;
import com.gps.geolocation.model.GpsLocation;
import com.gps.geolocation.repository.GeoLocationRepository;
import com.gps.geolocation.utils.GeoUtils;
import lombok.extern.slf4j.Slf4j;
import org.geotools.referencing.GeodeticCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api")
public class GeoLocationController {


  private final GeoLocationRepository geoLocationRepository;

  public GeoLocationController(GeoLocationRepository geoLocationRepository) {
    this.geoLocationRepository = geoLocationRepository;
  }

  @GetMapping("/locations")
  public ResponseEntity<List<GpsLocation>> getAllGpsLocations() {
    try {
      List<GpsLocation> tutorials = geoLocationRepository.findAll();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      log.warn("Exception occurred while retrieving data", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/location/{id}", consumes = "application/json", method = RequestMethod.GET)
  public ResponseEntity<GpsLocation> getGpsLocationById(@PathVariable("id") String id) {
    try {
      return  new ResponseEntity<>(geoLocationRepository.findById(id).get(), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      log.warn("Exception occurred while retrieving data", e);
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/populate", produces = "application/json", method = RequestMethod.POST)
  public ResponseEntity<GpsLocation> createTutorial(@RequestBody GpsLocation gpsLocation) {
    try {
      GpsLocation _gpsLocation1 = geoLocationRepository.save(new GpsLocation(UUID.randomUUID().toString() ,gpsLocation.getLatitude(), gpsLocation.getLongitude()));
      return new ResponseEntity<>(_gpsLocation1, HttpStatus.CREATED);
    } catch (Exception e) {
      log.warn("An exception occurred while saving object", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") String id) {
    try {
      geoLocationRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @RequestMapping(value = "/compare/{idSource}/{idDest}", method = RequestMethod.GET)
  public ResponseEntity<Double> getDistanceBetweenTwoPos(@PathVariable("idSource") String idSource, @PathVariable("idDest") String idDest) {
    try {
      Optional<GpsLocation> sourceLocation = geoLocationRepository.findById(idSource);
      Optional<GpsLocation> destLocation = geoLocationRepository.findById(idDest);
      return new ResponseEntity<>(GeoUtils.calculateDistanceBetween(sourceLocation.get(), destLocation.get()), HttpStatus.OK);
    } catch (Exception e) {
      log.warn("Unexpected exception occurred while handling distance", e);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
