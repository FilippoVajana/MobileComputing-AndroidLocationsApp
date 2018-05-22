package filippovajana.mcproject.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.R;
import filippovajana.mcproject.adapter.AppFriendAdapter;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.AppFriend;


public class FriendsActivity extends AppCompatActivity
{
    AppDataModel _dataModel;

    public FriendsActivity()
    {
        //init model reference
        _dataModel = AppDataModel.getInstance();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //update friends list
        updateFriendsListAsync();

        //set ListView adapter
        setFriendsListAdapter();

    }


    private void setFriendsListAdapter()
    {
        ArrayList<AppFriend> list = _dataModel.get_friendsList();

        //build list adapter
        AppFriendAdapter adapter = new AppFriendAdapter(this, list);

        //set list adapter
        ListView listView = (ListView) findViewById(R.id.friendsListView);
        listView.setAdapter(adapter);

        //check for empty list
        showNoFriendsMessage(list.size());
    }

    private void updateFriendsListAsync()
    {
        //spin new thread
        Runnable task = () ->
        {
            //update list
            _dataModel.updateFriendsList();
        };
        Thread updateThread = new Thread(task);
        updateThread.start();
        try
        {
            updateThread.join();
        }catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception during friends list update");
        }
    }


    /**
     * Show a '0 friends' snackbar message
     * @param friendsCount
     */
    private void showNoFriendsMessage(int friendsCount)
    {
        String message = String.format("%d Friends Followed", friendsCount);
        Snackbar.make(findViewById(R.id.myCoordinatorLayout), message, Snackbar.LENGTH_LONG)
                .show();
    }
}
