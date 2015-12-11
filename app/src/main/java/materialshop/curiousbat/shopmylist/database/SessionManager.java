package materialshop.curiousbat.shopmylist.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Maintain the session data across the app using SharedPreferences
 * We store a boolean flag isLoggedIn in SharedPreferences to check the login status.
 * Created by Zied on 01/09/2015.
 */
public class SessionManager {

    // Logcat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // SharedPreferences
    SharedPreferences pref;
    Editor editor;
    Context _context;

    // SharedPreferences Mode
    int PRIVATE_MODE = 0;

    // SharedPrefences file name
    private static final String PREF_NAME = "CuriousBatLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Set login and save into SharedPreferences
     * @param isLoggedIn
     */
    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modifed !");
    }

    /**
     * Check if user logged in or not
     * @return true if user is logged in , otherwise false
     */
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
