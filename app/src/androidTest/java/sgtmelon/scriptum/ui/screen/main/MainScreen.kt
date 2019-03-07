package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.add.AddDialogUi
import sgtmelon.scriptum.ui.screen.main.bin.BinScreen
import sgtmelon.scriptum.ui.screen.main.notes.NotesScreen
import sgtmelon.scriptum.ui.screen.main.rank.RankScreen

class MainScreen : ParentUi() {

    fun assert(func: MainAssert.() -> Unit) = MainAssert().apply { func() }
    fun addDialog(func: AddDialogUi.() -> Unit) = AddDialogUi().apply { func() }

    fun rankScreen(func: RankScreen.() -> Unit) = RankScreen().apply { func() }
    fun notesScreen(func: NotesScreen.() -> Unit) = NotesScreen().apply { func() }
    fun binScreen(func: BinScreen.() -> Unit) = BinScreen().apply { func() }

    fun navigateTo(page: MainPage.Name) = action {
        onClick(when (page) {
            MainPage.Name.RANK -> R.id.item_page_rank
            MainPage.Name.NOTES -> R.id.item_page_notes
            MainPage.Name.BIN -> R.id.item_page_bin
        })
    }

    companion object {
        operator fun invoke(func: MainScreen.() -> Unit) = MainScreen().apply { func() }
    }

}