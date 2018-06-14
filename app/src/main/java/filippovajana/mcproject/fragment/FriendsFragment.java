package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import filippovajana.mcproject.R;
import filippovajana.mcproject.adapter.AppFriendAdapter;
import filippovajana.mcproject.helper.FragmentHelper;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.AppFriend;

public class FriendsFragment extends Fragment
{
    //view
    View _view;

    public FriendsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

/*
        //init data model
        _dataModel = AppDataModel.getInstance();

        //update friends list
        updateFriendsListAsync();
*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_friends, container, false);

        //load friends list nested fragment
        FragmentHelper helper = new FragmentHelper(getChildFragmentManager(), _view.findViewById(R.id.fragmentContainer));
        helper.loadFragment(FragmentHelper.Fragments.LIST);



        /*
        //set ListView adapter
        setFriendsListAdapter();
*/

        return _view;
    }




}
