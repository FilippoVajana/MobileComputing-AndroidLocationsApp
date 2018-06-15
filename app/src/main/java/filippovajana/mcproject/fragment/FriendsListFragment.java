package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;

import filippovajana.mcproject.R;
import filippovajana.mcproject.adapter.AppFriendAdapter;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.location.LocationManager;
import filippovajana.mcproject.location.UserLocationUpdateListener;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.AppFriend;

public class FriendsListFragment extends Fragment implements UserLocationUpdateListener
{
    //view
    View _view;
    AppFriendAdapter _listAdapter;

    //data model
    AppDataModel _dataModel;

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

        //update friends list
        updateFriendsListAsync();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_friends_list, container, false);


        //set ListView adapter
        setFriendsListAdapter();

        return _view;
    }

    private void setFriendsListAdapter()
    {
        ArrayList<AppFriend> list = _dataModel.get_friendsList();

        //build list adapter
        _listAdapter = new AppFriendAdapter(this.getActivity(), list);

        //set list adapter
        ListView listView = _view.findViewById(R.id.friendsListView);
        listView.setAdapter(_listAdapter);

        //init location service
        new LocationManager(this).setUserLocationUpdateListener(this);


        SystemHelper.showSnackbar(String.format("%d Friends", list.size()));
    }

    private void updateFriendsListAsync()
    {
        //build task
        Thread updateThread = new Thread(() ->
        {
            //update list
            _dataModel.updateFriendsList();

            //compute distances
            LocationManager locationManager = new LocationManager(this);

            ArrayList<AppFriend> list = _dataModel.get_friendsList();
            synchronized (list)
            {
                for (AppFriend f : list)
                {
                    f.setDistanceToUser(locationManager.getDistanceFromUser(f));
                }
            }

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

            //notify list adapter
            if (_listAdapter != null)
                _view.post(() -> _listAdapter.notifyDataSetChanged());
        });

        //run task
        updateThread.start();
        try
        {
            updateThread.join();
            if (_dataModel.get_friendsList().size() > 0)
                SystemHelper.showSnackbar(String.format("%d Friends", _dataModel.get_friendsList().size()));
        }catch (Exception e)
        {
            SystemHelper.logError(this.getClass(), "Exception during friends list update");
        }
    }

    @Override
    public void updateCallback()
    {
        //force list update
        updateFriendsListAsync();
    }
}
