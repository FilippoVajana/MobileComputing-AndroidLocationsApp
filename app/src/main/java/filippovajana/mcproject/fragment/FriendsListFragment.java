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
import filippovajana.mcproject.adapter.ListItemAdapter;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.location.LocationManager;
import filippovajana.mcproject.location.UserLocationUpdateListener;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.AppFriend;
import filippovajana.mcproject.model.ListItemInterface;
import filippovajana.mcproject.model.RestaurantProfile;

public class FriendsListFragment extends Fragment implements UserLocationUpdateListener
{
    //view
    View _view;
    ListItemAdapter _listAdapter;

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
        setItemsListAdapter();

        //set on user location update event handler
        _location.setUserLocationUpdateListener(this);
        return _view;
    }



    //TODO: ESAME
    private void updateItemsListAsync()
    {
        //build task
        Thread updateThread = new Thread(() ->
        {
            //update friends list
            _dataModel.updateFriendsList();
            ArrayList<AppFriend> friendsList = _dataModel.get_friendsList();

            //update restaurants list
            ArrayList<RestaurantProfile> restaurantsList = new ArrayList<>();
            if (_dataModel.adsAllowed)
            {
                _dataModel.updateRestaurantsList();
                restaurantsList = _dataModel.get_restaurantsList();
            }

            //get merged list
            ArrayList<ListItemInterface> mergedList = _dataModel.mergeLists(friendsList, restaurantsList);

            //set distance to user
            setDistanceToUser(mergedList);

            //sort list by distance
            sortByDistance(mergedList);


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


    //TODO: ESAME
    private void setItemsListAdapter()
    {
        ArrayList<ListItemInterface> list = _dataModel.getMergedList();

        //build list adapter
        _listAdapter = new ListItemAdapter(this.getActivity(), list);

        //set list adapter
        ListView listView = _view.findViewById(R.id.friendsListView);
        listView.setAdapter(_listAdapter);

        //show list size snackbar
        SystemHelper.showSnackbar(String.format("%d Items", _dataModel.get_friendsList().size()));
    }


    private void setDistanceToUser(List<ListItemInterface> list)
    {
        if (list == null)
            return;

        //compute distance
        synchronized (list)
        {
            //compute distances
            for (ListItemInterface item : list)
            {
                item.setDistanceToUser(_location.getDistanceFromUser(item));
            }
        }
    }

    private void sortByDistance(List<ListItemInterface> list)
    {
        if (list == null)
            return;

        //sort list by distance
        list.sort((Comparator<ListItemInterface>) (item1, item2) -> {
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
