package sgtmelon.scriptum.parent.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.item.NoteItemUi
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.parent.ui.feature.OpenNote
import sgtmelon.scriptum.parent.ui.feature.OpenNoteDialog
import sgtmelon.scriptum.parent.ui.model.key.InfoCase
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.scriptum.parent.ui.parts.info.InfoContainerPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.parent.ui.parts.toolbar.TitleToolbarPart
import sgtmelon.scriptum.parent.ui.parts.toolbar.ToolbarItem
import sgtmelon.scriptum.parent.ui.screen.dialogs.message.ClearDialogUi
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of [BinFragment].
 */
class BinScreen : ContainerPart(TestViewTag.BIN),
    RecyclerPart<NoteItem, NoteItemUi>,
    OpenNote,
    OpenNoteDialog {

    //region Views

    private val toolbar = TitleToolbarPart(parentContainer, R.string.title_bin, withBack = false)
    private val clearItem = ToolbarItem(
        R.id.item_clear, R.drawable.ic_clear, R.string.menu_clear_bin
    )

    override val recyclerView = getView(R.id.recycler_view)

    private val infoContainer = InfoContainerPart(parentContainer, InfoCase.Bin)

    override fun getItem(p: Int) = NoteItemUi(recyclerView, p)

    override val openNoteState: NoteState = NoteState.BIN

    //endregion

    fun openClearDialog(func: ClearDialogUi.() -> Unit = {}) {
        clearItem.click()
        ClearDialogUi(func)
    }

    fun assert(isEmpty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar.assert()
        if (!isEmpty) {
            toolbar.assertItem(clearItem)
        }
        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    companion object {
        inline operator fun invoke(func: BinScreen.() -> Unit, isEmpty: Boolean): BinScreen {
            return BinScreen().assert(isEmpty).apply(func)
        }
    }
}