package memgame.hemant.com.memorygame.presenters;

/**
 * Locates the presenter factory.
 * 
 * Created by Hemant on 10/1/15.
 *
 */
public class PresenterFactoryLocator {

	private static IPresenterFactory sPresenterFactory;

	public static IPresenterFactory GetPresenterFactory() {
		return sPresenterFactory;
	}

	public static void SetPresenterFactory(IPresenterFactory sPresenterFac) {
		PresenterFactoryLocator.sPresenterFactory = sPresenterFac;
	}
}
