package com.gps.geolocation.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "locations")
public class GpsLocation {

  @Id
  private String id;

  private double latitude;

  private double longitude;

}
