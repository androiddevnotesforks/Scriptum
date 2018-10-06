package sgtmelon.scriptum.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import sgtmelon.scriptum.office.Help;
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
        if (bundle != null) {
            if (bundle.getBoolean(IntentDef.STATUS_OPEN)) {
                Intent intentMain = new Intent(this, MainActivity.class);

                Intent intentNote = new Intent(this, NoteActivity.class);
                intentNote.putExtra(IntentDef.NOTE_CREATE, false);
                intentNote.putExtra(IntentDef.NOTE_ID, bundle.getLong(IntentDef.NOTE_ID));

                startActivities(new Intent[]{intentMain, intentNote});
            } else {
                startNormal();
            }
        } else {
            startNormal();
        }

        finish();
    }

    private void startNormal() {
        Log.i(TAG, "startNormal");

        Intent intent;
        if (Help.Pref.isFirstStart(this)) {
            intent = new Intent(this, IntroActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
    }

}
