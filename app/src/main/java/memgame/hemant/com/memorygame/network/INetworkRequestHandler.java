package memgame.hemant.com.memorygame.network;

/**
 * Network connection handler interface.
 * This provides the connection interface between network request
 * and presenter.
 * 
 * Created by Hemant on 10/1/15.
 *
 */
public interface INetworkRequestHandler {
	
	/**
	 * @return true : online, false : offline
	 */
	boolean IsNetworkAvailable();

	void saveRecord(String name, int score, boolean overwrite);

	void queryHighestScore();

	void queryAllRecords();

}
