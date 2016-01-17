package memgame.hemant.com.memorygame.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import memgame.hemant.com.memorygame.MainActivity;
import memgame.hemant.com.memorygame.repositories.UserScoreData;

/**
 * Created by Hemant on 10/2/15.
 */
public class GameScoreFragment extends ListFragment {

    private static ArrayList<UserScoreData> arrayList;

    public static GameScoreFragment newInstance() {
        GameScoreFragment fragment = new GameScoreFragment();
        return fragment;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GameScoreFragment newInstance(int sectionNumber, List<UserScoreData> list) {
        GameScoreFragment fragment = new GameScoreFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        arrayList = (ArrayList<UserScoreData>) list;
        return fragment;
    }

    public GameScoreFragment(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<String> dataList = new ArrayList<>();
        for(UserScoreData userdata:arrayList) {
            dataList.add(userdata.getName()+"         "+userdata.getScore());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                dataList);
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
