package appworld.gogogo.bsgame.support;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import appworld.gogogo.bsgame.R;

/**
 * Created by Raul on 23.09.2016.
 */

public class AppData {

    public static String SHAREDPREFS_FILE_KEY = "sharedPrefs";

    public static void writeStringToSharedPrefs(Activity activity, String keyValueString, String string) {
        SharedPreferences sharedPref = activity.getSharedPreferences(SHAREDPREFS_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(keyValueString, string);
        editor.apply();
        editor.commit();
    }

    public static String readStringFromSharedPrefs(Activity activity, String keyValueString) {
        SharedPreferences sharedPref = activity.getSharedPreferences(SHAREDPREFS_FILE_KEY, Context.MODE_PRIVATE);
        String defaultValue = "no string";
        return sharedPref.getString(keyValueString, defaultValue);
    }

}
