package filippovajana.mcproject.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.R;

public class LocationManager
{
    private Fragment _fragment;
    private static FusedLocationProviderClient _locationProvider;


    public LocationManager(Fragment fragment) //TODO: add GoogleMap reference & initial settings
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
    public Task<Location> getUserLocation(@Nullable OnSuccessListener<Location> onSuccessListener, @Nullable OnFailureListener onFailureListener) //TODO: add onSuccess/onFailure handler
    {
        try
        {
            Task<Location> locationTask = _locationProvider.getLastLocation();

            //add success listener
            if (onSuccessListener != null)
                locationTask.addOnSuccessListener(onSuccessListener);
            else
                locationTask.addOnSuccessListener(defaultOnSuccessListener);

            //add failure listener
            if (onFailureListener != null)
                locationTask.addOnFailureListener(onFailureListener);
            else
                locationTask.addOnFailureListener(defaultOnFailureListener);

            return locationTask;
        }catch (SecurityException s_ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "getLastLocation() Exception");
            defaultOnFailureListener.onFailure(s_ex);
            return null;
        }
    }

    //TODO: add default onSuccess/OnFailure
    private OnSuccessListener<Location> defaultOnSuccessListener = new OnSuccessListener<Location>()
    {
        @Override
        public void onSuccess(Location location)
        {
            //display snackbar
            Snackbar snackbar = Snackbar.make(_fragment.getView(), "Success", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    };

    private OnFailureListener defaultOnFailureListener = new OnFailureListener()
    {
        @Override
        public void onFailure(@NonNull Exception e)
        {
            //display snackbar
            Snackbar snackbar = Snackbar.make(_fragment.getView(), "Failure", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    };

}
