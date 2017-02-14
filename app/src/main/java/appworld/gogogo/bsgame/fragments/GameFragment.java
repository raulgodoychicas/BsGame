package appworld.gogogo.bsgame.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import appworld.gogogo.bsgame.R;
import appworld.gogogo.bsgame.engine.PlayGroundView;
import appworld.gogogo.bsgame.interfaces.PlayerListener;
import appworld.gogogo.bsgame.objects.MarkedRects;
import appworld.gogogo.bsgame.support.SharedPrefsMethods;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment implements PlayerListener {

    public static String GAME_MODE;
    private int gameModeInt;
    private boolean singlePlayerMode;
    private TextView player1scoreTextView, player2scoreTextView;
    private LinearLayout player1LinearLayout, player2LinearLayout;
    private Context context;

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
        setHasOptionsMenu(true);
        gameModeInt = getArguments().getInt(GAME_MODE, 0);
        singlePlayerMode = gameModeInt % 10 == 1;
        context = getActivity();
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

        PlayGroundView playGroundView = new PlayGroundView(view.getContext(), gameModeInt, this);
        playGroundView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        relativeLayout.addView(playGroundView);

        player1scoreTextView = (TextView) view.findViewById(R.id.fragment_game_player1score_textview);
        player1scoreTextView.setText("0");
        player2scoreTextView = (TextView) view.findViewById(R.id.fragment_game_player2score_textview);
        player2scoreTextView.setText("0");

        player1LinearLayout = (LinearLayout) view.findViewById(R.id.fragment_game_player1_linearlayout);
        player2LinearLayout = (LinearLayout) view.findViewById(R.id.fragment_game_player2_linearlayout);

        TextView player1NameTextView = (TextView) view.findViewById(R.id.fragment_game_player1_textview);
        TextView player2NameTextView = (TextView) view.findViewById(R.id.fragment_game_player2_textview);

        player1NameTextView.setText(SharedPrefsMethods.readStringFromSharedPrefs(getActivity(), LoginFragment.USER_NAME_KEY));
        if (singlePlayerMode) {
            player2NameTextView.setText(R.string.fragment_overview_computer_name);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem logoutMenuItem = menu.findItem(R.id.action_logout);
        MenuItem clearDataMenuItem = menu.findItem(R.id.action_clear_data);
        logoutMenuItem.setVisible(false);
        clearDataMenuItem.setVisible(false);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void changePlayer(int player) {
        if (player == 0) {
            player1LinearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.orange_p1_zug));
            player2LinearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Background));
        } else {
            player1LinearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Background));
            player2LinearLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blau_p2_zug));
        }
    }

    @Override
    public void changeScore(MarkedRects[] MarkedRects) {
        int player1Score = 0;
        int player2Score = 0;

        for (MarkedRects markedRect : MarkedRects) {
            if (markedRect.player == 0) {
                player1Score++;
            } else if (markedRect.player == 1) {
                player2Score++;
            }
        }
        player1scoreTextView.setText(String.valueOf(player1Score));
        player2scoreTextView.setText(String.valueOf(player2Score));
    }

    @Override
    public void onGameFinished(String winner, String points) {
        createGameFinishedDialog(winner, points);
    }

    private void createGameFinishedDialog(String winner, String points) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Spiel Beendet!");
        builder.setMessage("Spieler " + winner + " hat " + points + " Punkte erzielt und gewonnen");
        builder.setPositiveButton("Spiel anschauen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Verlassen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getFragmentManager().popBackStack();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
