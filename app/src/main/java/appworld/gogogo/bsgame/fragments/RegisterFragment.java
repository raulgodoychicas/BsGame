package appworld.gogogo.bsgame.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import appworld.gogogo.bsgame.MainActivity;
import appworld.gogogo.bsgame.R;
import appworld.gogogo.bsgame.support.SharedPrefsMethods;

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

        Button registerButton = (Button) view.findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTextInputEditText.getText().toString();
                String password = passwordTextInputEditText.getText().toString();
                String passwordRepeat = repeatPasswordTextInputEditText.getText().toString();

                if (!isUserNameAvailable(username)) {
                    emptyAllErrorTexts();
                    usernameTextInputLayout.setError("Username is not available");
                } else if (isPasswordAccordingToRules(password, passwordRepeat)) {
                    SharedPrefsMethods.writeStringToSharedPrefs(getActivity(), username, password);
                    //TODO Datenbankverbindnug herstellen und Credentials reinschreiben!!! Internet verbindung checken // user schon vorhanden
                    if(isNetworkAvailable(getActivity())) {
                        BackGround doInBackGround = new BackGround();
                        doInBackGround.execute(username, password);
                    } else {
                        Toast.makeText(getActivity(),"Registrierung nicht möglich. Bitte Internetverbindung prüfen",Toast.LENGTH_LONG).show();

                    }

                    MainActivity.switchFragment(new LoginFragment(), getActivity(), true);
                }
            }
        });
    }

    /**
     * This Method proofs if the given username is contained in the Database.
     * Right now Databse = Shared Preferences
     *
     * @param username
     * @return
     */

    private Boolean isUserNameAvailable(String username) {
        //TODO Datenbankverbindung herstellen und überprüfen,ob user bereits exisitiert !
        return !SharedPrefsMethods.containsStringInSharedPrefs(getActivity(), username);
    }

    /**
     * This Methods controls if the password has the needed Format
     *
     * @param passwordString
     * @param repeatpasswordString
     */
    private Boolean isPasswordAccordingToRules(String passwordString, String repeatpasswordString) {
        if (passwordString.isEmpty()) {
            emptyAllErrorTexts();
            passwordTextInputLayout.setError("Please fill this field");
            return false;
        }
        if (repeatpasswordString.isEmpty()) {
            emptyAllErrorTexts();
            repeatPasswordTextInputLayout.setError("Please fill this field");
            return false;
        }
        if (!passwordString.equals(repeatpasswordString)) {
            emptyAllErrorTexts();
            repeatPasswordTextInputLayout.setError("Passwords have to be identic");
            return false;
        }
        if (passwordString.length() < 4) {
            emptyAllErrorTexts();
            passwordTextInputLayout.setError("Password is too short");
            return false;
        }
        if (passwordString.contains("!") || passwordString.contains("&")
                || passwordString.contains("_") || passwordString.contains("-")
                || passwordString.contains(".") || passwordString.contains("%")) {
            return true;
        } else {
            emptyAllErrorTexts();
            passwordTextInputLayout.setError("Password must contain !&-_%.");
            return false;
        }
    }
    //Check if there is Internet Connection
    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void emptyAllErrorTexts() {
        passwordTextInputLayout.setError("");
        repeatPasswordTextInputLayout.setError("");
        usernameTextInputLayout.setError("");
    }


    //Background Task which saves User-Credentials in Database
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://www.worldlustblog.de/Registration/register.php");
                String urlParams = "&name=" + username + "&password=" + password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                while ((tmp = inputStream.read()) != -1) {
                    data += (char) tmp;
                }
                inputStream.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("")){
                s="Registrierung war erfolgreich";
            }
            Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
        }
    }

}