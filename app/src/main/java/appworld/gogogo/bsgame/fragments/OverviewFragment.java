package appworld.gogogo.bsgame.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import appworld.gogogo.bsgame.R;

import static appworld.gogogo.bsgame.R.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {

    private Spinner field_size;
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
        field_size = (Spinner)view.findViewById(id.choose_fieldsize);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), array.size_array,android.R.layout.simple_spinner_item);
        field_size.setAdapter(adapter);





    }
}
