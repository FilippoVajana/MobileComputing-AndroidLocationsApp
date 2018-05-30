package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.R;


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
        _map = googleMap;
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Getting Google Map reference");

        _map.setMinZoomPreference(12);
        LatLng ny = new LatLng(45.464161, 9.190336);
        _map.moveCamera(CameraUpdateFactory.newLatLng(ny));

    }
}
