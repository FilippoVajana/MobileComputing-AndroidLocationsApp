package filippovajana.mcproject.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import filippovajana.mcproject.R;
import filippovajana.mcproject.location.LocationManager;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.UserProfile;

public class ProfileFragment extends Fragment implements OnMapReadyCallback
{
    //fragment view
    View _view;

    //map view
    MapView _mapView;

    //Google Map
    private GoogleMap _map;
    private LocationManager _locationManager;

    //user profile
    UserProfile _profile;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_profile, container, false);

        //get mapView
        _mapView = (MapView) _view.findViewById(R.id.mapView);
        _mapView.onCreate(savedInstanceState);

        //get map
        _mapView.getMapAsync(this);


        //TODO: uncomment
        //add user info
        //Thread task = new Thread(profileTask);
        //task.start();

        return _view;
    }


    //Map Lifecycle
    @Override
    public void onResume()
    {
        super.onResume();
        _mapView.onResume();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        _mapView.onDestroy();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        _mapView.onPause();
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        //init local map reference
        _map = googleMap;

        //init location manager
        _locationManager = new LocationManager(this, googleMap);

        //request last location
        _locationManager.getUserLocation(onSuccessListener, onFailureListener);
    }

    //Location operations handlers
    OnSuccessListener<Location> onSuccessListener = new OnSuccessListener<Location>()
    {
        @Override
        public void onSuccess(Location location)
        {
            //display snackbar
            Snackbar.make(_view, "Location Update Success", Snackbar.LENGTH_LONG)
                    .show();

            //update location in user profile
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

            //move to user location
            _locationManager.moveToLocation(position);
        }
    };
    OnFailureListener onFailureListener = new OnFailureListener()
    {
        @Override
        public void onFailure(@NonNull Exception e)
        {
            //display snackbar
            Snackbar snackbar = Snackbar.make(_view, "Location Update Failure", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    };



    //Profile
    Runnable profileTask = new Runnable()
    {
        @Override
        public void run()
        {
            //get profile information
            _profile = getProfileInformation();

            if (_profile == null) //check if null
                return;

            //set profile information
            _view.post(() -> setProfileInformation());
        }
    };

    private UserProfile getProfileInformation()
    {
        //TODO: uncomment
        /*
        //call model
        UserProfile userProfile = AppDataModel.getInstance().get_userProfile();

        return userProfile;
        */


        return null;
    }

    private void setProfileInformation()
    {
        //TODO: uncomment
        /*
        //username
        TextView usernameText = _view.findViewById(R.id.userNameText);
        usernameText.setText(_profile.get_username());

        //message
        TextView messageText = _view.findViewById(R.id.userMessageText);
        messageText.setText(_profile.get_stateMessage());

        //position
        //TODO: display last position
        */
    }
}

