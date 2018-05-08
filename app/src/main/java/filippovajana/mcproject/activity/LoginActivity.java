package filippovajana.mcproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    public void userLoginButton_onClick(View view)
    {
        //get username
        EditText usernameText = (EditText) findViewById(R.id.userNameText);
        String username = usernameText.getText().toString();

        //get password
        EditText passwordText = (EditText) findViewById(R.id.userPasswordText);
        String password = passwordText.getText().toString();

        //check credentials
        RESTService restService = new RESTService();
        restService.checkLoginCredentials(username, password);
    }
}
