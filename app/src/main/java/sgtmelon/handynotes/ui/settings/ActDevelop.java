package sgtmelon.handynotes.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.database.NoteDB;
import sgtmelon.handynotes.service.Help;

public class ActDevelop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_develop);
        Log.i("ActDevelop", "onCreate");

        TextView noteText = findViewById(R.id.tView_actDevelop_notes);
        TextView rollText = findViewById(R.id.tView_actDevelop_rolls);
        TextView rankText = findViewById(R.id.tView_actDevelop_ranks);

        NoteDB noteDB = new NoteDB(this);
        noteDB.listAllNote(noteText);
        noteDB.listAllRoll(rollText);
        noteDB.listAllRank(rankText);
        noteDB.close();

        TextView prefText = findViewById(R.id.tView_actDevelop_pref);

        Help.Pref.listAllPref(this, prefText);
    }
}
