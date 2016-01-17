package memgame.hemant.com.memorygame.models;

import java.util.List;

import memgame.hemant.com.memorygame.businesslayer.INotifyObject;
import memgame.hemant.com.memorygame.repositories.UserScoreData;

/**
 * Model contains list of all users and their scores.
 * For Score board
 *
 * Created by Hemant on 10/6/15.
 */
public class AllUsersRecord implements INotifyObject {

    private List<UserScoreData> userScoreDataList;

    public List<UserScoreData> getUserScoreDataList() {
        return userScoreDataList;
    }

    public void setUserScoreDataList(List<UserScoreData> userScoreDataList) {
        this.userScoreDataList = userScoreDataList;
    }
}
