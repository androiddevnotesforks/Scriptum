package sgtmelon.scriptum.app.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.IntentDef;

public final class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean(IntentDef.STATUS_OPEN)) {
            Intent intentMain = new Intent(this, MainActivity.class);

            Intent intentNote = new Intent(this, NoteActivity.class);
            intentNote.putExtra(IntentDef.NOTE_CREATE, false);
            intentNote.putExtra(IntentDef.NOTE_ID, bundle.getLong(IntentDef.NOTE_ID));

            startActivities(new Intent[]{intentMain, intentNote});
        } else {
            startNormal();
        }

        finish();
    }

    private void startNormal() {
        Log.i(TAG, "startNormal");

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean firstStart = pref.getBoolean(getString(R.string.pref_first_start),
                getResources().getBoolean(R.bool.pref_first_start_default));

        if (firstStart) {
            pref.edit().putBoolean(getString(R.string.pref_first_start), false).apply();
        }

        Intent intent = new Intent(this, firstStart
                ? IntroActivity.class
                : MainActivity.class);

        startActivity(intent);
    }

}
