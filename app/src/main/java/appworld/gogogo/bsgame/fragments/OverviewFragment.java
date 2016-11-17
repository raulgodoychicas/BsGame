package appworld.gogogo.bsgame.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
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
import appworld.gogogo.bsgame.engine.PlayGroundView;
import appworld.gogogo.bsgame.support.SharedPrefsMethods;

import static appworld.gogogo.bsgame.R.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner o_field_size;
    private Spinner o_game_mode;
    private Button o_start_game;
    private TextView o_username;
    public String create_field_size;


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
        o_field_size = (Spinner)view.findViewById(R.id.overview_choosefieldsize);
        o_game_mode = (Spinner)view.findViewById(R.id.overview_spinner_gamemode);

        //Button initialiseren
        o_start_game = (Button)view.findViewById(R.id.overwiew_start_button);

        //textView initialisieren
        o_username = (TextView)view.findViewById(R.id.overview_textview_username);

        //Username übernehmen
        //o_username.setText("Aaron");

        //Array für Feldauswahl und Spielmodusauswahl initialiseren
        ArrayAdapter<CharSequence> field_size_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_size_array,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> game_mode_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_game_mode_array,android.R.layout.simple_spinner_dropdown_item);
        o_field_size.setAdapter(field_size_adapter);
        o_game_mode.setAdapter(game_mode_adapter);

        //Listener setzen
        o_field_size.setOnItemSelectedListener(this);
        o_game_mode.setOnItemSelectedListener(this);
        o_start_game.setOnClickListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (o_field_size.getSelectedItem().toString()){

            case ("10x10"):
                if(o_game_mode.getSelectedItem().toString().equals("Single-Player")) {
                    //MainActivity.switchFragment(new LoginFragment(),getActivity());
                    Toast.makeText(this.getActivity(), "10x10 Single-Player-Modus", Toast.LENGTH_SHORT).show();
                } else if (o_game_mode.getSelectedItem().toString().equals("Multi-Player")){
                    Toast.makeText(this.getActivity(), "10x10 Multi-Player-Modus", Toast.LENGTH_SHORT).show();
                    
                } else {
                    Toast.makeText(this.getActivity(), "10x10 Multi-Player-Modus-Online", Toast.LENGTH_SHORT).show();

                }
                // TODO: Integer übergeben in Game-Fragment Fragement und mit bundle Werte übergeben zum zum Aufbau des Spielfeldes mit entsprechender Größe
                break;

            case ("20x20"):
                if(o_game_mode.getSelectedItem().toString().equals("Single-Player")) {
                    //MainActivity.switchFragment(new LoginFragment(),getActivity());
                    Toast.makeText(this.getActivity(), "20x20 Single-Player-Modus", Toast.LENGTH_SHORT).show();
                } else if (o_game_mode.getSelectedItem().toString().equals("Multi-Player")){
                    Toast.makeText(this.getActivity(), "20x20 Multi-Player-Modus", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this.getActivity(), "20x20 Multi-Player-Online", Toast.LENGTH_SHORT).show();
                }
                // TODO: Integer übergeben in Game-Fragment Fragement und mit bundle Werte übergeben zum Aufbau des Spielfeldes mit entsprechender Größe
                break;

            case ("30x30"):
                if(o_game_mode.getSelectedItem().toString().equals("Single-Player")) {
                    //MainActivity.switchFragment(new LoginFragment(),getActivity());
                    Toast.makeText(this.getActivity(), "30x30 Single-Player-Modus", Toast.LENGTH_SHORT).show();
                } else if (o_game_mode.getSelectedItem().toString().equals("Multi-Player")){
                    Toast.makeText(this.getActivity(), "30x30 Multi-Player-Modus", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this.getActivity(), "30x30 Multi-Player-Online", Toast.LENGTH_SHORT).show();
                }
                // Integer übergeben in Game-Fragment Fragement und mit bundle Werte übergeben zum zum Aufbau des Spielfeldes mit entsprechender Größe
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        MainActivity.switchFragment(new GameFragment(),getActivity());
    }

    // TODO: Wieder löschen
    public void setPlayModeConstant(String m_PlayModeConstant){
        this.create_field_size = m_PlayModeConstant;
    }
    public String getPlayModeConstant(){

        return create_field_size;
    }


}
