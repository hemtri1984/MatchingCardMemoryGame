package memgame.hemant.com.memorygame.network;

/**
 * Factory class returns network connection handler class instance.
 *
 * Created by Hemant on 10/1/15.
 *
 */
public interface INetworkFactory {

	INetworkRequestHandler GetNetworkConnectionHandler();
}
