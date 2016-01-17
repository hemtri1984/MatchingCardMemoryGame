package memgame.hemant.com.memorygame.presenters;

import android.content.Context;

import memgame.hemant.com.memorygame.businesslayer.IPresenterNotifier;
import memgame.hemant.com.memorygame.businesslayer.PresenterNotifier;

/**
 * Factory class for Presenter, returns the associated presenter object for
 * an activity, also returns the attached Notification center object which is used
 * to send the result to right presenter.
 * 
 * Created by Hemant on 10/1/15.
 *
 */
public class PresenterFactory implements IPresenterFactory {

	private static IPresenterFactory sPresenterFactory;
	private PresenterNotifier mPresenterNotifier;

	public PresenterFactory(Context context) {
	}

	public IPresenterFactory GetPresenterFactory() {
		return sPresenterFactory;
	}

	public static void SetPresenterFactory(IPresenterFactory platform) {
		sPresenterFactory = platform;
	}

	@Override
	public IPresenterNotifier GetPresenterNotifier() {
		if (mPresenterNotifier == null) {
			mPresenterNotifier = new PresenterNotifier();
		}
		return mPresenterNotifier;
	}
}
