package filippovajana.mcproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import filippovajana.mcproject.R;
import filippovajana.mcproject.rest.RESTService;

public class LoginActivity extends AppCompatActivity
{

    //TODO: disable back navigation


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TODO: check for valid session_id
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
        RESTService.LoginTask task = new RESTService().new LoginTask(this); //TODO: wrap AsyncTask into a method in RESTService
        task.execute(username, password);
    }

    //Login check result handler
    public void loginCheckHandler(Boolean result)
    {
        //display result
        TextView resultText = (TextView) findViewById(R.id.loginResult);
        if (result == true)
        {
            resultText.setTextColor(Color.GREEN);
            resultText.setText("Successful");

            //next page
            navigateToMainActivity();
        }
        else
        {
            resultText.setTextColor(Color.RED);
            resultText.setText("Failed");
        }
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
