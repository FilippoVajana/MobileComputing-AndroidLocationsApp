package filippovajana.mcproject.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.R;

public class LocationManager
{
    private Fragment _fragment;
    private static FusedLocationProviderClient _locationProvider;


    public LocationManager(Fragment fragment)
    {
        //init fragment
        _fragment = fragment;
        Logger.getLogger(getClass().getName()).log(Level.INFO, String.format("Location Fragment %s", _fragment.getTag()));

        //init location provider
        _locationProvider = LocationServices.getFusedLocationProviderClient(_fragment.getContext());

        //check for permissions
        boolean locationGranted = checkLocationPermission();
        if (locationGranted == false)
            requestLocationPermissions();
    }

    //Permissions
    private Boolean checkLocationPermission()
    {
        //check permissions
        if (ActivityCompat.checkSelfPermission(_fragment.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Location Permissions DENIED");
            return false;
        }
        else
        {
            return true;
        }
    }
    private void requestLocationPermissions()
    {
        //request permission
        _fragment.requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                _fragment.getResources().getInteger(R.integer.location_permission_request_id));
    }

    //Last Location
    public Task<Location> getUserLocation()
    {
        try
        {
            Task<Location> lastLocation = _locationProvider.getLastLocation();
            return lastLocation;
        }catch (SecurityException s_ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "getLastLocation() Exception");
            return null;
        }
    }
}
