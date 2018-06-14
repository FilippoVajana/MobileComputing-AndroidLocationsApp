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
        if (FragmentHelper.loadedFragment == null)
        {
            //TODO: load friends list
            _fragmentHelper.loadFragment(FragmentHelper.Fragments.PROFILE);
        }
        else
        {
            _fragmentHelper.loadFragment(FragmentHelper.loadedFragment);
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

        FragmentHelper.loadedFragment = FragmentHelper.Fragments.PROFILE; //enforce navigation schema
        SystemHelper.logError(this.getClass(), "onStop()");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //device orientation hack
        //FragmentHelper.loadedFragment = _lastFragment;
        SystemHelper.logError(this.getClass(), "onDestroy()");
    }

    private void setupBottomNavigationView()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);

        bottomNavigationView.setOnNavigationItemSelectedListener(
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
