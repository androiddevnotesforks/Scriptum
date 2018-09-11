package sgtmelon.scriptum.app.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefNote;

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
        if (bundle == null) startNormal();
        else {
            if (bundle.getString(DefNote.OPEN) == null) startNormal();
            else {
                Intent intentMain = new Intent(this, ActMain.class);

                Intent intentNote = new Intent(this, ActNote.class);
                intentNote.putExtra(DefNote.CREATE, false);
                intentNote.putExtra(DefNote.ID, bundle.getLong(DefNote.ID));

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
