package memgame.hemant.com.memorygame.businesslayer;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import memgame.hemant.com.memorygame.application.MemoryGameApplication;
import memgame.hemant.com.memorygame.models.ErrorResponse;
import memgame.hemant.com.memorygame.network.INetworkRequestHandler;
import memgame.hemant.com.memorygame.presenters.IPresenterFactory;
import memgame.hemant.com.memorygame.presenters.PresenterFactoryLocator;
import memgame.hemant.com.memorygame.models.AllUsersRecord;
import memgame.hemant.com.memorygame.repositories.DatabaseHelper;
import memgame.hemant.com.memorygame.repositories.UserScoreData;
import memgame.hemant.com.memorygame.utils.IAppConstants;

/**
 * Created by Hemant on 10/6/15.
 */
public class KeepRecordService {

    private static final String TAG = KeepRecordService.class.getName();

    private IPresenterNotifier mPresenterNotifier;
    private IPresenterFactory mPresenterFactory;
    private INetworkRequestHandler mNetworkRequestHandler;

    // Reference of DatabaseHelper class to access its DAOs and other components
    private DatabaseHelper databaseHelper = null;

    private static int responseCode = 0;

    public KeepRecordService(INetworkRequestHandler networkRequestHandler) {
        mNetworkRequestHandler = networkRequestHandler;
        mPresenterFactory = PresenterFactoryLocator.GetPresenterFactory();
        mPresenterNotifier = mPresenterFactory.GetPresenterNotifier();
    }


    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(MemoryGameApplication.getMemoryGameInstance(), DatabaseHelper.class);
        }
        return databaseHelper;
    }


    /**
     * Operation to save user record into SQLite DB
     * @param name username
     * @param score user score
     * @param overwrite overwrite the existing record or not
     */
    public void saveRecord(String name, int score, boolean overwrite) {
        if(overwrite) {
            writeIntoDB(name, score, overwrite);
        }else {
            List<UserScoreData> resultList = getAllUsersData(true);
            if ((resultList != null) && (resultList.size() > 0)) {
                boolean nameFound = false;
                Iterator iterator = resultList.iterator();
                while(iterator.hasNext()) {
                    UserScoreData userdata = (UserScoreData)iterator.next();
                    if(name.equalsIgnoreCase(userdata.getName())) {
                        nameFound = true;
                        break;
                    }
                }
                if(nameFound) {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setErrorCode(IAppConstants.ERROR_RESPONSE_DUPLICATE_ENTRY);
                    errorResponse.setMessage("Duplicate Entry");
                    mPresenterNotifier.PostNotification(IPresenterNotifier.NOTIFICATION_SAVE_RECORD, errorResponse);
                }else {
                    writeIntoDB(name, score, overwrite);
                }
            } else {
                writeIntoDB(name, score, overwrite);
            }
        }
    }


    /**
     * Add data into new row of DB. If data already exist it checks whether it
     * needs to overwrite or not.
     * @param name user name
     * @param score user score
     * @param overwrite overwrite data or not if duplicate exist.
     */
    private void writeIntoDB(String name, int score, boolean overwrite) {
        UserScoreData userScore = new UserScoreData(name, score);
        try {
            final Dao<UserScoreData, Integer> userScoreData = getHelper().getUserScoreData();
            if(overwrite) {
                QueryBuilder<UserScoreData, Integer> query = userScoreData.queryBuilder();
                query.where().eq("name", name);
                UserScoreData sub = userScoreData.queryForFirst(query.prepare());
                userScoreData.delete(sub);
                userScoreData.create(userScore);
            }else {
                userScoreData.create(userScore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mPresenterNotifier.PostNotification(IPresenterNotifier.NOTIFICATION_SAVE_RECORD, userScore);
    }


    /**
     * Returns all the data from Table in sorted order by score.
     * @param isWriting isQuery purpose to write/update data
     * @return all data list in table
     */
    public List<UserScoreData> getAllUsersData(boolean isWriting) {
        List<UserScoreData> resultList = null;
        try {
            Dao<UserScoreData, Integer> usersScoreData = getHelper().getUserScoreData();
            QueryBuilder<UserScoreData, Integer> builder = usersScoreData.queryBuilder();
            builder.orderBy("score", false);
            resultList = usersScoreData.query(builder.prepare());
            if(!isWriting && (resultList.size() > 0)) {
                AllUsersRecord usersList = new AllUsersRecord();
                usersList.setUserScoreDataList(resultList);
                mPresenterNotifier.PostNotification(IPresenterNotifier.NOTIFICATION_ALL_RECORDS, usersList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
        return resultList;
    }


    /**
     * Returns highest score row from DB.
     */
    public void getHighestScore() {
        try {
            Dao<UserScoreData, Integer> usersScoreData = getHelper().getUserScoreData();
            QueryBuilder<UserScoreData, Integer> builder = usersScoreData.queryBuilder();
            builder.orderBy("score", false);
            UserScoreData result = usersScoreData.queryForFirst(builder.prepare());

            mPresenterNotifier.PostNotification(IPresenterNotifier.NOTIFICATION_HIGHEST_SCORE, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
