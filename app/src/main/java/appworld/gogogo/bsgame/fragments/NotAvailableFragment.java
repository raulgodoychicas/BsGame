package appworld.gogogo.bsgame.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import appworld.gogogo.bsgame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotAvailableFragment extends Fragment {


    public NotAvailableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not_available, container, false);
    }

}
