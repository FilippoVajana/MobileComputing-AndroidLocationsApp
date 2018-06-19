package filippovajana.mcproject.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import filippovajana.mcproject.R;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.model.AppFriend;
import filippovajana.mcproject.model.ListItemInterface;

public class LocationManager
{
    //view
    private Fragment _fragment;
    private GoogleMap _map;

    //location service
    private LocationRequest _locationRequest;
    private FusedLocationProviderClient _locationProvider;

    //user location
    private static Location _userLocation;

    public LocationManager(Fragment fragment, GoogleMap map)
    {
        //init fragment
        _fragment = fragment;

        //init map
        if (map != null)
        {
            _map = map;
            setMapDefaults();
        }

        //init location provider
        Context context = _fragment.getContext();
        _locationProvider = LocationServices.getFusedLocationProviderClient(context);

        //check for permissions
        boolean locationGranted = checkLocationPermission();
        if (locationGranted == false)
            requestLocationPermissions();

        //check location settings
        setLocationRequestDefaults();

        //get user location
        getUserLocation(null, null);
    }


    //Pattern Observer
    private UserLocationUpdateListener _listener = null;
    public void setUserLocationUpdateListener(UserLocationUpdateListener listener)
    {
        this._listener = listener;
        SystemHelper.logWarning(this.getClass(), "UserLocationUpdateListener set");
    }

    private void notifyUserLocationUpdate()
    {
        if (_listener != null)
        {
            _listener.updateCallback();

            SystemHelper.logWarning(this.getClass(), "UserLocationUpdateListener notified");
        }
    }



    //Defaults
    private void setMapDefaults()
    {
        //set defaults
        try
        {
            _map.setMyLocationEnabled(true);
            _map.getUiSettings().setMapToolbarEnabled(false);
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
                    } catch (Exception ex)
                    {
                        SystemHelper.logError(this.getClass(), ex.getMessage());
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
                locationTask.addOnSuccessListener(defaultOnSuccessListener); //default

            //add failure listener
            if (onFailureListener != null)
                locationTask.addOnFailureListener(onFailureListener);
            else
                locationTask.addOnFailureListener(defaultOnFailureListener); //default

            return locationTask;
        }catch (SecurityException s_ex)
        {
            SystemHelper.logError(this.getClass(), "getLastLocation() Exception");
            defaultOnFailureListener.onFailure(s_ex);
            return null;
        }
    }
    public float getDistanceFromUser(ListItemInterface item)
    {
        //get user location
        if (_userLocation == null)
            return Float.NaN;
        double uLat = _userLocation.getLatitude();
        double uLon = _userLocation.getLongitude();

        //get friend location
        double fLat = item.getLatitude();
        double fLon = item.getLongitude();

        float[] distance = new float[1];

        //compute distance
        Location.distanceBetween(uLat, uLon, fLat, fLon, distance);

        return distance[0];
    }


    //Markers
    public void drawUsersMarkers(List<ListItemInterface> itemsList)
    {
        for (ListItemInterface item : itemsList)
        {
            LatLng position = new LatLng(item.getLatitude(), item.getLongitude());
            MarkerOptions opt = new MarkerOptions();
            if (item.isUser())
            {
                opt.position(position)
                   .title(item.getName())
                   .snippet(item.getMessage());
            }
            else
            {
                opt.position(position)
                        .title(item.getName())
                        .snippet(String.format("solo per oggi sconto del %s%%", item.getMessage()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }

            _map.addMarker(opt);
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
    public void setCameraBounds(List<ListItemInterface> itemsList)
    {
        //check for MapView
        if (_map == null)
            return;

        //bounds builder
        LatLngBounds.Builder builder = LatLngBounds.builder();

        //include user position
        if (_userLocation != null)
            builder.include(new LatLng(_userLocation.getLatitude(), _userLocation.getLongitude()));

        //include friends position
        for (ListItemInterface item : itemsList)
        {
            builder.include(new LatLng(item.getLatitude(), item.getLongitude()));
        }

        //build bounds
        LatLngBounds bounds = builder.build();

        //set bounds
        _map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
    }


    //Default Listener
    private OnSuccessListener<Location> defaultOnSuccessListener = new OnSuccessListener<Location>()
    {
        @Override
        public void onSuccess(Location location)
        {
            //update user location
            _userLocation = location;

            //notify
            notifyUserLocationUpdate();
        }
    };
    private OnFailureListener defaultOnFailureListener = new OnFailureListener()
    {
        @Override
        public void onFailure(@NonNull Exception e)
        {
            //display snackbar
            SystemHelper.showSnackbar("Location Service Failure");
        }
    };

}
