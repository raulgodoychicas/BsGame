package appworld.gogogo.bsgame.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import appworld.gogogo.bsgame.R;

/**
 * A simple {@link Fragment} subclass.
 *
 * This Fragment is empty and will be used as a Placeholder in the App
 * Further content should replace this Fragment
 *
 */
public class PlaceHolderFragment extends Fragment {

    public PlaceHolderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_holder, container, false);
    }

}
