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
import android.widget.Switch;
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
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button loginButton;
    private Button registerButton;
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

        registerButton = (Button) view.findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(this);

        usernameTextInputLayout = (TextInputLayout) view.findViewById(R.id.login_username_textinputlayout);
        usernameTextInputLayout.setErrorEnabled(true);
        usernameTextInputEditText = (TextInputEditText) view.findViewById(R.id.login_username_textinputedittext);

        passwordTextInputLayout = (TextInputLayout) view.findViewById(R.id.login_password_textinputlayout);
        passwordTextInputLayout.setErrorEnabled(true);
        passwordTextInputEditText = (TextInputEditText) view.findViewById(R.id.login_password_textinputedittext);

        loginRememberMeSwitch = (Switch) view.findViewById(R.id.login_angemeldet_bleiben);
        loginRememberMeSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_button: {

                emptyAllErrorTexts();

                //get inputs from User , Username(Username converted to lower case, so log-in is CaseInsensitive) and Password
                username = usernameTextInputEditText.getText().toString().toLowerCase();
                password = passwordTextInputEditText.getText().toString();

                //Check if internet connection is available
                if (isNetworkAvailable(getActivity())) {

                    //execute AsyncTask in Background and commit inputs from User to the AsyncTask to compare User Credentials with Server
                    AsyncLogin asyncLogin = new AsyncLogin();
                    asyncLogin.execute(username, password);

                } else {
                    //If there is no internet connection compare User credentials with SharedPrefs
                    if (isPasswordRight(username, password)) {
                        MainActivity.switchFragment(new OverviewFragment(), getActivity(), false);
                    }
                }
                break;
            }
            case R.id.login_register_button: {
                MainActivity.switchFragment(new RegisterFragment(), getActivity(), false);
                break;
            }
        }
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

        String savedPassword = SharedPrefsMethods.readStringFromSharedPrefs(getActivity(), username);

        if (!SharedPrefsMethods.containsStringInSharedPrefs(getActivity(), username)) {
            passwordTextInputLayout.setError("Username or Password wrong");
            return false;
        }

        if (!password.equals(savedPassword)) {
            passwordTextInputLayout.setError("Username or Password wrong");
            return false;
        }
        return true;
    }

    private void emptyAllErrorTexts() {
        passwordTextInputLayout.setError("");
        usernameTextInputLayout.setError("");
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

        protected String doInBackground(String... params) {

            //params[0] = username, params[1] = password
            String uname = params[0];
            String pw = params[1];

            //Initializing Data String that is later needed to get the response JSON String from the Mysql-DB
            String data = "";

            //counter for while-loop
            int count;

            try {
                URL url = new URL("http://www.worldlustblog.de/Registration/db_fetch_user.php");
                String urlParams = "&name=" + uname + "&password=" + pw;

                httpURLConnection = (HttpURLConnection) url.openConnection();
                //httpURLConnection.setConnectTimeout(6000);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(urlParams.getBytes());
                outputStream.flush();
                outputStream.close();

                //get Input from Server (JSON String)
                inputStream = httpURLConnection.getInputStream();

                //Convert the Bytes from Server into a String
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

        protected void onPostExecute(String data) /*String data is for the array we get from the DB*/ {

            //Dismiss loading view in UI
            pdLoading.dismiss();

            try {
                String NAME;
                String PW;

                //Pass String s into JSONObjec root
                JSONObject root = new JSONObject(data);

                //get user_credentials, which are sent by the Server
                JSONObject user_data = root.getJSONObject("user_credentials");

                //retrive NAME and PW from user_credentials as a string
                NAME = user_data.getString("name");
                PW = user_data.getString("password");

                //If credentials are correct --> Login
                if (!(PW.equals(password) || NAME.equals(username))) {
                    passwordTextInputLayout.setError("Username or Password wrong");
                } else {
                    MainActivity.switchFragment(new OverviewFragment(), getActivity(), false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Serverproblems!", Toast.LENGTH_LONG).show();
            }

        }

    }

}







