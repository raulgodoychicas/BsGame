package appworld.gogogo.bsgame.support;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UiMethods {

    public static void closeKeyboard(View view, Context context) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
