package filippovajana.mcproject.helper;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import java.util.Dictionary;
import java.util.Hashtable;

import filippovajana.mcproject.fragment.ProfileFragment;

public class FragmentHelper
{
    //app fragments dictionary
    public static Dictionary<String, Fragment> fragmentDictionary = new Hashtable()
    {{
        put("Profile", new ProfileFragment());
    }};


    public static void loadFragment(FragmentManager manager, Fragment fragment, FrameLayout container)
    {
        //ensure current transaction finishes
        manager.executePendingTransactions();

        //check if fragment was already added
        if (manager.findFragmentById(fragment.getId()) == null)
        {
            //create transaction
            FragmentTransaction transaction = manager.beginTransaction();
            //replace current fragment
            transaction.replace(container.getId(), fragment);
            //append to backstack
            transaction.addToBackStack("Profile_Fragment");
            //commit
            transaction.commit();
        }
    }
}
