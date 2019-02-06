package sgtmelon.scriptum.app.factory

import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.app.view.fragment.BinFragment
import sgtmelon.scriptum.app.view.fragment.NotesFragment
import sgtmelon.scriptum.app.view.fragment.RankFragment
import sgtmelon.scriptum.office.annot.def.FragmentDef

object FragmentFactory {

    fun getRankFragment(fm: FragmentManager?): RankFragment =
            fm?.findFragmentByTag(FragmentDef.RANK) as RankFragment? ?: RankFragment()

    fun getNotesFragment(fm: FragmentManager?): NotesFragment =
            fm?.findFragmentByTag(FragmentDef.NOTES) as NotesFragment? ?: NotesFragment()

    fun getBinFragment(fm: FragmentManager?): BinFragment =
            fm?.findFragmentByTag(FragmentDef.BIN) as BinFragment? ?: BinFragment()

}