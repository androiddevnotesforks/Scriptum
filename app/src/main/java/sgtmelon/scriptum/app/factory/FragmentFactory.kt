package sgtmelon.scriptum.app.factory

import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.app.screen.view.main.BinFragment
import sgtmelon.scriptum.app.screen.view.main.NotesFragment
import sgtmelon.scriptum.app.screen.view.main.RankFragment

object FragmentFactory {

    fun getRankFragment(fm: FragmentManager?): RankFragment =
            fm?.findFragmentByTag(MainPage.Name.RANK.name) as RankFragment? ?: RankFragment()

    fun getNotesFragment(fm: FragmentManager?): NotesFragment =
            fm?.findFragmentByTag(MainPage.Name.NOTES.name) as NotesFragment? ?: NotesFragment()

    fun getBinFragment(fm: FragmentManager?): BinFragment =
            fm?.findFragmentByTag(MainPage.Name.BIN.name) as BinFragment? ?: BinFragment()

}