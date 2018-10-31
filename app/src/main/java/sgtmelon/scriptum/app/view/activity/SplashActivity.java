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

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean(IntentDef.STATUS_OPEN)) {
            final Intent intentMain = new Intent(this, MainActivity.class);

            final long id = bundle.getLong(IntentDef.NOTE_ID);
            final Intent intentNote = NoteActivity.getIntent(this, id);

            startActivities(new Intent[]{intentMain, intentNote});
        } else {
            startNormal();
        }

        finish();
    }

    private void startNormal() {
        Log.i(TAG, "startNormal");

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        final boolean firstStart = pref.getBoolean(getString(R.string.pref_first_start),
                getResources().getBoolean(R.bool.pref_first_start_default));

        if (firstStart) {
            pref.edit().putBoolean(getString(R.string.pref_first_start), false).apply();
        }

        final Intent intent = new Intent(this, firstStart
                ? IntroActivity.class
                : MainActivity.class);

        startActivity(intent);
    }

}
