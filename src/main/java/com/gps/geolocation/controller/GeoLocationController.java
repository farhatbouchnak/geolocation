package com.gps.geolocation.controller;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.gps.geolocation.model.GpsLocation;
import com.gps.geolocation.repository.GeoLocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.geotools.referencing.GeodeticCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class GeoLocationController {


  private final GeoLocationRepository geoLocationRepository;

  public GeoLocationController(GeoLocationRepository geoLocationRepository) {
    this.geoLocationRepository = geoLocationRepository;
  }

  @GetMapping("/api/locations")
  public ResponseEntity<List<GpsLocation>> getAllGpsLocations() {
    try {
      List<GpsLocation> tutorials = geoLocationRepository.findAll();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {

      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/api/locations/{id}", consumes = "application/json", method = RequestMethod.GET)
  public ResponseEntity<GpsLocation> getGpsLocationById(@PathVariable("id") String id) {
    Optional<GpsLocation> gpsLocationData = geoLocationRepository.findById(id);

    return gpsLocationData.map(gpsLocation -> new ResponseEntity<>(gpsLocation, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @RequestMapping(value = "/api/locations/", produces = "application/json", method = RequestMethod.POST)
  public ResponseEntity<GpsLocation> createTutorial(@RequestBody GpsLocation gpsLocation) {
    try {
      GpsLocation _gpsLocation1 = geoLocationRepository.save(new GpsLocation(UUID.randomUUID().toString() ,gpsLocation.getLatitude(), gpsLocation.getLongitude()));
      return new ResponseEntity<>(_gpsLocation1, HttpStatus.CREATED);
    } catch (Exception e) {
      log.warn("An exception occurred while saving object", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") String id) {
    try {
      geoLocationRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @RequestMapping(value = "/api/locations/{idSource}/{idDest}", consumes = "application/json", method = RequestMethod.GET)
  public ResponseEntity<Double> getDistanceBetweenTwoPos(@PathVariable("idSource") String idSource, @PathVariable("idDest") String idDest) {

    final GeodeticCalculator calc = new GeodeticCalculator();

    Optional<GpsLocation> sourceLocation = geoLocationRepository.findById(idSource);
    Optional<GpsLocation> destLocation = geoLocationRepository.findById(idDest);
    final Point2D source = new Point2D.Double(sourceLocation.get().getLatitude(), sourceLocation.get().getLongitude());
    final Point2D destination = new Point2D.Double(destLocation.get().getLatitude(), destLocation.get().getLongitude());
    calc.setStartingGeographicPoint(source);
    calc.setDestinationGeographicPoint(destination);

    System.out.println("Distance London-NY: " + calc.getOrthodromicDistance()/1000 + " kms");

    return new ResponseEntity<>(calc.getOrthodromicDistance()/1000, HttpStatus.OK);
  }
}
