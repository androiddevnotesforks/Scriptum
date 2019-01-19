package sgtmelon.scriptum.app.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.view.parent.BaseActivityParent;
import sgtmelon.scriptum.office.utils.PrefUtils;

public final class DevelopActivity extends BaseActivityParent {

    private static final String TAG = DevelopActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop);

        final TextView noteText = findViewById(R.id.note_text);
        final TextView rollText = findViewById(R.id.roll_text);
        final TextView rankText = findViewById(R.id.rank_text);

        final RoomDb db = RoomDb.provideDb(this);
        db.daoNote().listAll(noteText);
        db.daoRoll().listAll(rollText);
        db.daoRank().listAll(rankText);
        db.close();

        final TextView prefText = findViewById(R.id.preference_text);
        PrefUtils.getInstance(this).listAllPref(prefText);
    }

}
