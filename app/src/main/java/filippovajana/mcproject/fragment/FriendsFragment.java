package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import filippovajana.mcproject.R;
import filippovajana.mcproject.helper.FragmentHelper;

public class FriendsFragment extends Fragment
{
    //view
    private View _view;
    private FloatingActionButton _floatingButton;

    public FriendsFragment()
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
        _view = inflater.inflate(R.layout.fragment_friends, container, false);

        //floating button onclick listener
        _floatingButton = _view.findViewById(R.id.switchViewModeButton);
        _floatingButton.setOnClickListener(_floatingOnClickListener);

        return _view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        //load friends list nested fragment
        FragmentHelper helper = new FragmentHelper(getChildFragmentManager(), _view.findViewById(R.id.fragmentContainer));
        helper.loadFragment(FragmentHelper.Fragments.LIST);
    }


    private View.OnClickListener _floatingOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            //get actual view mode
            Fragment fragment = getChildFragmentManager().getFragments().get(0);

            //change view mode
            FragmentHelper helper = new FragmentHelper(getChildFragmentManager(), _view.findViewById(R.id.fragmentContainer));
            if (fragment instanceof FriendsListFragment)
            {
                //switch button icon
                _floatingButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_black_24dp));

                //load new fragment
                helper.loadFragment(FragmentHelper.Fragments.MAP);
            }
            else
            {
                //switch button icon
                _floatingButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_map_black_24dp));

                //load new fragment
                helper.loadFragment(FragmentHelper.Fragments.LIST);
            }
        }
    };

}
