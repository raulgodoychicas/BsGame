package appworld.gogogo.bsgame.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import appworld.gogogo.bsgame.R;
import appworld.gogogo.bsgame.engine.PlayGroundView;
import appworld.gogogo.bsgame.interfaces.PlayerListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment implements PlayerListener {

    public static String GAME_MODE;
    private int gameModeInt;
    private int player;
    private int player1score;
    private int player2score;
    private TextView player1scoreTextView, player2scoreTextView;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameModeInt = getArguments().getInt(GAME_MODE, 0);
        player1score = 0;
        player2score = 0;
        player = 1;
        Toast.makeText(this.getActivity(), "GAME_ID: " + String.valueOf(gameModeInt), Toast.LENGTH_SHORT).show();
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

        PlayGroundView playGroundView = new PlayGroundView(view.getContext(), gameModeInt / 10, this);
        playGroundView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        relativeLayout.addView(playGroundView);

        player1scoreTextView = (TextView) view.findViewById(R.id.fragment_game_player1score_textview);
        player1scoreTextView.setText("0");
        player2scoreTextView = (TextView) view.findViewById(R.id.fragment_game_player2score_textview);
        player2scoreTextView.setText("0");

    }


    @Override
    public void changePlayer(int player) {
        this.player = player;
    }

    @Override
    public void changeScore() {
        if (player == 0) {
            player1score++;
            player1scoreTextView.setText(String.valueOf(player1score));
        } else {
            player2score++;
            player2scoreTextView.setText(String.valueOf(player2score));
        }

    }

    @Override
    public void onGameFinished() {

    }
}
