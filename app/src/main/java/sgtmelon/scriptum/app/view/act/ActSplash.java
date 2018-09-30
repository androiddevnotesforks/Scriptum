package sgtmelon.scriptum.app.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.st.StNote;

public class ActSplash extends AppCompatActivity {

    private static final String TAG = ActSplash.class.getSimpleName();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) startNormal();
        else {
            if (bundle.getString(DefIntent.STATUS_OPEN) == null) {
                startNormal();
            } else {
                Intent intentMain = new Intent(this, ActMain.class);

                StNote stNote = bundle.getParcelable(DefIntent.STATE_NOTE);

                Intent intentNote = new Intent(this, ActNote.class);
                intentNote.putExtra(DefIntent.STATE_NOTE, stNote);
                intentNote.putExtra(DefIntent.NOTE_ID, bundle.getLong(DefIntent.NOTE_ID));

                startActivities(new Intent[]{intentMain, intentNote});
            }
        }

        finish();
    }

    private void startNormal() {
        Log.i(TAG, "startNormal");

        Intent intent;
        if (Help.Pref.isFirstStart(this)) {
            intent = new Intent(this, ActIntro.class);
        } else {
            intent = new Intent(this, ActMain.class);
        }

        startActivity(intent);
    }

}
