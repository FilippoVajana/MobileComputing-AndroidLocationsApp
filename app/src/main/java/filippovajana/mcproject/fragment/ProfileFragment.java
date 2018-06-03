package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import filippovajana.mcproject.R;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.UserProfile;

public class ProfileFragment extends Fragment
{
    //fragment view
    View _view;

    //user profile
    UserProfile _profile;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_profile, container, false);

        //TODO: uncomment
        //add user info
        //Thread task = new Thread(profileTask);
        //task.start();

        return _view;
    }


    Runnable profileTask = new Runnable()
    {
        @Override
        public void run()
        {
            //get profile information
            _profile = getProfileInformation();

            if (_profile == null) //check if null
                return;

            //set profile information
            _view.post(() -> setProfileInformation());
        }
    };

    private UserProfile getProfileInformation()
    {
        //TODO: uncomment
        /*
        //call model
        UserProfile userProfile = AppDataModel.getInstance().get_userProfile();

        return userProfile;
        */


        return null;
    }

    private void setProfileInformation()
    {
        //TODO: uncomment
        /*
        //username
        TextView usernameText = _view.findViewById(R.id.userNameText);
        usernameText.setText(_profile.get_username());

        //message
        TextView messageText = _view.findViewById(R.id.userMessageText);
        messageText.setText(_profile.get_stateMessage());

        //position
        //TODO: display last position
        */
    }
}

