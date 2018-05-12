package filippovajana.mcproject.activity;

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
        RESTService.LoginCheckTask task = new RESTService().new LoginCheckTask(this);
        task.execute(username, password);
    }

    //Login check result handler
    public void LoginCheckHandler(Boolean result)
    {
        //display result
        TextView resultText = (TextView) findViewById(R.id.loginResult);
        if (result == true)
        {
            resultText.setTextColor(Color.GREEN);
            resultText.setText("Successful");
        }
        else
        {
            resultText.setTextColor(Color.RED);
            resultText.setText("Failed");
        }
    }

}
