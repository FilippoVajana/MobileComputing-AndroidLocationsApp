package filippovajana.mcproject.activity;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import filippovajana.mcproject.R;
import filippovajana.mcproject.helper.FragmentHelper;
import filippovajana.mcproject.helper.SystemHelper;


public class MainActivity extends AppCompatActivity
{
    public MainActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemHelper.logError(this.getClass(), "onCreate()");

        //init system helper
        SystemHelper.setMainView(findViewById(R.id.mainView));

        //init bottom navigation
        setupBottomNavigationView();
    }

    private FragmentHelper _fragmentHelper;
    @Override
    protected void onStart()
    {
        super.onStart();
        SystemHelper.logError(this.getClass(), "onStart()");

        //init fragment helper
        _fragmentHelper = new FragmentHelper(this.getSupportFragmentManager(), findViewById(R.id.fragment_container));

        //reload last fragment
        if (_lastFragment == null)
        {
            _fragmentHelper.loadFragment(FragmentHelper.Fragments.FRIENDS);
        }
        else
        {
            _fragmentHelper.loadFragment(_lastFragment);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        SystemHelper.logError(this.getClass(), "onPause()");
    }

    private static FragmentHelper.Fragments _lastFragment;
    @Override
    protected void onStop()
    {
        super.onStop();

        //reset loaded fragment
        _lastFragment = FragmentHelper.loadedFragment;

        FragmentHelper.loadedFragment = FragmentHelper.Fragments.FRIENDS; //force navigation entry point
        SystemHelper.logError(this.getClass(), "onStop()");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SystemHelper.logError(this.getClass(), "onDestroy()");
    }

    private void setupBottomNavigationView()
    {
        BottomNavigationView bottomNav = findViewById(R.id.bottomMenu);

        bottomNav.setOnNavigationItemSelectedListener(
                item ->
                {
                    switch (item.getItemId())
                    {
                        case R.id.action_profile:
                            _fragmentHelper.loadFragment(FragmentHelper.Fragments.PROFILE);
                            break;
                        case R.id.action_status_update:
                            _fragmentHelper.loadFragment(FragmentHelper.Fragments.STATUS);
                            break;
                        case R.id.action_add_friend:
                            _fragmentHelper.loadFragment(FragmentHelper.Fragments.ADD);
                            break;
                        case R.id.action_friends_list:
                            _fragmentHelper.loadFragment(FragmentHelper.Fragments.FRIENDS);
                    }
                    return true;
                });
    }
}
