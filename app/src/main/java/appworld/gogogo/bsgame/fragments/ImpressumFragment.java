package appworld.gogogo.bsgame.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import appworld.gogogo.bsgame.R;



public class ImpressumFragment extends Fragment {

    private TextView impressum;
    private TextView impressum_header;

    public ImpressumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_impressum, container, false);

    }
     //TODO in Strings XML machen, verhindern dass Fragment immer neu geladen wird
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        impressum = (TextView)view.findViewById(R.id.impressum_textView);
        impressum_header = (TextView)view.findViewById(R.id.impressumHeader_textView);

        impressum_header.setText(Html.fromHtml("<u>Impressum</u>"));

        impressum.setText(Html.fromHtml("<u>SG Solutions AG - Lösungen für ihren Vorsprung</u><br><br>" +
                "Rechtsform: Kapitalgesellschaft<br><br>" +
                "Unternehmensgegenstand: Sofwareentwicklung<br><br>" +
                "UID Nummer: DE 54611651<br><br>" +
                "Handelsregisternummer: HRB 846552<br><br>" +
                "Handelsregistergericht: Amtsgericht München<br><br>" +
                "Firmensitz: Freising<br><br>" +
                "Anschrift: 85354 Freising, Musterstraße 1, Deutschland.<br><br>" +
                "Telefon +49 8161 / 123 456<br>" +
                "Email: support@sg-solutions.de<br><br>" +
                "Geschäftsführer:<br>" +
                "Aaron Siegl, Raul Gonzales Godoy"));
    }

}
