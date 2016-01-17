package memgame.hemant.com.memorygame.businesslayer;

/**
 *
 * Interface to pass Notification event to associated presenter.
 * When any query (Network or DB) is complete.
 *
 * Created by Hemant on 10/1/15.
 */
public interface INotificationListener extends INotifyObject{
	
	void OnNotificationEvent(int notification, INotifyObject notificationData);
}
