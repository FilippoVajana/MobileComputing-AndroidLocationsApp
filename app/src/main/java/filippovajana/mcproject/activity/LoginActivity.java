package filippovajana.mcproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import filippovajana.mcproject.R;
import filippovajana.mcproject.rest.RESTService;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart()
    {
        super.onStart();


        //disable login button
        Button loginButton = findViewById(R.id.userLoginButton);
        loginButton.setEnabled(false);

        //check stored session token
        boolean isValid = checkStoredSessionToken();
        if (isValid)
            navigateToMainActivity();
        else
            //enable login button
            loginButton.setEnabled(true);
    }

    private boolean checkStoredSessionToken()
    {
        //get stored token
        String token = getStoredSessionToken();

        if (token == null) //not stored | logout done
            return false; //do login procedure
        else //jump login procedure
        {
            //set REST service local token
            RESTService.setSessionToken(token);
            return true;
        }
    }

    private String getStoredSessionToken()
    {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(getString(R.string.store_session_token), null);
    }

    private boolean setStoredSessionToken(String token)
    {
        //update REST service session token
        RESTService.setSessionToken(token);

        //update stored session token
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.store_session_token), token);
        return editor.commit();
    }

    public void userLoginButton_onClick(View view)
    {
        //get username
        EditText usernameText = (EditText) findViewById(R.id.userNameText);
        String username = usernameText.getText().toString();

        //get password
        EditText passwordText = (EditText) findViewById(R.id.userPasswordText);
        String password = passwordText.getText().toString();

        //try login
        tryLoginAsync(username, password);

    }

    //TODO: login task
    private void tryLoginAsync(String username, String password)
    {
        Thread task = new Thread(() -> {
            //rest call
            String token = new RESTService().doLogin(username, password);

            //check result
            if (token != null) //login successful
            {
                //update stored-local session token
                setStoredSessionToken(token);

                //navigate to main activity
                navigateToMainActivity();
            }
            else //login failed
            {
                showSnackbar("Login failed");
            }
        });

        //run task
        task.start(); //wait completion???
    }

    public void showSnackbar(String message)
    {
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .show();
    }
    private void navigateToMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
