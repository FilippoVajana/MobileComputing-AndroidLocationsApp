package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import filippovajana.mcproject.R;
import filippovajana.mcproject.rest.RESTService;
import filippovajana.mcproject.rest.UsersListRespose;

public class FriendAddFragment extends Fragment
{
    //view
    View _view;

    //list view
    ListView _resultListView;
    ArrayAdapter<String> _resultListAdapter;

    //search bar
    EditText _searchBar;

    //model
    private ArrayList<String> _resultList = new ArrayList<>();


    public FriendAddFragment()
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
        _view = inflater.inflate(R.layout.fragment_friend_add, container, false);


        //set result listview adapter
        _resultListAdapter = new ArrayAdapter<>(this.getActivity(),
                R.layout.user_list_item, R.id.usernameTextView, _resultList);

        _resultListView = _view.findViewById(R.id.resultListView);
        _resultListView.setAdapter(_resultListAdapter);

        //TODO: on click handler

        //set search bar onTextChanged listener
        _searchBar = _view.findViewById(R.id.searchText);
        _searchBar.addTextChangedListener(_searchBarWatcher);


        //update local model
        setResultListAsync(new String());
        return _view;
    }


    //Users List
    private <E> void updateListContent(List<E> target, List<E> source)
    {
        _view.post(() -> {
            synchronized (_resultList)
            {
                //flush target list
                target.clear();

                //fill with source elements
                if (source != null)
                {
                    target.addAll(source);
                }

                //notify data changed
                _resultListAdapter.notifyDataSetChanged();
            }
        });
    }
    private void setResultListAsync(@NonNull String prefix)
    {
        //update task
        Thread updateTask = new Thread(() ->
        {
            //call rest api
            RESTService rest = new RESTService();
            UsersListRespose respose = rest.getUsers(prefix, Integer.MAX_VALUE);

            if (respose != null)
                Snackbar.make(_view, "Loading Users List", Snackbar.LENGTH_LONG)
                .show();
            else
                Snackbar.make(_view, "Error", Snackbar.LENGTH_INDEFINITE)
                        .show();

            //update local copy
            updateListContent(_resultList, respose.getUsersList());
        });

        //start task
        updateTask.start();
    }

    //Search Bar
    private TextWatcher _searchBarWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }
        @Override
        public void afterTextChanged(Editable editable)
        {
            //wait 1 second

            //get text
            String queryPrefix = editable.toString();

            //update result list
            setResultListAsync(queryPrefix);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }
    };

    //List View
    AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {

        }
    };
}