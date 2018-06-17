package filippovajana.mcproject.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import filippovajana.mcproject.R;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.location.LocationManager;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.AppFriend;

public class FriendsMapFragment extends Fragment implements OnMapReadyCallback
{
    //view
    private View _view;

    //map view
    MapView _mapView;

    //Google Map
    private GoogleMap _map;
    private LocationManager _locationManager;


    public FriendsMapFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_friends_map, container, false);

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

        //update user location, compute camera bounds and draw markers
        _locationManager.getUserLocation(_onPositionUpdateSuccessListener, _onPositionUpdateFailureListener);
    }

    //Default Listener
    /**
     * Sets camera bounds (friends + user) and draws friends map markers
     */
    private OnSuccessListener<Location> _onPositionUpdateSuccessListener = new OnSuccessListener<Location>()
    {
        @Override
        public void onSuccess(Location location)
        {
            if (location == null)
                _onPositionUpdateFailureListener.onFailure(new NullPointerException());

            //get friends list
            ArrayList<AppFriend> list = AppDataModel.getInstance().get_friendsList();

            //build a fake friend representing the user position
            AppFriend fakeFriend = new AppFriend();
            fakeFriend.setLatitude((float)location.getLatitude());
            fakeFriend.setLongitude((float)location.getLongitude());

            //set camera bounds (friends + user)
            ArrayList<AppFriend> boundList = new ArrayList<>(list); //friends
            boundList.add(fakeFriend); //user
            _locationManager.setCameraBounds(boundList);

            //draw users markers (only friends)
            _locationManager.drawUsersMarkers(list);
        }
    };

    /**
     * Sets camera bounds (only friends) and draws friends map markers.
     * Shows an error snackbar
     */
    private OnFailureListener _onPositionUpdateFailureListener = new OnFailureListener()
    {
        @Override
        public void onFailure(@NonNull Exception e)
        {
            //get friends list
            ArrayList<AppFriend> list = AppDataModel.getInstance().get_friendsList();

            //set camera bounds (only friends)
            ArrayList<AppFriend> boundList = new ArrayList<>(list); //friends
            _locationManager.setCameraBounds(boundList);

            //draw users markers (only friends)
            _locationManager.drawUsersMarkers(list);



            //display snackbar
            SystemHelper.showSnackbar("Location Service Failure");
        }
    };
}
