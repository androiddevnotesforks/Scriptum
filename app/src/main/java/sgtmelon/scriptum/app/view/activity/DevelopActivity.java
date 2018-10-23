package sgtmelon.scriptum.app.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.view.parent.ActivityParent;
import sgtmelon.scriptum.office.Help;

public final class DevelopActivity extends ActivityParent {

    private static final String TAG = DevelopActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop);

        TextView noteText = findViewById(R.id.note_text);
        TextView rollText = findViewById(R.id.roll_text);
        TextView rankText = findViewById(R.id.rank_text);

        RoomDb db = RoomDb.provideDb(this);

        db.daoNote().listAll(noteText);
        db.daoRoll().listAll(rollText);
        db.daoRank().listAll(rankText);

        db.close();

        TextView prefText = findViewById(R.id.preference_text);

        Help.Pref.listAllPref(this, prefText);
    }

}