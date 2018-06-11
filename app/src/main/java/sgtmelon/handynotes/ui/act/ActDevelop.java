package sgtmelon.handynotes.ui.act;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.db.DbRoom;
import sgtmelon.handynotes.Help;

public class ActDevelop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_develop);
        Log.i("ActDevelop", "onCreate");

        TextView noteText = findViewById(R.id.actDevelop_tv_note);
        TextView rollText = findViewById(R.id.actDevelop_tv_roll);
        TextView rankText = findViewById(R.id.actDevelop_tv_rank);

        DbRoom db = Room.databaseBuilder(this, DbRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        db.daoNote().listAllNote(noteText);
        db.daoRoll().listAllRoll(rollText);
        db.daoRank().listAllRank(rankText);

        db.close();

        TextView prefText = findViewById(R.id.actDevelop_tv_pref);

        Help.Pref.listAllPref(this, prefText);
    }
}
