package filippovajana.mcproject.helper;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemHelper
{
    public static void closeKeyboard(Activity activity, EditText input)
    {
        try
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }catch (Exception e)
        {
            Logger.getLogger("SystemHelper").log(Level.SEVERE, e.getMessage());
        }
    }


    public static void logError(Class source, String message)
    {
        //get logger
        Logger logger = Logger.getLogger(source.getSimpleName());

        //log
        logger.log(Level.SEVERE, message);
    }

    public static void logWarning(Class source, String message)
    {
        //get logger
        Logger logger = Logger.getLogger(source.getSimpleName());

        //log
        logger.log(Level.WARNING, message);
    }
}
