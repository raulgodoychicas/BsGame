package appworld.gogogo.bsgame.support;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import appworld.gogogo.bsgame.R;

/**
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
        SharedPreferences sharedPrefs = activity.getSharedPreferences(SHAREDPREFS_FILE_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.remove(SHAREDPREFS_FILE_KEY);
        editor.apply();
        editor.commit();
        }

    }



