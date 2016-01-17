package memgame.hemant.com.memorygame.presenters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import memgame.hemant.com.memorygame.businesslayer.INotificationListener;
import memgame.hemant.com.memorygame.businesslayer.INotifyObject;
import memgame.hemant.com.memorygame.businesslayer.IPresenterNotifier;
import memgame.hemant.com.memorygame.iactivities.IMainActivity;
import memgame.hemant.com.memorygame.models.AllUsersRecord;
import memgame.hemant.com.memorygame.models.ErrorResponse;
import memgame.hemant.com.memorygame.network.INetworkRequestHandler;
import memgame.hemant.com.memorygame.network.NetworkFactoryLocator;
import memgame.hemant.com.memorygame.repositories.UserScoreData;

/**
 * Created by Hemant on 10/1/15.
 */
public class MainActivityPresenter implements INotificationListener {

    private static final String TAG = MainActivityPresenter.class.getName();

    private IMainActivity iMainActivity;
    private Context mContext;

    /**
     * Notification center interface
     */
    private IPresenterNotifier mPresenterNotifier;
    private IPresenterFactory mPresenterFactory;
    private INetworkRequestHandler mNetworkRequestHandler;


    public MainActivityPresenter(Context context) {
        mContext = context;
        iMainActivity = (IMainActivity)context;

        mPresenterFactory = PresenterFactoryLocator.GetPresenterFactory();
        mPresenterNotifier = mPresenterFactory.GetPresenterNotifier();
        mNetworkRequestHandler = NetworkFactoryLocator.GetNetworkFactory()
                .GetNetworkConnectionHandler();
    }


    /**
     * Remove all the fragments from backstack. Get called when
     * activity kills
     * @param fm
     */
    public void clearFragmentBackstack(FragmentManager fm) {
        int backStackCount = fm.getBackStackEntryCount();
        for(int i = 0; i < backStackCount ; ++i) {
            fm.popBackStack();
        }
    }


    /**
     * Unregister this presenter with its associated activity.
     */
    public void unregisterPresenter() {
        iMainActivity = null;
    }

    /**
     * Call to save the user name as score to DB.
     * @param name user name
     * @param score user score.
     * @param overwrite can data be overwrite or not.
     */
    public void saveRecord(String name, int score, boolean overwrite) {
        mPresenterNotifier.RegisterNotificationListener(IPresenterNotifier.NOTIFICATION_SAVE_RECORD, this);
        mNetworkRequestHandler.saveRecord(name, score, overwrite);
    }


    /**
     * Query to fetch highest score value.
     */
    public void queryHighestScore() {
        mPresenterNotifier.RegisterNotificationListener(IPresenterNotifier.NOTIFICATION_HIGHEST_SCORE, this);
        mNetworkRequestHandler.queryHighestScore();
    }


    /**
     * Query to fetch all the existing database records.
     */
    public void getAllRecords() {
        mPresenterNotifier.RegisterNotificationListener(IPresenterNotifier.NOTIFICATION_ALL_RECORDS, this);
        mNetworkRequestHandler.queryAllRecords();
    }


    @Override
    public void OnNotificationEvent(int notification, INotifyObject notificationData) {

        switch (notification) {
            case IPresenterNotifier.NOTIFICATION_ALL_RECORDS:
                if(iMainActivity != null) {
                    if(notificationData instanceof AllUsersRecord) {
                        AllUsersRecord result = (AllUsersRecord)notificationData;
                        iMainActivity.onSuccessAllData(result);
                    }else {
                        iMainActivity.onSuccessAllData(null);
                    }
                }
                break;

            case IPresenterNotifier.NOTIFICATION_SAVE_RECORD:
                if(iMainActivity != null ) {
                    if(notificationData instanceof UserScoreData) {
                        UserScoreData result = (UserScoreData)notificationData;
                        iMainActivity.onSuccessfulSaveData(result.getName(), result.getScore());
                    }else if(notificationData instanceof ErrorResponse) {
                        ErrorResponse errorResponse = (ErrorResponse)notificationData;
                        iMainActivity.onErrorReport(errorResponse);
                    }
                }
                break;

            case IPresenterNotifier.NOTIFICATION_HIGHEST_SCORE:
                    if(iMainActivity != null) {
                        if(notificationData instanceof UserScoreData) {
                            UserScoreData result = (UserScoreData)notificationData;
                            if(result != null) {
                                iMainActivity.updateHighestScore(result.getScore());
                            }else {
                                iMainActivity.updateHighestScore(0);
                            }
                        }else {
                            iMainActivity.updateHighestScore(0);
                        }
                    }
                break;
        }

    }
}
