package appworld.gogogo.bsgame.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import appworld.gogogo.bsgame.MainActivity;
import appworld.gogogo.bsgame.R;

import static appworld.gogogo.bsgame.R.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner fieldSizeSpinner;
    private Spinner gameModeSpinner;
    private Button startGameButton;

    private TextView userNameTextView;

    /*Bsp. gameModeID:
             31 --> Fieldsize 3 = 3x3 , GameMode 1 = Single Player
             52 --> Fieldsize 5 = 5x5 , GameMode 2 = MultiPlayer
            103 --> Fieldsite 10 = 10x10 , GameMode 3 = Multi Player Online */

    private int gameModeID;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Spinner initialisieren
        fieldSizeSpinner = (Spinner)view.findViewById(R.id.overview_choosefieldsize);
        gameModeSpinner = (Spinner)view.findViewById(R.id.overview_spinner_gamemode);

        //Button initialiseren
        startGameButton = (Button)view.findViewById(R.id.overwiew_start_button);

        //textView initialisieren
        userNameTextView = (TextView)view.findViewById(R.id.overview_textview_username);

        //TODO Username übernehmen
        //o_username.setText("Aaron");

        //Array für Feldauswahl und Spielmodusauswahl initialiseren
        ArrayAdapter<CharSequence> field_size_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_size_array,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> game_mode_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_game_mode_array,android.R.layout.simple_spinner_dropdown_item);
        fieldSizeSpinner.setAdapter(field_size_adapter);
        gameModeSpinner.setAdapter(game_mode_adapter);

        //Listener setzen
        fieldSizeSpinner.setOnItemSelectedListener(this);
        gameModeSpinner.setOnItemSelectedListener(this);
        startGameButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        //Spielmodus und Spielfeldgröße des Spielers auslesen und eindeutige ID (int+string) an GameFragment übergeben
        switch (fieldSizeSpinner.getSelectedItem().toString()){
            case ("10x10"):
                if(gameModeSpinner.getSelectedItem().toString().equals("Single-Player")) {
                    gameModeID = 31;
                } else if (gameModeSpinner.getSelectedItem().toString().equals("Multi-Player")){
                    gameModeID = 32;
                } else {
                    gameModeID = 33;
                }
                break;
            case ("20x20"):
                if(gameModeSpinner.getSelectedItem().toString().equals("Single-Player")) {
                    gameModeID = 51;
                } else if (gameModeSpinner.getSelectedItem().toString().equals("Multi-Player")){
                    gameModeID = 52;
                } else {
                    gameModeID = 53;
                }
                // TODO: Integer gamemode definieren für gameFragment
                break;
            case ("30x30"):
                if(gameModeSpinner.getSelectedItem().toString().equals("Single-Player")) {
                    gameModeID = 101;
                } else if (gameModeSpinner.getSelectedItem().toString().equals("Multi-Player")){
                    gameModeID = 102;
                } else {
                    gameModeID = 103;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onClick(View view) {
        MainActivity.switchFragment(GameFragment.newInstance(gameModeID),getActivity());
    }

}
