package appworld.gogogo.bsgame.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    private Button goToIntroButton;
    private Button helpButton;

//    private TextView userNameTextView;

    /*Bsp. gameModeID:
             31 --> Fieldsize 3 = 3x3 , GameMode 1 = Single Player
             52 --> Fieldsize 5 = 5x5 , GameMode 2 = MultiPlayer
            103 --> Fieldsize 10 = 10x10 , GameMode 3 = Multi Player Online */

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
        fieldSizeSpinner = (Spinner) view.findViewById(R.id.overview_choosefieldsize);
        gameModeSpinner = (Spinner) view.findViewById(R.id.overview_spinner_gamemode);

        //Button initialiseren
        startGameButton = (Button) view.findViewById(R.id.overwiew_start_button);

        // Button Introduction
        goToIntroButton = (Button) view.findViewById(id.overwiew_introduction_button);

        // Help Button
        helpButton = (Button) view.findViewById(R.id.overview_help_button);

        //textView initialisieren
//        userNameTextView = (TextView)view.findViewById(R.id.overview_textview_username);

        //TODO Username übernehmen
        //o_username.setText("Aaron");

        //Array für Feldauswahl und Spielmodusauswahl initialiseren
        ArrayAdapter<CharSequence> field_size_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_size_array, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> game_mode_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_game_mode_array, android.R.layout.simple_spinner_dropdown_item);
        fieldSizeSpinner.setAdapter(field_size_adapter);
        gameModeSpinner.setAdapter(game_mode_adapter);

        //Listener setzen
        fieldSizeSpinner.setOnItemSelectedListener(this);
        gameModeSpinner.setOnItemSelectedListener(this);
        startGameButton.setOnClickListener(this);
        goToIntroButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        //Spielmodus und Spielfeldgröße des Spielers auslesen und eindeutige ID (int) an GameFragment übergeben
        switch (fieldSizeSpinner.getSelectedItem().toString()) {
            case ("3x3"):
                if (gameModeSpinner.getSelectedItem().toString().equals("Single-Player")) {
                    gameModeID = 31;
                } else if (gameModeSpinner.getSelectedItem().toString().equals("Multi-Player")) {
                    gameModeID = 32;
                } else {
                    gameModeID = 33;
                }
                break;
            case ("5x5"):
                if (gameModeSpinner.getSelectedItem().toString().equals("Single-Player")) {
                    gameModeID = 51;
                } else if (gameModeSpinner.getSelectedItem().toString().equals("Multi-Player")) {
                    gameModeID = 52;
                } else {
                    gameModeID = 53;
                }
                break;
            case ("10x10"):
                if (gameModeSpinner.getSelectedItem().toString().equals("Single-Player")) {
                    gameModeID = 101;
                } else if (gameModeSpinner.getSelectedItem().toString().equals("Multi-Player")) {
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
        switch (view.getId()) {
            case id.overwiew_start_button:
                MainActivity.switchFragment(GameFragment.newInstance(gameModeID), getActivity(), true);
                break;
            case id.overwiew_introduction_button:
                MainActivity.switchFragment(new IntroductionFragment(), getActivity(), true);
                break;
            case id.overview_help_button:
                createHelpDialog();
                break;
        }
    }

    private void createHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Title");
        builder.setMessage("Erklärung über beide Buttons");
        builder.setPositiveButton("Verstanden", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
