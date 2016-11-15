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
import android.widget.Toast;

import appworld.gogogo.bsgame.MainActivity;
import appworld.gogogo.bsgame.R;
import appworld.gogogo.bsgame.engine.PlayGroundView;

import static appworld.gogogo.bsgame.R.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner o_Field_Size;
    private Spinner o_Game_Mode;
    private Button o_Start_Game;
    public static int Create_Field_Size;


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

        //Spinner field_size initialisieren
        o_Field_Size = (Spinner)view.findViewById(R.id.overview_choosefieldsize);
        o_Game_Mode = (Spinner)view.findViewById(R.id.overview_spinner_gamemode);
        o_Start_Game = (Button)view.findViewById(R.id.overwiew_start_button);


        ArrayAdapter<CharSequence> field_size_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_size_array,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> game_mode_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_game_mode,android.R.layout.simple_spinner_dropdown_item);

        o_Field_Size.setAdapter(field_size_adapter);
        o_Game_Mode.setAdapter(game_mode_adapter);

        o_Field_Size.setOnItemSelectedListener(this);
        o_Game_Mode.setOnItemSelectedListener(this);

        o_Start_Game.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (o_Field_Size.getSelectedItem().toString()){

            case ("10x10"):
                if(o_Game_Mode.getSelectedItem().toString().equals("Single-Player")) {
                    //MainActivity.switchFragment(new LoginFragment(),getActivity());
                    Toast.makeText(this.getActivity(), "10x10 Single-Player-Modus", Toast.LENGTH_SHORT).show();
                } else if (o_Game_Mode.getSelectedItem().toString().equals("Multi-Player")){
                    Toast.makeText(this.getActivity(), "10x10 Multi-Player-Modus", Toast.LENGTH_SHORT).show();
                    
                } else {
                    Toast.makeText(this.getActivity(), "10x10 Multi-Player-Online", Toast.LENGTH_SHORT).show();
                    Create_Field_Size = 3;
                }
                // Integer übergeben in Game-Fragment Fragement und mit bundle Werte übergeben zum zum Aufbau des Spielfeldes mit entsprechender Größe
                break;

            case ("20x20"):
                if(o_Game_Mode.getSelectedItem().toString().equals("Single-Player")) {
                    //MainActivity.switchFragment(new LoginFragment(),getActivity());
                    Toast.makeText(this.getActivity(), "20x20 Single-Player-Modus", Toast.LENGTH_SHORT).show();
                } else if (o_Game_Mode.getSelectedItem().toString().equals("Multi-Player")){
                    Toast.makeText(this.getActivity(), "20x20 Multi-Player-Modus", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this.getActivity(), "20x20 Multi-Player-Online", Toast.LENGTH_SHORT).show();
                }
                // Integer übergeben in Game-Fragment Fragement und mit bundle Werte übergeben zum zum Aufbau des Spielfeldes mit entsprechender Größe
                break;

            case ("30x30"):
                if(o_Game_Mode.getSelectedItem().toString().equals("Single-Player")) {
                    //MainActivity.switchFragment(new LoginFragment(),getActivity());
                    Toast.makeText(this.getActivity(), "30x30 Single-Player-Modus", Toast.LENGTH_SHORT).show();
                } else if (o_Game_Mode.getSelectedItem().toString().equals("Multi-Player")){
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

        //view.getId();
        MainActivity.switchFragment(new GameFragment(),getActivity());




    }
}
