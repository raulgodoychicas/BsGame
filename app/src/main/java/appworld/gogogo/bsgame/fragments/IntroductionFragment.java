package appworld.gogogo.bsgame.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import appworld.gogogo.bsgame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroductionFragment extends Fragment {
    private TextView introductionHeaderTextView;
    private TextView introductionBodyTextView;


    public IntroductionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_introduction, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        introductionHeaderTextView = (TextView) view.findViewById(R.id.introduction_header_textview);
        introductionBodyTextView = (TextView) view.findViewById(R.id.introduction_body_textview);

        Spanned resultHeader;
        Spanned resultBody;

        //SDK-Version Check because .fromHtml requires a certain version
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            resultHeader = Html.fromHtml(getString(R.string.introduction_header_textview), Html.FROM_HTML_MODE_LEGACY);
            resultBody = Html.fromHtml(getString(R.string.introduction_body_textview), Html.FROM_HTML_MODE_LEGACY);
            introductionHeaderTextView.setText(resultHeader);
            introductionBodyTextView.setText(resultBody);
        } else {
            resultHeader = Html.fromHtml(getString(R.string.introduction_header_textview));
            resultBody = Html.fromHtml(getString(R.string.introduction_body_textview));
            introductionHeaderTextView.setText(resultHeader);
            introductionBodyTextView.setText(resultBody);
        }
    }

}
