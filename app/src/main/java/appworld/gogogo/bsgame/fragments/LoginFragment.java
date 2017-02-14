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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import appworld.gogogo.bsgame.MainActivity;
import appworld.gogogo.bsgame.R;
import appworld.gogogo.bsgame.support.SharedPrefsMethods;
import appworld.gogogo.bsgame.support.UiMethods;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    public static final String USER_NAME_KEY = "userNameKey";

    private Switch loginRememberMeSwitch;
    private TextInputEditText usernameTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    String username;
    String password;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button loginButton = (Button) view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        Button registerButton = (Button) view.findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(this);

        loginRememberMeSwitch = (Switch) view.findViewById(R.id.login_angemeldet_bleiben);
        loginRememberMeSwitch.setOnClickListener(this);

        usernameTextInputLayout = (TextInputLayout) view.findViewById(R.id.login_username_textinputlayout);
        usernameTextInputLayout.setErrorEnabled(true);
        usernameTextInputEditText = (TextInputEditText) view.findViewById(R.id.login_username_textinputedittext);

        passwordTextInputLayout = (TextInputLayout) view.findViewById(R.id.login_password_textinputlayout);
        passwordTextInputLayout.setErrorEnabled(true);
        passwordTextInputEditText = (TextInputEditText) view.findViewById(R.id.login_password_textinputedittext);

        //Check if remember me feature is selected and set username for next start of the app !!
        boolean saveLogin = SharedPrefsMethods.readRememberServiceStatus(getActivity(), "serviceStatus");
        if (saveLogin) {
            String savedUsername = SharedPrefsMethods.readUsernameForRememberMeService(getActivity(), "userName");
            usernameTextInputEditText.setText(savedUsername);
            loginRememberMeSwitch.setChecked(true);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem logoutMenuItem = menu.findItem(R.id.action_logout);
        MenuItem clearDataMenuItem = menu.findItem(R.id.action_clear_data);
        logoutMenuItem.setVisible(false);
        clearDataMenuItem.setVisible(false);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_button: {
                emptyAllErrorTexts();
                UiMethods.closeKeyboard(getView(), getActivity());

                //get inputs from User, Username(lower case, so login is CaseInsensitive) and password and delete spaces at the beginning and end
                username = usernameTextInputEditText.getText().toString().toLowerCase().trim();
                password = passwordTextInputEditText.getText().toString().trim();

                //check if Username Field is empty
                if (isUsernameEmpty(username)) {
                    //check if Password Field is empty
                    if (isPasswordEmpty(password)) {
                        //if Switch is on, remember Switch-State and Write Username To SharedPrefs
                        if (loginRememberMeSwitch.isChecked()) {

                            SharedPrefsMethods.writeRememberMeServiceStateToSharedPrefs(getActivity(), true);
                            SharedPrefsMethods.writeUsernameToSharedPrefs(getActivity(), username);
                        } else {
                            SharedPrefsMethods.clearRememberMeService(getActivity());
                        }
                        //Check if internet connection is available
                        if (isNetworkAvailable(getActivity())) {
                            //execute AsyncTask in Background and commit inputs from User to the AsyncTask to compare User Credentials with Server
                            AsyncLogin asyncLogin = new AsyncLogin();
                            asyncLogin.execute(username, password);
                        } else {
                            //If there is no internet connection compare User credentials with SharedPrefs
                            if (isPasswordRight(username, password)) {
                                MainActivity.switchFragment(new OverviewFragment(), getActivity(), false);
                                SharedPrefsMethods.writeStringToSharedPrefs(getActivity(), USER_NAME_KEY, username);
                            }
                        }
                    }
                }
                break;
            }
            case R.id.login_register_button: {
                //hide keyboard
                UiMethods.closeKeyboard(getView(), getActivity());
                //switch to RegisterFragment
                MainActivity.switchFragment(new RegisterFragment(), getActivity(), true);
                break;
            }
        }
    }

    private boolean isPasswordEmpty(String passwordString) {
        if (passwordString.equals("")) {
            emptyAllErrorTexts();
            passwordTextInputLayout.setError("Pflichtfeld!");
            return false;
        }
        return true;
    }

    private boolean isUsernameEmpty(String usernameString) {
        if (usernameString.equals("")) {
            emptyAllErrorTexts();
            usernameTextInputLayout.setError("Pflichtfeld!");
            return false;
        }
        return true;
    }

    /**
     * Check if Internet Connection is available or not
     */
    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Checks if the Username and Password matches with any saved username in the Local Database
     *
     * @param username Username
     * @param password Password
     */
    private boolean isPasswordRight(String username, String password) {

        String savedPassword = decodePassword(SharedPrefsMethods.readStringFromSharedPrefs(getActivity(), username));

        if (!SharedPrefsMethods.containsStringInSharedPrefs(getActivity(), username)) {
            passwordTextInputLayout.setError("Username oder Passwort falsch!");
            return false;
        }

        if (!password.equals(savedPassword)) {
            passwordTextInputLayout.setError("Username oder Passwort falsch!");
            return false;
        }
        return true;
    }

    private void emptyAllErrorTexts() {
        passwordTextInputLayout.setError("");
        usernameTextInputLayout.setError("");
    }

    //encode password
    public String encodePasswordString(String password) {
        byte[] encodePassword = new byte[0];
        try {
            encodePassword = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(encodePassword, Base64.DEFAULT);
    }

    //Decode local saved password
    public String decodePassword(String password) {
        String decodedPasswordString = "";
        byte[] decodePassword = Base64.decode(password, Base64.DEFAULT);
        try {
            decodedPasswordString = new String(decodePassword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedPasswordString;
    }

    /**
     * Checks if the Username and Password matches with any saved username in the online Mysql-Database
     */
    class AsyncLogin extends AsyncTask<String, String, String> {

        //Loading Window in UI while loggin in
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        OutputStream outputStream;
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        String uname;
        String pw;

        protected String doInBackground(String... params) {

            //params[0] = username, params[1] = password
            uname = params[0];
            pw = params[1];

            //Initializing Data String that is later needed to get the response JSON String from the Mysql-DB
            String data = "";

            //counter for while-loop
            int count;

            try {

                URL url = new URL("http://www.worldlustblog.de/Registration/db_fetch_user.php");
                String urlParams = "&name=" + uname + "&password=" + encodePasswordString(pw);

                //connect to server and send Credentials to Server
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(urlParams.getBytes());
                outputStream.flush();
                outputStream.close();

                //get Input from Server (JSON coded array)
                inputStream = httpURLConnection.getInputStream();

                //read Inputstream from Server
                while ((count = inputStream.read()) != -1) {
                    data += (char) count;
                }
                //close connection
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

        protected void onPostExecute(String data) /*String data is for the JSON array we get from the DB*/ {

            //Dismiss loading view in UI
            pdLoading.dismiss();

            try {
                String name;
                String pw;
                //check if Database Connection Failed
                if (data.equals("Verbindungsfehler")) {
                    Toast.makeText(getActivity(), "Verbindung zur Datenbank fehlgeschlagen!", Toast.LENGTH_LONG).show();
                } else {
                    //Pass String data into JSONObjec root
                    JSONObject root = new JSONObject(data);

                    //get user_credentials, which are sent by the Server
                    JSONObject user_data = root.getJSONObject("user_credentials");

                    //extract name and pw from user_credentials as a string
                    name = user_data.getString("name");
                    pw = user_data.getString("password");

                    //decode  retrived Password
                    String decodedPasswordString = "";
                    if (!pw.equals("2")) {
                        decodedPasswordString = decodePassword(pw);
                    }

                    //If credentials are correct --> Login
                    if (!(decodedPasswordString.equals(password) || name.equals(username))) {
                        passwordTextInputLayout.setError("Username oder Passwort falsch!");
                    } else {
                        SharedPrefsMethods.writeStringToSharedPrefs(getActivity(), USER_NAME_KEY, username);
                        MainActivity.switchFragment(new OverviewFragment(), getActivity(), false);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //Error message if Database connection was successful, but retriving Data from Server failed
                Toast.makeText(getActivity(), "Serverproblem! Bitte versuchen Sie es später wieder.", Toast.LENGTH_LONG).show();
            }

        }

    }
}







