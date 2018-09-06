package sgtmelon.handynotes.app.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.DefNote;

public class ActSplash extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final String TAG = "ActSplash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.i(TAG, "onCreate: bundle == null");

            Intent intent;
            if (Help.Pref.isFirstStart(this)) {
                intent = new Intent(this, ActIntro.class);
            } else {
                intent = new Intent(this, ActMain.class);
            }

            startActivity(intent);
        } else {
            Log.i(TAG, "onCreate: bundle != null");

            Intent intentMain = new Intent(this, ActMain.class);

            Intent intentNote = new Intent(this, ActNote.class);
            intentNote.putExtra(DefNote.CREATE, false);
            intentNote.putExtra(DefNote.ID, bundle.getLong(DefNote.ID));

            startActivities(new Intent[]{intentMain, intentNote});
        }

        finish();
    }

}
