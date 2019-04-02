package sgtmelon.scriptum.app.screen.develop

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.repository.DevelopRepo
import sgtmelon.scriptum.app.screen.app.AppActivity
import sgtmelon.scriptum.office.utils.Preference

class DevelopActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop)

        DevelopRepo.getInstance(context = this).apply {
            listNoteTable(findViewById(R.id.note_text))
            listRollTable(findViewById(R.id.roll_text))
            listRankTable(findViewById(R.id.rank_text))
        }

        Preference(context = this).listAllPref(findViewById(R.id.preference_text))
    }

}
