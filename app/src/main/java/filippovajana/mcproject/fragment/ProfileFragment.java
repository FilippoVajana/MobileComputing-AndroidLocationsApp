package filippovajana.mcproject.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import filippovajana.mcproject.R;
import filippovajana.mcproject.activity.LoginActivity;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.location.LocationManager;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.UserProfile;
import filippovajana.mcproject.rest.RESTService;

public class ProfileFragment extends Fragment implements OnMapReadyCallback, OnClickListener
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

        //get user info
        new Thread(profileTask).start();

        //init logout button
        Button btn = _view.findViewById(R.id.logoutButton);
        btn.setOnClickListener(this);

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
        _locationManager.getUserLocation(onSuccessListener, null);
    }


    //Location
    OnSuccessListener<Location> onSuccessListener = new OnSuccessListener<Location>()
    {
        @Override
        public void onSuccess(Location location)
        {
            //check null location
            if ( location == null)
            {
                SystemHelper.showSnackbar("Invalid location");
            }

            //update location in user profile
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

            //move to user location
            _locationManager.moveToLocation(position);
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

            //check if null
            if (_profile == null)
            {
                //show error snackbar
                SystemHelper.showSnackbar("Loading failure");

                //init default user
                _profile = new UserProfile();
            }

            //set profile information
            _view.post(() -> setProfileInformation());
        }
    };
    private UserProfile getProfileInformation()
    {
        //call model
        UserProfile userProfile = AppDataModel.getInstance().get_userProfile();

        return userProfile;
    }
    private void setProfileInformation()
    {
        //username
        TextView usernameText = _view.findViewById(R.id.userNameText);
        usernameText.setText(_profile.get_username());

        //message
        TextView messageText = _view.findViewById(R.id.userMessageText);
        messageText.setText(_profile.get_stateMessage());
    }


    //Logout
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.logoutButton:
                logoutAsync();
        }
    }
    private void logoutAsync()
    {
        Thread logoutTask = new Thread(() ->
        {
            RESTService rest = new RESTService();
            boolean result = rest.logoutUserCall();

            if (result == true)
            {
                //navigate to login page
                Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.getActivity().finish();
            }
            else
                SystemHelper.showSnackbar("Logout failure");
        });
        logoutTask.start();
    }
}

