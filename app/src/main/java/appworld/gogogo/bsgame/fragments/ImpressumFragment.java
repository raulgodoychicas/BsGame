package appworld.gogogo.bsgame.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import appworld.gogogo.bsgame.R;



public class ImpressumFragment extends Fragment {

    private TextView impressumHeaderTextView;
    private TextView impressumBodyTextView;

    public ImpressumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_impressum, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        impressumHeaderTextView = (TextView)view.findViewById(R.id.impressum_haeder_textview);
        impressumBodyTextView = (TextView)view.findViewById(R.id.impressum_body_textview);

        impressumHeaderTextView.setText(Html.fromHtml(getString(R.string.impressum_header_textview)));
        impressumBodyTextView.setText(Html.fromHtml(getString(R.string.impressum_body_textview)));
    }

}
