package appworld.gogogo.bsgame.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import appworld.gogogo.bsgame.R;

/**
 * A simple Fragment displaying a layout for a Feature not available in the App
 */
public class NotAvailableFragment extends Fragment {

    public NotAvailableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not_available, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem logoutMenuItem = menu.findItem(R.id.action_logout);
        MenuItem clearDataMenuItem = menu.findItem(R.id.action_clear_data);
        MenuItem impressumMenuItem = menu.findItem(R.id.action_impressum);
        logoutMenuItem.setVisible(false);
        clearDataMenuItem.setVisible(false);
        impressumMenuItem.setVisible(false);
        getActivity().invalidateOptionsMenu();
    }
}
