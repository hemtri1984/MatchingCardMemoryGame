package memgame.hemant.com.memorygame.network;

import android.content.Context;

/**
 *
 * Provide NetworkConnection Handler instance.
 *
 * Created by Hemant on 10/1/15.
 *
 */
public class NetworkFactory implements INetworkFactory {

	private Context mContext;
	private INetworkRequestHandler mNetworkConnectionHandler;

	public NetworkFactory(Context context) {
		this.mContext = context;
	}

	@Override
	public INetworkRequestHandler GetNetworkConnectionHandler() {
		if (mNetworkConnectionHandler == null) {
			mNetworkConnectionHandler = new NetworkTaskHandler(mContext);
		}
		return mNetworkConnectionHandler;
	}
}
