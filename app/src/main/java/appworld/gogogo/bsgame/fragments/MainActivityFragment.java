package appworld.gogogo.bsgame.fragments;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import appworld.gogogo.bsgame.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    TextView textView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.fragment_textview);

        Button buttonRot = (Button) view.findViewById(R.id.rot_button);
        Button buttonGelb = (Button) view.findViewById(R.id.gelb_button);
        Button buttonEnde= (Button) view.findViewById(R.id.ende_button);

        buttonRot.setOnClickListener(this);
        buttonGelb.setOnClickListener(this);
        buttonEnde.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rot_button:
                textView.setBackgroundColor(Color.RED);
                break;
            case R.id.gelb_button:
                textView.setBackgroundColor(Color.YELLOW);
                break;
            case R.id.ende_button:
                getActivity().finish();
                break;

        }

    }
}
