package filippovajana.mcproject.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import filippovajana.mcproject.R;
import filippovajana.mcproject.rest.FollowUserResponse;
import filippovajana.mcproject.rest.RESTService;
import filippovajana.mcproject.rest.UsersListResponse;

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

        //on click handler
        _resultListView.setOnItemClickListener(onItemClickListener);

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
            UsersListResponse respose = rest.getUsers(prefix, Integer.MAX_VALUE);

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
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l)
        {
            /**TODO:
             * get selection
             * show dialog popup
             * call rest service (check followed + follow)
             * show result
             */
            String selectedEntry = _resultList.get(position);
            showDialog(selectedEntry);




            Snackbar.make(_view, String.format("Selected %s", _resultList.get(position)), Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
    };


    //Follow User
    private void tryFollowUserAsync(String username)
    {
        //call rest service
        RESTService rest = new RESTService();

        Thread followTask = new Thread(() -> {
            try
            {

                FollowUserResponse response = rest.followUser(username);

                Snackbar.make(_view, response.message, Snackbar.LENGTH_LONG)
                        .show();
            }catch (Exception e)
            {
                Snackbar.make(_view, e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                        .show();
            }
        });

        followTask.start();
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

    
    //Dialog
    private void showDialog(String username)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(String.format("Add %s to followed friends?",username ))
                .setTitle("Follow User");

        builder.setPositiveButton("Add", (dialogInterface, i) -> tryFollowUserAsync(username));
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}