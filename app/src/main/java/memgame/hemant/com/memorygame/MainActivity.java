package memgame.hemant.com.memorygame;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import memgame.hemant.com.memorygame.application.MemoryGameApplication;
import memgame.hemant.com.memorygame.fragments.GameScoreFragment;
import memgame.hemant.com.memorygame.fragments.GameScreenFragment;
import memgame.hemant.com.memorygame.fragments.NavigationDrawerFragment;
import memgame.hemant.com.memorygame.iactivities.IMainActivity;
import memgame.hemant.com.memorygame.models.AllUsersRecord;
import memgame.hemant.com.memorygame.models.ErrorResponse;
import memgame.hemant.com.memorygame.presenters.MainActivityPresenter;
import memgame.hemant.com.memorygame.repositories.UserScoreData;
import memgame.hemant.com.memorygame.utils.IAppConstants;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, IMainActivity, GameScreenFragment.IGameScreenFragment {

    private static final String TAG = MainActivity.class.getName();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    private MemoryGameApplication mMemoryGameApp;
    private MainActivityPresenter mPresenter;
    private FragmentManager mFragmentManager;



    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Used to store username and score of user for overwrite
     */
    private String mUserName;
    private int mUserScore;

    /**
     * Fragments
     */
    private GameScreenFragment mGameScreenFragment;
    private GameScoreFragment mGameScoreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadEnvironment();
        loadPresenter();
        loadHomeScreen();
    }


    //Load prerequisite before UI starts
    private void loadEnvironment() {
        mMemoryGameApp = (MemoryGameApplication)getApplication();

    }


    //Initialize actvity presenter
    private void loadPresenter() {
        mPresenter = (MainActivityPresenter)mMemoryGameApp.getPresenter(TAG);
        if(mPresenter == null) {
            mPresenter = new MainActivityPresenter(this);
            mMemoryGameApp.registerPresenter(TAG, mPresenter);
        }
    }


    //initialise home screen UI
    private void loadHomeScreen() {
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                mGameScreenFragment = GameScreenFragment.newInstance(position + 1);
                if(mFragmentManager == null) {
                    mFragmentManager = getSupportFragmentManager();
                }
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mGameScreenFragment)
                        .commit();
                break;
            case 1:
                mPresenter.getAllRecords();
                break;
        }

    }

    @Override
    public void showHighestScore() {
        mPresenter.queryHighestScore();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void notifyCurrentScore(int score) {
        mNavigationDrawerFragment.updateCurrentScore(score);
    }


    @Override
    public void showInputDialog(final int currentScore) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialogbox, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mUserName = editText.getText().toString();
                        mUserScore = currentScore;
                        mPresenter.saveRecord(mUserName, mUserScore, false);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onSuccessfulSaveData(String name, int score) {
        mGameScreenFragment.resetGame();
        mPresenter.queryHighestScore();
    }

    @Override
    public void onErrorReport(ErrorResponse errorResponse) {
        if(errorResponse.getErrorCode() == IAppConstants.ERROR_RESPONSE_DUPLICATE_ENTRY) {
            //show dialog to overwrite entry or not
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.duplicate_entry_title));
            alertDialogBuilder.setMessage(getResources().getString(R.string.duplicate_entry_message));
            alertDialogBuilder.setCancelable(false).
                    setPositiveButton(getResources().
                            getString(R.string.text_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mPresenter.saveRecord(mUserName, mUserScore, true);
                        }
                    }).setNegativeButton(getResources().getString(R.string.text_no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mGameScreenFragment.resetGame();
                            dialog.cancel();
                        }
                    });
            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }


    /**
     * Navigate user to score list screen.
     * @param view top right (High score) button view.
     */
    public void goToScoreScreen(View view) {
        onNavigationDrawerItemSelected(1);
    }


    @Override
    protected void onDestroy() {
        exitActivity();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        exitActivity();
        super.onBackPressed();
    }

    private void exitActivity() {
        if(mFragmentManager != null) {
            mPresenter.clearFragmentBackstack(mFragmentManager);
        }
        mPresenter.unregisterPresenter();
        mMemoryGameApp.exitGame();

    }


    @Override
    public void onSuccessAllData(AllUsersRecord allUsersRecord) {
        if(allUsersRecord == null) {
            ArrayList<UserScoreData> list = new ArrayList<>();
            allUsersRecord.setUserScoreDataList(list);
        }
        mGameScoreFragment = GameScoreFragment.newInstance(2, allUsersRecord.getUserScoreDataList());
        if(mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        mFragmentManager.beginTransaction().replace(R.id.container, mGameScoreFragment).commit();
    }

    @Override
    public void updateHighestScore(int highestScore) {
        mNavigationDrawerFragment.setHighestScore(highestScore);
    }
}
