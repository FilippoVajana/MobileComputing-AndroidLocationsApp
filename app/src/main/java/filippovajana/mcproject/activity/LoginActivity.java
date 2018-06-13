package filippovajana.mcproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
        tryLoginAsync();

    }

    //TODO: login task
    private void tryLoginAsync()
    {

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
