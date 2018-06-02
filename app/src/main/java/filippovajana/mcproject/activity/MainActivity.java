package filippovajana.mcproject.activity;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import filippovajana.mcproject.R;
import filippovajana.mcproject.helper.FragmentHelper;


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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item ->
                {
                    switch (item.getItemId())
                    {
                        case R.id.action_profile:
                            FragmentHelper.loadFragment(getSupportFragmentManager(),
                                    FragmentHelper.fragmentDictionary.get("Profile"),
                                    FragmentHelper.fragmentDictionary.get("Profile").getClass().getSimpleName(),
                                    findViewById(R.id.fragment_container));
                            break;
                        case R.id.action_status_update:
                            FragmentHelper.loadFragment(getSupportFragmentManager(),
                                    FragmentHelper.fragmentDictionary.get("Status"),
                                    FragmentHelper.fragmentDictionary.get("Status").getClass().getSimpleName(),
                                    findViewById(R.id.fragment_container));
                            break;
                        case R.id.action_add_friend:
                            break;
                        case R.id.action_friends_list:
                            FragmentHelper.loadFragment(getSupportFragmentManager(),
                                    FragmentHelper.fragmentDictionary.get("Map"),
                                    FragmentHelper.fragmentDictionary.get("Map").getClass().getSimpleName(),
                                    findViewById(R.id.fragment_container));

                    }
                    return true;
                });
    }
}
