package sgtmelon.scriptum.app.view.activity

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.view.parent.BaseActivityParent
import sgtmelon.scriptum.office.utils.PrefUtils

class DevelopActivity : BaseActivityParent() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop)

        val db = RoomDb.provideDb(this)
        db.daoNote().listAll(findViewById(R.id.note_text))
        db.daoRoll().listAll(findViewById(R.id.roll_text))
        db.daoRank().listAll(findViewById(R.id.rank_text))
        db.close()

        PrefUtils(context = this).listAllPref(findViewById(R.id.preference_text))
    }

}
