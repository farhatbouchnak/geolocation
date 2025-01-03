package com.gps.geolocation.utils;

import com.gps.geolocation.model.GpsLocation;
import org.geotools.referencing.GeodeticCalculator;

import java.awt.geom.Point2D;

public class GeoUtils {

    public static double calculateDistanceBetween(final GpsLocation gpsSource, final GpsLocation gpsDestination) {
        final GeodeticCalculator calc = new GeodeticCalculator();
        final Point2D source = new Point2D.Double(gpsSource.getLatitude(), gpsSource.getLongitude());
        final Point2D destination = new Point2D.Double(gpsDestination.getLatitude(), gpsDestination.getLongitude());
        calc.setStartingGeographicPoint(source);
        calc.setDestinationGeographicPoint(destination);
        return calc.getOrthodromicDistance()/1000;
    }
}
