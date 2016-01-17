package memgame.hemant.com.memorygame.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Hemant on 10/6/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "memgame.db";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<UserScoreData, Integer> userScoreDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, UserScoreData.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {


            TableUtils.dropTable(connectionSource, UserScoreData.class, true);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVersion + " to new "
                    + newVersion, e);
        }
    }


    public Dao<UserScoreData, Integer> getUserScoreData() throws SQLException {
        if (userScoreDao == null) {
            userScoreDao = getDao(UserScoreData.class);
        }
        return userScoreDao;
    }
}
