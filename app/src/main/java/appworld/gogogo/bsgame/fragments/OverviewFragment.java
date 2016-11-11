package appworld.gogogo.bsgame.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import appworld.gogogo.bsgame.R;

import static appworld.gogogo.bsgame.R.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Spinner o_field_size;
    private Spinner o_game_mode;
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
        o_field_size = (Spinner)view.findViewById(R.id.overview_choosefieldsize);
        o_game_mode = (Spinner)view.findViewById(R.id.overview_spinner_gamemode);

        ArrayAdapter<CharSequence> field_size_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_size_array,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> game_mode_adapter = ArrayAdapter.createFromResource(getActivity(), array.overview_game_mode,android.R.layout.simple_spinner_dropdown_item);

        o_field_size.setAdapter(field_size_adapter);
        o_game_mode.setAdapter(game_mode_adapter);

        o_field_size.setOnItemSelectedListener(this);
        o_game_mode.setOnItemSelectedListener(this);
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
                    Toast.makeText(this.getActivity(), "10x10 Multi-Player-Online", Toast.LENGTH_SHORT).show();
                }

                // Integer übergeben in leeres Fragement und mit bundle Werte übergeben zum zum Aufbau des Spielfeldes mit entsprechender Größe
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
