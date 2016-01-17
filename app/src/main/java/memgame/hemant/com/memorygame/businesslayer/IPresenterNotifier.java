package memgame.hemant.com.memorygame.businesslayer;
/**
 * Notification Center that registers notification listener with a activity
 * presenter class, so that the result should transfer to correct object.
 *
 * Created by Hemant on 10/1/15.
 *
 */
public interface IPresenterNotifier extends INotifyObject{

	/**
	 * These are notification ids, indicates the type of notification result.
	 */
	public static final int NOTIFICATION_SUCCESS_RESULT = 0;
	public static final int NOTIFICATION_ERROR_RESULT = -1;
	
	/**
	 * Requests id's
	 */
	public static final int NOTIFICATION_SAVE_RECORD = 1;
	public static final int NOTIFICATION_ALL_RECORDS = 2;
	public static final int NOTIFICATION_HIGHEST_SCORE = 3;

	/**
	 * registers the notification listener with a presenter.
	 * @param notification notification type id.
	 * @param notificationListener registered notification listener instance.
	 */
	void RegisterNotificationListener(int notification, INotificationListener notificationListener);

	/**
	 * post the notification object to the registered presenter.
	 * @param notification Notification type
	 * @param anyWdObject generic notification result object.
	 */
	void PostNotification(int notification, INotifyObject anyWdObject);

	/**
	 * unb-registers the notification listener with a presenter.
	 * @param notification notification type id.
	 * @param notificationListener registered notification listener instance.
	 */
	void UnRegisterNotificationListener(int notification, INotificationListener notificationListener);
}
