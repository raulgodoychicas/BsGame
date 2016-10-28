package appworld.gogogo.bsgame.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import appworld.gogogo.bsgame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout repeatPasswordTextInputLayout;
    private TextInputEditText passwordTextInputEditText;
    private TextInputEditText repeatPasswordTextInputEditText;

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
                isPasswordAccordingToRules(passwordTextInputEditText.getText().toString(),
                        repeatPasswordTextInputEditText.getText().toString());

            }
        });

    }

    private void checkUsername(String username){

    }

    private void isPasswordAccordingToRules(String passwordString, String repeatpasswordString){

        if (passwordString.equals(repeatpasswordString)){

        }
    }
}
