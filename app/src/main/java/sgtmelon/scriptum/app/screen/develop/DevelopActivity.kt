package sgtmelon.scriptum.app.screen.develop

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.screen.parent.ParentActivity
import sgtmelon.scriptum.office.utils.Preference

class DevelopActivity : ParentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop)

        val db = RoomDb.getInstance(context = this)
        db.daoNote().listAll(findViewById(R.id.note_text))
        db.daoRoll().listAll(findViewById(R.id.roll_text))
        db.daoRank().listAll(findViewById(R.id.rank_text))
        db.close()

        Preference(context = this).listAllPref(findViewById(R.id.preference_text))
    }

}
