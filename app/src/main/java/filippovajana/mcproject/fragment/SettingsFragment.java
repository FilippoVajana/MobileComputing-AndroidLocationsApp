package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import filippovajana.mcproject.R;
import filippovajana.mcproject.model.AppDataModel;


public class SettingsFragment extends Fragment
{
    public SettingsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_fragmente, container, false);

        //check permission
        AppDataModel model = AppDataModel.getInstance();

        return view;
    }
}
