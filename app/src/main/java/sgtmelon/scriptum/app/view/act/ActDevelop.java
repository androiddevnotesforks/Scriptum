package sgtmelon.scriptum.app.view.act;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.blank.BlankAct;

public class ActDevelop extends BlankAct {

    private static final String TAG = ActDevelop.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_develop);

        TextView noteText = findViewById(R.id.actDevelop_tv_note);
        TextView rollText = findViewById(R.id.actDevelop_tv_roll);
        TextView rankText = findViewById(R.id.actDevelop_tv_rank);

        DbRoom db = DbRoom.provideDb(this);

        db.daoNote().listAll(noteText);
        db.daoRoll().listAll(rollText);
        db.daoRank().listAll(rankText);

        db.close();

        TextView prefText = findViewById(R.id.actDevelop_tv_pref);

        Help.Pref.listAllPref(this, prefText);
    }

}
