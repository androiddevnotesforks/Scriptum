package sgtmelon.scriptum.factory

import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.ui.main.RankFragment

/**
 * Factory for create/get fragments
 *
 * @author SerjantArbuz
 */
class FragmentFactory(private val fm: FragmentManager?) {

    fun getRankFragment(): RankFragment =
            fm?.findFragmentByTag(MainPage.RANK.name) as? RankFragment ?: RankFragment()

    fun getNotesFragment(): NotesFragment =
            fm?.findFragmentByTag(MainPage.NOTES.name) as? NotesFragment ?: NotesFragment()

    fun getBinFragment(): BinFragment =
            fm?.findFragmentByTag(MainPage.BIN.name) as? BinFragment ?: BinFragment()

}