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

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    String NAME;
    String PASSWORD;

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

        loginRememberMeSwitch =(Switch)view.findViewById(R.id.login_angemeldet_bleiben);
        loginRememberMeSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_button: {
                //boolean isSwitchOn = loginRememberMeSwitch.isChecked();
                //Check if internet connection is available
                if(isNetworkAvailable(getActivity())) {
                    //TODO COMPARE CREDENTIALS ONLINE
                    username = usernameTextInputEditText.getText().toString();
                    password = passwordTextInputEditText.getText().toString();
                    AsyncLogin asyncLogin = new AsyncLogin();
                    asyncLogin.execute(username,password);

                } else {
                    //TODO COMPARE WITH SHARED-PREFS
                    if (isPasswordRight(usernameTextInputEditText.getText().toString(),
                            passwordTextInputEditText.getText().toString())) {
                        MainActivity.switchFragment(new OverviewFragment(), getActivity(),true);
                    }
                }
                break;
            }
            case R.id.login_register_button: {
                MainActivity.switchFragment(new RegisterFragment(), getActivity(),true);
                break;
            }
        }
    }

    /** Check if Internet Connection is available or not
     *
     */
    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Checks if the Username and Password matches with any saved username in the Database
     *
     * @param username Username
     * @param password Password
     */
    private boolean isPasswordRight(String username, String password) {
        // TODO: delete when App is finished
        // if (true) return true;

        String savedPassword = SharedPrefsMethods.readStringFromSharedPrefs(getActivity(), username);
        if (!password.equals(savedPassword)) {
            passwordTextInputLayout.setError("Password is wrong!");
            return false;
        }
        if (!SharedPrefsMethods.containsStringInSharedPrefs(getActivity(), username)) {
            usernameTextInputLayout.setError("Username is not registered");
            return true;
        }
        return false;

    }

    class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://www.worldlustblog.de/Registration/db_fetch_user.php");
                String urlParams = "&name=" + username + "&password=" + password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
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
            String error;
            pdLoading.dismiss();
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_credentials");
                NAME = user_data.getString("name");
                PASSWORD = user_data.getString("password");
                if (PASSWORD.equals(password)  && NAME.equals(username)) {
                    MainActivity.switchFragment(new OverviewFragment(), getActivity(), true);
                }

                //try catch nötig ???????
            } catch (JSONException e) {
                e.printStackTrace();
                //error = "Exception: "+e.getMessage();
                Toast.makeText(getActivity(),"Username oder Passwort falsch!",Toast.LENGTH_LONG).show();
            }

        }
    }

}





