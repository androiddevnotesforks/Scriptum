package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.AddDialogUi
import sgtmelon.scriptum.ui.screen.main.bin.BinScreen
import sgtmelon.scriptum.ui.screen.main.notes.NotesScreen
import sgtmelon.scriptum.ui.screen.main.rank.RankScreen

class MainScreen : ParentUi() {

    companion object {
        operator fun invoke(func: MainScreen.() -> Unit) = MainScreen().apply { func() }
    }

    fun assert(func: MainAssert.() -> Unit) = MainAssert().apply { func() }

    fun addDialog(func: AddDialogUi.() -> Unit) = AddDialogUi().apply { func() }

    fun rankScreen(func: RankScreen.() -> Unit) = RankScreen().apply { func() }
    fun notesScreen(func: NotesScreen.() -> Unit) = NotesScreen().apply { func() }
    fun binScreen(func: BinScreen.() -> Unit) = BinScreen().apply { func() }

    fun navigateTo(page: PAGE) = action {
        onClick(when (page) {
            PAGE.RANK -> R.id.item_page_rank
            PAGE.NOTES -> R.id.item_page_notes
            PAGE.BIN -> R.id.item_page_bin
        })
    }

}