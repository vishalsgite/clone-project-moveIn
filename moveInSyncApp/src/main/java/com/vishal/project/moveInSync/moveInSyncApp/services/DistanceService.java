package com.vishal.project.moveInSync.moveInSyncApp.services;

import org.locationtech.jts.geom.Point;

public interface DistanceService {

    double calculateDistance(Point src, Point des);


}
