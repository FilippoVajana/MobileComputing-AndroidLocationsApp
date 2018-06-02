package filippovajana.mcproject.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

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

    //TODO: call lifecycle methods
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

        //get location service (check for location permissions)
        LocationManager location = new LocationManager(this);

        _map = googleMap;

        //setup map
        try
        {
            _map.setMinZoomPreference(15);
            _map.setMyLocationEnabled(true);
        }
        catch (SecurityException s_ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "onMapReady - Security Exception");
        }
        catch (Exception ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "onMapReady - Exception");
            ex.printStackTrace();
        }

        //move to last location
        moveToLastLocationAsync();
    }

    public void moveToLastLocationAsync()
    {
        //location service
        LocationManager location = new LocationManager(this);

        //request last location
        Task<Location> locationTask = location.getUserLocation(null, null);
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
