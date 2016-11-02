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
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button loginButton;
    private Button registerButton;

    private TextInputEditText usernameTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        registerButton = (Button) view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);

        usernameTextInputLayout = (TextInputLayout) view.findViewById(R.id.login_username_textinputlayout);
        usernameTextInputLayout.setErrorEnabled(true);
        usernameTextInputEditText = (TextInputEditText) view.findViewById(R.id.login_username_textinputedittext);

        passwordTextInputLayout = (TextInputLayout) view.findViewById(R.id.login_password_textinputlayout);
        passwordTextInputLayout.setErrorEnabled(true);
        passwordTextInputEditText = (TextInputEditText) view.findViewById(R.id.login_password_textinputedittext);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button: {
                if (isPasswordRight(usernameTextInputEditText.getText().toString(),
                        passwordTextInputEditText.getText().toString())) {
                    MainActivity.switchFragment(new OverviewFragment(),getActivity());
                }
                break;
            }
            case R.id.register_button: {
                MainActivity.switchFragment(new RegisterFragment(), getActivity());
                break;
            }

        }
    }

    /**
     * Checks if the Username and Password matches with any saved username in the Database
     *
     * @param username Username
     * @param password Password
     */
    private boolean isPasswordRight(String username, String password) {

        /**
         * TODO:
         * Check Database for the User (if()return boolean)
         * Check if the password matches the User (if()return boolean)
         *
         */
        if (!username.equals("playerOne")) {
            usernameTextInputLayout.setError("Use playerOne as username");
            return false;
        } else if (!password.equals("blyat")) {
            passwordTextInputLayout.setError("use blyat als password");
            return false;
        } else {
            return true;
        }
    }
}
