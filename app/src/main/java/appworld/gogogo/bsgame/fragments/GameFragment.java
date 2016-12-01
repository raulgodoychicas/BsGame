package appworld.gogogo.bsgame.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import appworld.gogogo.bsgame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    public static String GAME_MODE;

    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance(int gameModeInt) {
        GameFragment gameFragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(GAME_MODE, gameModeInt);
        gameFragment.setArguments(args);
        return gameFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int gameModeInt;
        gameModeInt = getArguments().getInt(GAME_MODE, 0);
        Toast.makeText(this.getActivity(), "GAME_ID: " + String.valueOf(gameModeInt), Toast.LENGTH_SHORT).show();
    }


    /**
     * Object class to identify all of the Lines
     * Changes will still probably come
     */
    public class lineOnFeld {
        public int top;
        public int left;
        public int bottom;
        public int right;
        public int player;

        public lineOnFeld(int top, int left, int bottom, int right, int player) {
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
            this.player = player;
        }

    }

}
