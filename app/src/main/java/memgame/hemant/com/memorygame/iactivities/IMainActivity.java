package memgame.hemant.com.memorygame.iactivities;

import java.util.List;

import memgame.hemant.com.memorygame.models.AllUsersRecord;
import memgame.hemant.com.memorygame.models.ErrorResponse;
import memgame.hemant.com.memorygame.repositories.UserScoreData;

/**
 * Created by Hemant on 10/1/15.
 */
public interface IMainActivity  {

    /**
     * If data is saved successfully in cache.
     * @param name user name.
     * @param score user score.
     */
    void onSuccessfulSaveData(String name, int score);

    /**
     * If there is some error while adding new data. Error can be the same row already exist.
     * @param errorResponse Error model
     */
    void onErrorReport(ErrorResponse errorResponse);

    /**
     * Retrive list of users along with the scores from DB.
     * @param allUsersRecord list contains all the records.
     */
    void onSuccessAllData(AllUsersRecord allUsersRecord);

    /**
     * Retrieve highest score value.
     * @param highestScore  highest score.
     */
    void updateHighestScore(int highestScore);

}
