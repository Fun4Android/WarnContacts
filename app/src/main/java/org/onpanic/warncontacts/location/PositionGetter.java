package org.onpanic.warncontacts.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class PositionGetter implements LocationListener {

    private LocationManager locationManager;
    private PositionHandler positionHandler;

    public PositionGetter(Context context) {
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void get(final PositionHandler positionHandler) {
        this.positionHandler = positionHandler;
        /*
        final Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.d(getClass().getName(), "Last locations isn't null.");
            positionHandler.onGet(location);
            return;
        }
        */

        Log.d(getClass().getName(), "Requesting locations updates.");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    public boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(getClass().getName(), "Location changed.");
        if (positionHandler != null) {
            positionHandler.onGet(location);
            positionHandler = null;
        }

        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public interface PositionHandler {
        void onGet(Location location);
    }
}