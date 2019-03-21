package sgtmelon.scriptum.app.screen.develop

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.repository.DeveloperRepo
import sgtmelon.scriptum.app.screen.parent.ParentActivity
import sgtmelon.scriptum.office.utils.Preference

class DevelopActivity : ParentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop)

        DeveloperRepo.getInstance(this).apply {
            listNoteTable(findViewById(R.id.note_text))
            listRollTable(findViewById(R.id.roll_text))
            listRankTable(findViewById(R.id.rank_text))
        }

        Preference(context = this).listAllPref(findViewById(R.id.preference_text))
    }

}
