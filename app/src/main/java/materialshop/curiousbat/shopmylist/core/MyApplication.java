package materialshop.curiousbat.shopmylist.core;

import android.app.Application;
import android.content.Context;

/**
 * Created by Zied on 01/11/2015.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
