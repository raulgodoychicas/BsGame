package appworld.gogogo.bsgame.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import appworld.gogogo.bsgame.MainActivity;
import appworld.gogogo.bsgame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout repeatPasswordTextInputLayout;
    private TextInputLayout usernameTextInputLayout;
    private TextInputEditText passwordTextInputEditText;
    private TextInputEditText repeatPasswordTextInputEditText;
    private TextInputEditText usernameTextInputEditText;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_username_textinputlayout);
        usernameTextInputLayout.setErrorEnabled(true);
        usernameTextInputEditText = (TextInputEditText) view.findViewById(R.id.register_username_textinputedittext);

        passwordTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_password_textinputlayout);
        passwordTextInputLayout.setErrorEnabled(true);
        passwordTextInputEditText = (TextInputEditText) view.findViewById(R.id.register_password_textinputedittext);

        repeatPasswordTextInputLayout = (TextInputLayout) view.findViewById(R.id.register_repeatpassword_textinputlayout);
        repeatPasswordTextInputLayout.setErrorEnabled(true);
        repeatPasswordTextInputEditText = (TextInputEditText) view.findViewById(R.id.register_repeatpassword_textinputedittext);

        Button registerButton = (Button) view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUserNameAvailable(usernameTextInputEditText.getText().toString())){
                    usernameTextInputLayout.setError("Username is not available");
                } else if (isPasswordAccordingToRules(passwordTextInputEditText.getText().toString(),
                        repeatPasswordTextInputEditText.getText().toString())){
                    //TODO: Benutzer in der Datenbank speichern
                    MainActivity.switchFragment(new PlaceHolderFragment(), getActivity());
                };
            }
        });
    }

    private Boolean isUserNameAvailable(String username) {
        //TODO: Select abfrage in der Datenbank um zu überprüfen ob der Name schon vergeben ist
        return true;
    }

    /**
     * This Methods controls if the password has the needed Format
     *
     * @param passwordString
     * @param repeatpasswordString
     */
    private Boolean isPasswordAccordingToRules(String passwordString, String repeatpasswordString) {
        if (passwordString.isEmpty()) {
            passwordTextInputLayout.setError("Please fill this field");
            return false;
        }
        if (repeatpasswordString.isEmpty()) {
            repeatPasswordTextInputLayout.setError("Please fill this field");
            return false;
        }
        if (passwordString.equals(repeatpasswordString)) {
            repeatPasswordTextInputLayout.setError("Passwords have to be identic");
            return false;
        }
        if (passwordString.length() < 4) {
            passwordTextInputLayout.setError("Password is too short");
            return false;
        }
        if (!passwordString.contains("!") || !passwordString.contains("&")
                || !passwordString.contains("_") || !passwordString.contains("-")
                || !passwordString.contains(".") || !passwordString.contains("%")) {
            passwordTextInputLayout.setError("Password must contain !&-_%.");
        }
        return true;
    }
}
