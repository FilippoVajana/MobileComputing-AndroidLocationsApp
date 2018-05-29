package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import filippovajana.mcproject.R;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.UserProfile;


public class StatusUpdateFragment extends Fragment implements View.OnClickListener
{
    private View _view;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_status_update, container, false);

        //set send button onClick listener
        Button b = _view.findViewById(R.id.statusSendButton);
        b.setOnClickListener(this);

        return _view;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.statusSendButton:
                updateStatusMessage();
        }
    }


    private void updateStatusMessage()
    {
        //get message text
        TextInputEditText input = (TextInputEditText)_view.findViewById(R.id.statusMessage);
        String stateMessage = input.getText().toString();

        Thread updateTask = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //update model information
                UserProfile profile = AppDataModel.getInstance().get_userProfile();
                profile.set_stateMessage(stateMessage);
                //TODO: update LAT and LON

                AppDataModel.getInstance().set_userProfile(profile);
            }
        });
        updateTask.start();
    }
}
