package filippovajana.mcproject.location;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.R;
import filippovajana.mcproject.activity.MainActivity;
import filippovajana.mcproject.helper.SystemHelper;

public class LocationManager
{
    private Fragment _fragment;
    private GoogleMap _map;

    //location service
    private LocationRequest _locationRequest;
    private static FusedLocationProviderClient _locationProvider;


    public LocationManager(Fragment fragment, GoogleMap map)
    {
        //init fragment
        _fragment = fragment;

        //init map
        _map = map;
        setMapDefaults();

        //init location provider
        _locationProvider = LocationServices.getFusedLocationProviderClient(_fragment.getContext());

        //check for permissions
        boolean locationGranted = checkLocationPermission();
        if (locationGranted == false)
            requestLocationPermissions();

        //check location settings
        setLocationRequestDefaults();
    }

    //Defaults
    private void setMapDefaults()
    {
        //set defaults
        try
        {
            _map.setMyLocationEnabled(true);
            //_map.setMinZoomPreference(15); //minimum zoom level set to "Streets"
        }
        catch (SecurityException s_ex)
        {
            SystemHelper.logError(this.getClass(), String.format("%s - %s", getClass().getSimpleName(), s_ex.getMessage()));
        }
    }

    private void setLocationRequestDefaults()
    {
        //build request specs
        _locationRequest = new LocationRequest();
        _locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //build user settings request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(_locationRequest);

        //check user settings
        SettingsClient client = LocationServices.getSettingsClient(_fragment.getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnFailureListener(_fragment.getActivity(), new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                if (e instanceof ResolvableApiException)
                {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try
                    {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(_fragment.getActivity(),
                                1000);
                    } catch (IntentSender.SendIntentException sendEx)
                    {
                        // Ignore the error.
                    }
                }
            }
        });
    }




    //Permissions
    private Boolean checkLocationPermission()
    {
        //check permissions
        if (ActivityCompat.checkSelfPermission(_fragment.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Location Permissions DENIED");
            SystemHelper.logWarning(this.getClass(), "Location Permissions DENIED");
            return false;
        }
        else
        {
            SystemHelper.logWarning(this.getClass(), "Location Permissions GRANTED");
            return true;
        }
    }
    public void promptLocationSettingsChange()
    {
        setLocationRequestDefaults();
    }



    private void requestLocationPermissions()
    {
        //request permission
        _fragment.requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                _fragment.getResources().getInteger(R.integer.location_permission_request_id));
    }

    //Location
    public Task<Location> getUserLocation(@Nullable OnSuccessListener<Location> onSuccessListener, @Nullable OnFailureListener onFailureListener)
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
            SystemHelper.logError(this.getClass(), "getLastLocation() Exception");
            defaultOnFailureListener.onFailure(s_ex);
            return null;
        }
    }

    //Camera
    public void moveToLocation(LatLng position)
    {
        //get user location
        getUserLocation(location -> {
            _map.moveCamera(CameraUpdateFactory.newLatLng(position)); //set camera position
            _map.animateCamera(CameraUpdateFactory.zoomTo(5), 4000, null); //zoom in
            }, null);
    }

    //Default Listener
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