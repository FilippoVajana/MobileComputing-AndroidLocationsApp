package filippovajana.mcproject.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.R;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.location.LocationManager;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.UserProfile;


public class StatusUpdateFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback
{
    //fragment view
    private View _view;

    //fragment map view
    private MapView _mapView;

    //Google Map
    private GoogleMap _map;
    private LocationManager _locationManager;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_status_update, container, false);

        //set send button onClick listener
        Button b = _view.findViewById(R.id.statusSendButton);
        b.setOnClickListener(this);

        //get mapView
        _mapView = (MapView) _view.findViewById(R.id.mapView);
        _mapView.onCreate(savedInstanceState);

        //get map
        _mapView.getMapAsync(this);

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

        //move to last location
        moveToLastLocationAsync();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.statusSendButton:
                updateUserStatus();
        }
    }

    private LatLng _userPosition;
    OnSuccessListener<Location> onSuccessListener = new OnSuccessListener<Location>()
    {
        @Override
        public void onSuccess(Location location)
        {
            //display snackbar
            Snackbar snackbar = Snackbar.make(_view, "Location Update Success", Snackbar.LENGTH_LONG);
            snackbar.show();

            //update location in user profile
            _userPosition = new LatLng(location.getLatitude(), location.getLongitude());

            //move to user location
            _locationManager.moveToLocation(_userPosition);

            //enable send button
            _view.findViewById(R.id.statusSendButton).setEnabled(true);
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

    private void moveToLastLocationAsync()
    {
        //request last location
        _locationManager.getUserLocation(onSuccessListener, onFailureListener);

    }

    private void updateUserStatus()
    {

        //get message text
        TextInputEditText input = (TextInputEditText)_view.findViewById(R.id.statusMessage);
        String stateMessage = input.getText().toString();

        //close keyboard
        SystemHelper.closeKeyboard(getActivity(), input);

        //update profile
        Thread updateTask = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //update profile informations
                UserProfile profile = AppDataModel.getInstance().get_userProfile();

                profile.set_stateMessage(stateMessage);
                profile.set_latitude((float) _userPosition.latitude);
                profile.set_longitude((float) _userPosition.longitude);

                AppDataModel.getInstance().set_userProfile(profile); //also update remote profile

                //show snackbar
                Snackbar.make(_view, "Status Updated", Snackbar.LENGTH_LONG).show();
            }
        });
        updateTask.start();
    }


}
