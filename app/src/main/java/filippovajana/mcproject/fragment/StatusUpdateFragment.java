package filippovajana.mcproject.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

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
    private LatLng _userPosition;


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
        Button b = _view.findViewById(R.id.statusMessageSendButton);
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

        //get last location
        _locationManager.getUserLocation(onSuccessListener, null);
    }

    //Location operations handlers
    OnSuccessListener<Location> onSuccessListener = new OnSuccessListener<Location>()
    {
        @Override
        public void onSuccess(Location location)
        {
            //TODO: add null check
            if (location == null)
                return;

            SystemHelper.showSnackbar("Position updated");

            //update location in user profile
            _userPosition = new LatLng(location.getLatitude(), location.getLongitude());

            //move to user location
            _locationManager.moveToLocation(_userPosition);

            //enable send button
            _view.findViewById(R.id.statusMessageSendButton).setEnabled(true);
        }
    };

    //Status update
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.statusMessageSendButton:
                updateUserStatus();
        }
    }
    private void updateUserStatus()
    {

        //get message text
        TextInputEditText input = (TextInputEditText)_view.findViewById(R.id.statusMessageEditText);
        String stateMessage = input.getText().toString();

        //close keyboard
        SystemHelper.closeKeyboard(getActivity(), input);

        //check null position
        if (_userPosition == null)
        {
            //snackbar
            Snackbar.make(_view, "Invalid Location", Snackbar.LENGTH_LONG)
                    .show();

            //prompt location settings change
            _locationManager.promptLocationSettingsChange();

            //get location
            _locationManager.getUserLocation(onSuccessListener, null);
            return;
        }

        //update profile
        Thread updateTask = new Thread(() -> {

            UserProfile profile = AppDataModel.getInstance().get_userProfile();

            //update profile informations
            profile.set_stateMessage(stateMessage);
            profile.set_latitude((float) _userPosition.latitude);
            profile.set_longitude((float) _userPosition.longitude);

            AppDataModel.getInstance().set_userProfile(profile); //also update remote profile

            //show snackbar
            SystemHelper.showSnackbar("Status updated");

            //clear message text
            EditText messageText = _view.findViewById(R.id.statusMessageEditText);
            _view.post(() -> messageText.setText(""));

        });
        updateTask.start();
    }
}
