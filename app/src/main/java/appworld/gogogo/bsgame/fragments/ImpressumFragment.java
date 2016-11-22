package appworld.gogogo.bsgame.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        Spanned resultHeader;
        Spanned resultBody;

        //SDK-Version Check because .fromHtml requires a certain version
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            resultHeader = Html.fromHtml(getString(R.string.impressum_header_textview),Html.FROM_HTML_MODE_LEGACY);
            resultBody = Html.fromHtml(getString(R.string.impressum_body_textview),Html.FROM_HTML_MODE_LEGACY);
            impressumHeaderTextView.setText(resultHeader);
            impressumBodyTextView.setText(resultBody);
        } else {
            resultHeader = Html.fromHtml(getString(R.string.impressum_header_textview));
            resultBody = Html.fromHtml(getString(R.string.impressum_body_textview));
            impressumHeaderTextView.setText(resultHeader);
            impressumBodyTextView.setText(resultBody);
        }
    }

}
