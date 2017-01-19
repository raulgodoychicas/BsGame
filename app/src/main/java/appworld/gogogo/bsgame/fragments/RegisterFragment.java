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
    private String username;
    private String password;


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
                 username = usernameTextInputEditText.getText().toString().toLowerCase();
                 password = passwordTextInputEditText.getText().toString();
                String passwordRepeat = repeatPasswordTextInputEditText.getText().toString();

                //TODO Registration only with Internet connection !
                // if (!isUserNameAvailable(username)) {
                //    emptyAllErrorTexts();
                //   usernameTextInputLayout.setError("Username is not available");
                //}
                if (isPasswordAccordingToRules(password, passwordRepeat)) {
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

        //Loading view in UI while registration
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        HttpURLConnection httpURLConnection;
        OutputStream outputStream;
        InputStream inputStream;


        protected String doInBackground(String... params) {

            //params[0] = username, params[1] = password
            String uname = params[0];
            String pw = params[1];

            //Initializing Data String that is later needed to get the response information from the Server
            String data = "";

            //counter for while-loop
            int count;

            try {
                URL url = new URL("http://www.worldlustblog.de/Registration/register.php");
                String urlParams = "&name=" + uname + "&password=" + pw;

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

        //Run loading view on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();
    }

        protected void onPostExecute(String flag) /*Flag "1" --> User already exists, Flag = "" --> Registration was successful*/
        {
                //Dismiss loading view in UI
                pdLoading.dismiss();

                //Check if User already exists
                if (flag.equals("1")) {
                    //Toast.makeText(getActivity(),"User existiert bereits! Bitte anderen Usernamen verwenden!",Toast.LENGTH_LONG).show();
                    emptyAllErrorTexts();
                    usernameTextInputLayout.setError("Username is not available");
                } else {
                    //UI Information that registration was successfull
                    Toast.makeText(getActivity(),"Registration was successful",Toast.LENGTH_LONG).show();
                    //TODO write in SharedPrefs
                    SharedPrefsMethods.writeStringToSharedPrefs(getActivity(), username, password);

                    //Registration was successful --> Login
                    MainActivity.switchFragment(new LoginFragment(), getActivity(), true);
                }
        }
    }

}