package appworld.gogogo.bsgame.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

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
                    if (isNetworkAvailable(getActivity())) {
                        AsyncRegistraton doInBackGround = new AsyncRegistraton();
                        doInBackGround.execute(username, password);
                    } else {
                        Toast.makeText(getActivity(), "Registrierung nicht möglich. Bitte Internetverbindung prüfen", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * This Method proofs if the given username is contained in the Database.
     * Right now Database = Shared Preferences
     *
     * @param username
     * @return
     */

    private Boolean isUserNameAvailable(String username) {
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

    //This Method checks if internet connection is availabe
    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void emptyAllErrorTexts() {
        passwordTextInputLayout.setError("");
        repeatPasswordTextInputLayout.setError("");
        usernameTextInputLayout.setError("");
    }

    //inner class with async-task that saves User-Credentials in Online-Database
    class AsyncRegistraton extends AsyncTask<String, String, String> {

        //Loading Window in UI while loggin in
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        HttpURLConnection httpURLConnection;
        OutputStream outputStream;
        InputStream inputStream;


        protected String doInBackground(String... params) {

            //params[0] = username, params[1] = password
            String username = params[0];
            String password = params[1];

            //Initializing Data String that is later needed to get the response information of the Mysql-DB
            String data = "";

            //counter for while-loop
            int count;

            try {
                URL url = new URL("http://www.worldlustblog.de/Registration/register.php");
                String urlParams = "&name=" + username + "&password=" + password;

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(urlParams.getBytes());
                outputStream.flush();
                outputStream.close();
                inputStream = httpURLConnection.getInputStream();
                while ((count = inputStream.read()) != -1) {
                    data += (char) count;
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
        protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();
    }

        protected void onPostExecute(String s) {
            try {
                pdLoading.dismiss();
                //fetch JSON Object from Server
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_credentials");

                //fetch String from Array
                String COUNT = user_data.getString("count");

                //If result is not "0" --> User is already registered
                if (!COUNT.equals("0")) {
                    Toast.makeText(getActivity(),"User existiert bereits! Bitte anderen Usernamen verwenden!",Toast.LENGTH_LONG).show();
                } else {
                    //Switch to Overview
                    Toast.makeText(getActivity(),"Registrierung war erfolgreich",Toast.LENGTH_LONG).show();

                    //Registration was successful --> Login
                    MainActivity.switchFragment(new LoginFragment(), getActivity(), true);
                }
                //try catch nötig ???????
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }


}