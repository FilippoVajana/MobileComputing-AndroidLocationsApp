package filippovajana.mcproject.activity;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import filippovajana.mcproject.R;
import filippovajana.mcproject.helper.FragmentHelper;


public class MainActivity extends AppCompatActivity
{
    private FragmentHelper _fragmentHelper;

    public MainActivity()
    {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init bottom navigation
        setupBottomNavigationView();

        //init fragment helper
        _fragmentHelper = FragmentHelper.getInstance(this.getSupportFragmentManager(), findViewById(R.id.fragment_container));

        //TODO: load friends list
        //load profile fragment
        _fragmentHelper.loadFragment(FragmentHelper.Fragments.PROFILE);
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
                            _fragmentHelper.loadFragment(FragmentHelper.Fragments.MAP); //TODO: change to friends list
                    }
                    return true;
                });
    }
}
