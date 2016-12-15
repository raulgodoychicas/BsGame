package appworld.gogogo.bsgame.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import appworld.gogogo.bsgame.R;
import appworld.gogogo.bsgame.engine.PlayGroundView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    public static String GAME_MODE;
    private int gameModeInt;


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
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_game_relativelayout);

        PlayGroundView playGroundView = new PlayGroundView(view.getContext(), gameModeInt / 10);
        playGroundView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        relativeLayout.addView(playGroundView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameModeInt = getArguments().getInt(GAME_MODE, 0);
        Toast.makeText(this.getActivity(), "GAME_ID: " + String.valueOf(gameModeInt), Toast.LENGTH_SHORT).show();
    }



}
