package memgame.hemant.com.memorygame.presenters;


import memgame.hemant.com.memorygame.businesslayer.IPresenterNotifier;

/**
 * Created by Hemant on 10/1/15.
 */
public interface IPresenterFactory {

	/**
	 * @return Attached Notification center instance.
	 */
	IPresenterNotifier GetPresenterNotifier();
}
