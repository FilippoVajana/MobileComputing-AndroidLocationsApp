package filippovajana.mcproject.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import filippovajana.mcproject.R;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.AppFriend;


public class FriendsActivity extends AppCompatActivity
{

    private List<AppFriend> _friendsList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //set bottom nav handler
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //get followed friends
        getFriendsListAsync();

        //check empty list
        //if (empty) then add snackbar
    }

    private void getFriendsListAsync()
    {
        AppDataModel model = AppDataModel.getInstance();

        //spin new thread
        Runnable task = () ->
        {
            //get list
            ArrayList<AppFriend> list = model.get_friendsList();

            //update local copy
            _friendsList = list;
        };
        new Thread(task).start();
    }
}
