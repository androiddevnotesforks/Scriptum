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
object FragmentFactory {

    fun getRankFragment(fm: FragmentManager?): RankFragment =
            fm?.findFragmentByTag(MainPage.RANK.name) as RankFragment? ?: RankFragment()

    fun getNotesFragment(fm: FragmentManager?): NotesFragment =
            fm?.findFragmentByTag(MainPage.NOTES.name) as NotesFragment? ?: NotesFragment()

    fun getBinFragment(fm: FragmentManager?): BinFragment =
            fm?.findFragmentByTag(MainPage.BIN.name) as BinFragment? ?: BinFragment()

}