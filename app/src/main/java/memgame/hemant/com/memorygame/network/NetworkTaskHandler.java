package memgame.hemant.com.memorygame.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import memgame.hemant.com.memorygame.businesslayer.KeepRecordService;

/**
 * Pass all the network requests from Presnter Layer to
 * associated Network layer.
 *
 * Created by Hemant on 10/1/15.
 *
 */
public class NetworkTaskHandler implements INetworkRequestHandler {
	
	private static final String TAG = NetworkTaskHandler.class.getName();

	/**
	 * Application context.
	 */
	private Context mContext;

	public NetworkTaskHandler(Context context) {
		this.mContext = context;
	}

	@Override
	public boolean IsNetworkAvailable() {
		ConnectivityManager connMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}


	//Interface to connect Presenter with business layer

	@Override
	public void saveRecord(String name, int score, boolean overwrite) {
		new KeepRecordService((INetworkRequestHandler)this).saveRecord(name, score, overwrite);
	}

	@Override
	public void queryHighestScore() {
		new KeepRecordService((INetworkRequestHandler)this).getHighestScore();
	}

	@Override
	public void queryAllRecords() {
		new KeepRecordService((INetworkRequestHandler)this).getAllUsersData(false);
	}


}
