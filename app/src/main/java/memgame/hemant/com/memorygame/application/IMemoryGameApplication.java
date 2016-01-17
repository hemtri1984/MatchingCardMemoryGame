package memgame.hemant.com.memorygame.application;

/**
 * Created by Hemant on 10/1/15.
 */
public interface IMemoryGameApplication {

    /**
     * This method assign the activity presenter in the application object
     * hash map. That can be retrieved easily when needed.
     */
    void registerPresenter(String key, Object obj);

    void unregisterPresenter(String key);

    Object getPresenter(String key);

    /**
     * Method complete exit formalities when user comes our from application.
     */
    void exitGame();
}
