package appworld.gogogo.bsgame;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.transition.Fade;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import appworld.gogogo.bsgame.fragments.ImpressumFragment;
import appworld.gogogo.bsgame.fragments.LoginFragment;
import appworld.gogogo.bsgame.support.SharedPrefsMethods;
import appworld.gogogo.bsgame.support.UiMethods;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        switchFragment(new LoginFragment(), this, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);

        if (id == R.id.action_settings) {
            return true;

        } else if (id == R.id.action_impressum) {
            //Überprüfen ob ImpressumFragment bereits geladen ist, um Mehrfachladen des Fragments zu verhindern
            if (!(fragment instanceof ImpressumFragment)) {
                switchFragment(new ImpressumFragment(), this, true);
            }

        } else if (id == R.id.action_clear_data) {
            //Delete all local datas from sharedPrefs
            SharedPrefsMethods.clearSharedPrefs(this);
            SharedPrefsMethods.clearRememberMeService(this);
            Toast.makeText(this, "Lokale Daten wurden gelöscht.", Toast.LENGTH_LONG).show();

        } else if (id == R.id.action_logout) {
            //clear Backstack!
            clearBackStack(this);
            switchFragment(new LoginFragment(), this, false);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void switchFragment(Fragment fragment, Activity activity, boolean addToBackstack) {
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        fragment.setEnterTransition(new Fade());
        fragmentTransaction.replace(R.id.fragment, fragment);
        if (addToBackstack) fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
        UiMethods.closeKeyboard(fragment.getView(), activity);
    }

    public static void clearBackStack(Activity activity) {
        final FragmentManager fragmentManager = activity.getFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        while (count > -1) {
            fragmentManager.popBackStackImmediate();
            count--;
        }
    }
}
