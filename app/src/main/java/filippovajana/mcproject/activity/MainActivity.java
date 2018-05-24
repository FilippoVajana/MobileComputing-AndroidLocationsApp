package filippovajana.mcproject.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import filippovajana.mcproject.R;


public class MainActivity extends AppCompatActivity
{
    private FragmentManager _fragmentManager;
    private FragmentTransaction _fragmentTransaction;

    public MainActivity()
    {
        _fragmentManager = getSupportFragmentManager();
        _fragmentTransaction = _fragmentManager.beginTransaction();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void setupBottomNavigationView()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottomMenu);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profile:

                            case R.id.action_status_update:

                            case R.id.action_add_friend:

                            case R.id.action_friends_list:
                                gotoView(FriendsActivity.class);

                        }
                        return true;
                    }
                });
    }

    private void gotoView(Class destination)
    {
        Intent navIntent = new Intent(this, destination);
        startActivity(navIntent);
    }

    //TODO: fragments load
    private void loadFragment()
    {

    }
}
