package dim.mobilecomputing.findme.application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Sathindu on 2016-05-23.
 */
public class FindMe extends Application {
    public static Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        bus = new Bus(ThreadEnforcer.MAIN);
    }
}