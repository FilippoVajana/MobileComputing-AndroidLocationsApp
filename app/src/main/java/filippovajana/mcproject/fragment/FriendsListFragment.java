package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import filippovajana.mcproject.R;
import filippovajana.mcproject.adapter.AppFriendAdapter;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.location.LocationManager;
import filippovajana.mcproject.location.UserLocationUpdateListener;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.AppFriend;
import filippovajana.mcproject.model.RestaurantProfile;

public class FriendsListFragment extends Fragment implements UserLocationUpdateListener
{
    //view
    View _view;
    AppFriendAdapter _listAdapter;

    //data model
    AppDataModel _dataModel;

    //location
    private LocationManager _location;

    public FriendsListFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //init data model
        _dataModel = AppDataModel.getInstance();

        //init location service
        _location = new LocationManager(this, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        //update view items list
        updateItemsListAsync();

        //set ListView adapter
        setFriendsListAdapter();

        //set on user location update event handler
        _location.setUserLocationUpdateListener(this);
        return _view;
    }





    //TODO: use common interface
    private void updateItemsListAsync()
    {
        //build task
        Thread updateThread = new Thread(() ->
        {
            //update list
            _dataModel.updateFriendsList();
            ArrayList<AppFriend> list = _dataModel.get_friendsList();

            //TODO: check
            _dataModel.updateRestaurantsList();
            ArrayList<RestaurantProfile> restaurantsList = _dataModel.get_restaurantsList();

            //set distance to user
            setDistanceToUser(list);

            //sort list by distance
            sortByDistance(list);


            //notify list adapter
            if (_listAdapter != null)
                _view.post(() -> _listAdapter.notifyDataSetChanged());
        });

        //run task
        updateThread.start();
        try
        {
            updateThread.join();
        }
        catch (Exception e)
        {
            SystemHelper.logError(this.getClass(), "Exception during friends list update");
        }
    }


    //TODO: use common interface
    private void setFriendsListAdapter()
    {
        ArrayList<AppFriend> list = _dataModel.get_friendsList();

        //build list adapter
        _listAdapter = new AppFriendAdapter(this.getActivity(), list);

        //set list adapter
        ListView listView = _view.findViewById(R.id.friendsListView);
        listView.setAdapter(_listAdapter);

        //show list size snackbar
        SystemHelper.showSnackbar(String.format("%d Friends", _dataModel.get_friendsList().size()));
    }


    private void setDistanceToUser(List<AppFriend> list)
    {
        if (list == null)
            return;

        //compute distance
        synchronized (list)
        {
            //compute distances
            for (AppFriend f : list)
            {
                f.setDistanceToUser(_location.getDistanceFromUser(f));
            }
        }
    }

    private void sortByDistance(List<AppFriend> list)
    {
        if (list == null)
            return;

        //sort list by distance
        list.sort((Comparator<AppFriend>) (item1, item2) -> {
            //less
            if (item1.getDistanceToUser() < item2.getDistanceToUser())
                return -1;

            //greater
            if (item1.getDistanceToUser() > item2.getDistanceToUser())
                return 1;

            //equal
            return 0;
        });
    }


    @Override
    public void updateCallback()
    {
        //update
        updateItemsListAsync();
    }
}
