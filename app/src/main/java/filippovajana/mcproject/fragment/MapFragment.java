package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import filippovajana.mcproject.R;
import filippovajana.mcproject.location.LocationManager;


public class MapFragment extends Fragment implements OnMapReadyCallback
{
    //fragment view
    View _view;

    //fragment map view
    MapView _mapView;

    //Google Map
    GoogleMap _map;
    LocationManager _locationManager;

    public MapFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_map, container, false);

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

        //init location manager
        _locationManager = new LocationManager(this, googleMap);

        //move to last location
        moveToLastLocationAsync();
    }

    private void moveToLastLocationAsync()
    {
        //request last location
        _locationManager.getUserLocation(null, null);
    }



    //TODO: remove
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Logger.getLogger(getClass().getName()).log(Level.INFO, String.format("Location Permissions %s", grantResults[0]));
    }
    */
}
