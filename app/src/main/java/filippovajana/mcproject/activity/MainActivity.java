package filippovajana.mcproject.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import filippovajana.mcproject.R;
import filippovajana.mcproject.helper.FragmentHelper;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.model.AppDataModel;


public class MainActivity extends AppCompatActivity
{
    private SharedPreferences _preferences;

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

        //TODO:ESAME
        _preferences = this.getPreferences(MODE_PRIVATE);
        //set ads permission
        setAdsPermission();

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
                            break;
                        case R.id.action_settings:
                            _fragmentHelper.loadFragment(FragmentHelper.Fragments.SETTINGS);
                    }
                    return true;
                });
    }

    public void onAllowAdsCheckboxClicked(View view)
    {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId())
        {
            case R.id.allowAdsCheckBox:
                if (checked)
                {
                    // save user preference
                    SharedPreferences.Editor editor = _preferences.edit();
                    editor.putBoolean("ALLOW_ADS", true);
                    editor.commit();

                    setAdsPermission();
                }
                else
                {
                    // save user preference
                    SharedPreferences.Editor editor = _preferences.edit();
                    editor.putBoolean("ALLOW_ADS", false);
                    editor.commit();

                    setAdsPermission();
                }


        }
    }

    private void setAdsPermission()
    {
        boolean allow_ads = _preferences.getBoolean("ALLOW_ADS", false);

        AppDataModel.getInstance().adsAllowed = allow_ads;
    }


}
