package memgame.hemant.com.memorygame.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Random;

import memgame.hemant.com.memorygame.MainActivity;
import memgame.hemant.com.memorygame.R;

/**
 * Game screen fragment design on HomeScreen
 *
 * Created by Hemant on 10/2/15.
 */
public class GameScreenFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = GameScreenFragment.class.getName();

    private View mRootView;

    //Table Layout inflater
    private TableLayout cardsTableLayout;

    // The card deck
    private final static int[] cardDeck = new int[]{
            R.drawable.colour1,
            R.drawable.colour2,
            R.drawable.colour3,
            R.drawable.colour4,
            R.drawable.colour5,
            R.drawable.colour6,
            R.drawable.colour7,
            R.drawable.colour8,
            R.drawable.colour1,
            R.drawable.colour2,
            R.drawable.colour3,
            R.drawable.colour4,
            R.drawable.colour5,
            R.drawable.colour6,
            R.drawable.colour7,
            R.drawable.colour8,
    };

    ArrayList<ImageView> flipTracker;

    /**
     * Interface instance, to pass message to Activity.
     */
    private IGameScreenFragment iActivityNotifier;

    //value to store current score
    private int currentScore = 0;
    private int imageRemaining = 16;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GameScreenFragment newInstance(int sectionNumber) {
        GameScreenFragment fragment = new GameScreenFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public GameScreenFragment() {
    }


    /**
     * Reset User running game to game start.
     */
    public void resetGame() {
        currentScore = 0;
        imageRemaining = 16;
        shuffleAllCards(cardDeck);
        for (int i = 0; i < cardsTableLayout.getChildCount(); i++)
        {
            TableRow row = (TableRow) cardsTableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++)
            {
                ImageView imageView = (ImageView) row.getChildAt(j);
                imageView.setVisibility(View.VISIBLE);
                if(iActivityNotifier != null) {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.card_bg));
                }
            }
        }
    }

    /**
     * Interface to communicate with attached activity.
     */
    public interface IGameScreenFragment {
        void notifyCurrentScore(int score);
        void showInputDialog(int score);
        void showHighestScore();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));

        iActivityNotifier = (IGameScreenFragment)activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        cardsTableLayout = (TableLayout)mRootView.findViewById(R.id.tl_cardsgrid);

        for (int i = 0; i < cardsTableLayout.getChildCount(); i++)
        {
            TableRow row = (TableRow) cardsTableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++)
            {
                ImageView imageView = (ImageView) row.getChildAt(j);
                imageView.setId(4*i+j);
                imageView.setOnClickListener(this);
            }
        }

        shuffleAllCards(cardDeck);
        flipTracker = new ArrayList<>(2);
        iActivityNotifier.notifyCurrentScore(currentScore);
        iActivityNotifier.showHighestScore();
        return mRootView;
    }

    @Override
    public void onClick(View v) {
        if(flipTracker.size() == 2) {
            return;
        }
        final ImageView iv = (ImageView)mRootView.findViewById(v.getId());
        //Flip Card
        if(isFlipped(iv)) {

            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.card_flip_left_out);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    iv.setImageDrawable(getResources().getDrawable(cardDeck[iv.getId()]));
                    flipTracker.add(iv);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(flipTracker.size() == 2) {
                        compareViews();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            iv.startAnimation(animation);
        }else {
            flipBackCard(iv);
        }
    }

    /**
     * This methods suffles the card deck randomly.
     * @param carddeck
     */
    public void shuffleAllCards(int[] carddeck) {
        int index;
        Random random = new Random();
        for (int i = carddeck.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            if (index != i)
            {
                carddeck[index] ^= carddeck[i];
                carddeck[i] ^= carddeck[index];
                carddeck[index] ^= carddeck[i];
            }
        }

        /*int index, temp;
        Random random = new Random();
        for (int i = carddeck.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = carddeck[index];
            carddeck[index] = carddeck[i];
            carddeck[i] = temp;
        }*/
    }


    /**
     * NOTE: This logic is for if cards will not flip on wrong selection.NOTE: This logic is for if cards will not flip on wrong selection.
     * @param flipView recent card view selected by user
     */
    /*private void compareViews(ImageView flipView) {
        for (int i=0; i<flipTracker.size()-1; i++) {
            if(flipView.getDrawable().getConstantState().equals(flipTracker.get(i).getDrawable().getConstantState())) {
                //removing the items that are paired
                flipView.setVisibility(View.INVISIBLE);
                flipTracker.get(i).setVisibility(View.INVISIBLE);
                currentScore += 2;
            }
        }
    }*/

    /**
     * Compare the cards selected by user
     */
    private void compareViews() {
        final ImageView view1 = flipTracker.get(0);
        final ImageView view2 = flipTracker.get(1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                if (view1.getDrawable().getConstantState().equals(view2.getDrawable().getConstantState())) {
                    //Items matched
                    view1.setVisibility(View.INVISIBLE);
                    view2.setVisibility(View.INVISIBLE);
                    flipTracker.clear();
                    currentScore += 2;
                    imageRemaining -=2;
                } else {
                    flipBackCard(view2);
                    currentScore -= 1;
                }
                iActivityNotifier.notifyCurrentScore(currentScore);
                if(imageRemaining == 0) {
                    iActivityNotifier.showInputDialog(currentScore);
                }
            }
        }, 1000);
    }


    /**
     * Flip the card back to original position.
     * @param cardView card that need to flip back.
     */
    private void flipBackCard(final ImageView cardView) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.card_flip_left_in);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setImageDrawable(getResources().getDrawable(R.drawable.card_bg));
                flipTracker.remove(flipTracker.size() - 1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        cardView.startAnimation(animation);
    }


    /**
     * Check if card is in flipped state or not.
     * @param cardView
     * @return true if card is flipped by user else false.
     */
    private boolean isFlipped(ImageView cardView) {
        if(cardView.getDrawable() != null) {
            return (cardView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.card_bg).getConstantState())) ? true : false;
        }else {
            return true;
        }
    }

}
