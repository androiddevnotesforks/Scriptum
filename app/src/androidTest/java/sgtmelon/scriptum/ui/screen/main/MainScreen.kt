package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.AddDialogUi
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Класс для ui контроля экрана [MainActivity]
 *
 * @author SerjantArbuz
 */
class MainScreen : ParentUi() {

    private var wasNavigate = false

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun rankScreen(func: RankScreen.() -> Unit) = RankScreen().apply {
        wasNavigate = true

        navigateTo(MainPage.RANK)
        func()
    }
    fun notesScreen(func: NotesScreen.() -> Unit) = NotesScreen().apply {
        if (wasNavigate) navigateTo(MainPage.NOTES)
        func()
    }
    fun binScreen(func: BinScreen.() -> Unit) = BinScreen().apply {
        wasNavigate = true

        navigateTo(MainPage.BIN)
        func()
    }

    fun addDialogUi(func: AddDialogUi.() -> Unit = {}) = AddDialogUi().apply {
        action { onClick(R.id.main_add_fab) }

        onOpenSwipe()
        assert { onDisplayContent() }

        func()
    }

    fun textNoteScreen(state: State, func: TextNoteScreen.() -> Unit = {}) = TextNoteScreen().apply {
        assert { onDisplayContent(state) }
        func()
    }

    fun rollNoteScreen(state: State, func: RollNoteScreen.() -> Unit = {}) = RollNoteScreen().apply {
        assert { onDisplayContent(state) }
        func()
    }

    fun navigateTo(page: MainPage) = action {
        onClick(when (page) {
            MainPage.RANK -> R.id.item_page_rank
            MainPage.NOTES -> R.id.item_page_notes
            MainPage.BIN -> R.id.item_page_bin
        })
    }

    fun scrollTop(page: MainPage) = action {
        onLongClick(when (page) {
            MainPage.RANK -> R.id.item_page_rank
            MainPage.NOTES -> R.id.item_page_notes
            MainPage.BIN -> R.id.item_page_bin
        })
        Thread.sleep(500)
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.main_parent_container)
            onDisplay(R.id.main_toolbar_holder)
            onDisplay(R.id.main_menu_navigation)
        }

        fun onDisplayContent(page: MainPage) = when (page) {
            MainPage.RANK -> isSelected(R.id.item_page_rank)
            MainPage.NOTES -> isSelected(R.id.item_page_notes)
            MainPage.BIN -> isSelected(R.id.item_page_bin)
        }

        fun onDisplayFab(visible: Boolean) =
                if (visible) onDisplay(R.id.main_add_fab) else notDisplay(R.id.main_add_fab)

    }

    companion object {
        operator fun invoke(func: MainScreen.() -> Unit) = MainScreen().apply { func() }
    }

}