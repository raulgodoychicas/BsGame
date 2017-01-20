package appworld.gogogo.bsgame.support;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

import appworld.gogogo.bsgame.R;

/**
 * Created by Raul on 23.09.2016.
 * <p>
 * This class contains all Methods for the Shared Preferences Methods used in the App
 * <p>
 * Shared Preferences are use to save diverse Settings from the App
 */

public class SharedPrefsMethods {

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

    public static Boolean containsStringInSharedPrefs(Activity activity, String keyValueString) {
        SharedPreferences sharedPref = activity.getSharedPreferences(SHAREDPREFS_FILE_KEY, Context.MODE_PRIVATE);
        return sharedPref.contains(keyValueString);
    }

    public static String readGameModeFromSharedPrefs(Activity activity, String m_gameModeValue){
        SharedPreferences sharedPref = activity.getSharedPreferences(SHAREDPREFS_FILE_KEY,Context.MODE_PRIVATE);
        String defaulValue = "Zero";
        return sharedPref.getString(m_gameModeValue,defaulValue);
    }

    public static void clearSharedPrefs(Activity activity){
       //final String FILENAME = "sharedPrefs";
       //File sharedPrefsFile = new File(activity.getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/" + FILENAME +".xml");
       //sharedPrefsFile.delete();

        SharedPreferences sharedPrefs = activity.getSharedPreferences(SHAREDPREFS_FILE_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.remove(SHAREDPREFS_FILE_KEY);
        editor.commit();
        }

    }



