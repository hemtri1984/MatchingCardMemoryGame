package memgame.hemant.com.memorygame.application;

import android.app.Application;

import java.util.HashMap;

import memgame.hemant.com.memorygame.network.NetworkFactory;
import memgame.hemant.com.memorygame.network.NetworkFactoryLocator;
import memgame.hemant.com.memorygame.presenters.PresenterFactory;
import memgame.hemant.com.memorygame.presenters.PresenterFactoryLocator;

/**
 *
 * Application object class.
 * This object creates only first time when application start.
 * Whenever user sends application into background using HOME button, or application exited
 * using BACK button, its instance is saved by Androd OS Application Stack.
 * This instance is also used to pass the data between difference activities.
 *
 * Created by Hemant on 10/1/15.
 */
public class MemoryGameApplication extends Application implements IMemoryGameApplication {

    private static final String TAG = MemoryGameApplication.class.getName();

    /**
     * HashMap containing only activity presenter objects. This will be
     * useful when we backtrack the activity. IF user presses back button
     * then previous activity presenter should persist.
     */
    private HashMap<String, Object> mPresentersContainer;

    /**
     * Application class object static instance. This instance can be invoked by all the
     * activities of application.
     */
    private static MemoryGameApplication mInstance;


    public static MemoryGameApplication getMemoryGameInstance() { return mInstance; }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        //Initialise presenter data structure
        mPresentersContainer = new HashMap<String, Object>();
        PresenterFactoryLocator.SetPresenterFactory(new PresenterFactory(this));
        NetworkFactoryLocator.SetNetworkFactory(new NetworkFactory(this));
    }

    @Override
    public void registerPresenter(String key, Object obj) {
        mPresentersContainer.put(key, obj);
    }

    @Override
    public void unregisterPresenter(String key) {
        mPresentersContainer.remove(key);
    }

    @Override
    public Object getPresenter(String key) {
        try {
            return mPresentersContainer.get(key);
        }catch (NullPointerException err) {
            return null;
        }
    }

    @Override
    public void exitGame() {
        if(mPresentersContainer != null) {
            mPresentersContainer.clear();
        }
    }
}
