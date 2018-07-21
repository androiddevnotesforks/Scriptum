package sgtmelon.handynotes.office.blank;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import sgtmelon.handynotes.R;

public class BlankAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(this);
        int valTheme = pref.getInt(getString(R.string.pref_key_theme), getResources().getInteger(R.integer.pref_default_theme));
        switch (valTheme){
            case 0:
                setTheme(R.style.App_Light);
                break;
            case 1:
                setTheme(R.style.App_Dark);
                break;
        }
    }
}
