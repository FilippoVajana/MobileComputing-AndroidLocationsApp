package filippovajana.mcproject.activity;

import android.support.annotation.IdRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import filippovajana.mcproject.R;
import filippovajana.mcproject.fragment.ProfileFragment;


public class MainActivity extends AppCompatActivity
{
    private FragmentTransaction _fragmentTransaction;

    public MainActivity()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        _fragmentTransaction = fragmentManager.beginTransaction();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init bottom navigation
        setupBottomNavigationView();
    }


    private void setupBottomNavigationView()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottomMenu);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_profile:

                        case R.id.action_status_update:

                        case R.id.action_add_friend:

                        case R.id.action_friends_list:
                            loadFragment(new ProfileFragment());

                    }
                    return true;
                });
    }



    //TODO: safe fragment commit
    private void loadFragment(Fragment fragment)
    {
        //add fragment
        _fragmentTransaction.add(R.id.fragment_container, fragment);
        _fragmentTransaction.commit();
    }
}
