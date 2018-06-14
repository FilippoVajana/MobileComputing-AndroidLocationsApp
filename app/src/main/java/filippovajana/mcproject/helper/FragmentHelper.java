package filippovajana.mcproject.helper;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import java.util.Hashtable;

import filippovajana.mcproject.fragment.FriendAddFragment;
import filippovajana.mcproject.fragment.FriendsFragment;
import filippovajana.mcproject.fragment.MapFragment;
import filippovajana.mcproject.fragment.ProfileFragment;
import filippovajana.mcproject.fragment.StatusUpdateFragment;

public class FragmentHelper
{
    //app fragments dictionary
    public enum Fragments {PROFILE, STATUS, ADD, FRIENDS}
    private Hashtable<Fragments, Fragment> _fragmentDictionary = new Hashtable()
    {{
        put(Fragments.PROFILE, new ProfileFragment());
        put(Fragments.STATUS, new StatusUpdateFragment());
        put(Fragments.ADD, new FriendAddFragment());
        put(Fragments.FRIENDS, new FriendsFragment());
    }};

    private FragmentManager _manager;
    private FrameLayout _container;

    private static FragmentHelper _instance = null;
    public FragmentHelper(FragmentManager manager, FrameLayout container)
    {
        SystemHelper.logWarning(FragmentHelper.class, "Initialize FragmentHelper");

        //init fragment manager
        _manager = manager;

        //init fragment container
        _container = container;
    }
//
//    public static FragmentHelper getInstance(FragmentManager baseManager, FrameLayout baseContainer)
//    {
//
//        if (_instance == null)
//        {
//            _instance = new FragmentHelper(baseManager, baseContainer);
//        }
//
//        return _instance;
//    }

    public static Fragments loadedFragment = null;
    public void loadFragment(Fragments fragmentTag)
    {
        //build fragment instance
        Fragment fragment = _fragmentDictionary.get(fragmentTag);
        SystemHelper.logWarning(FragmentHelper.class, String.format("Fragment tag: %s", fragment.getTag()));


        //ensure current transaction finishes
        _manager.executePendingTransactions();

        //check if fragment was already added
        if (_manager.findFragmentByTag(fragment.getTag()) == null)
        {
            //build transaction
            FragmentTransaction transaction = _manager.beginTransaction();

            //replace current fragment
            transaction.replace(_container.getId(), fragment, fragmentTag.toString());

            //commit
            transaction.commit();
            SystemHelper.logWarning(FragmentHelper.class, String.format("%s Loaded", fragmentTag));

            //update loaded fragment variable
            loadedFragment = fragmentTag;
        }
        else
        {
            SystemHelper.logError(FragmentHelper.class, String.format("%s Already Loaded", fragmentTag));
        }
    }
}
