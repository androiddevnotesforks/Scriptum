package sgtmelon.scriptum.parent.ui.screen.note

import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.testData.DbDelegator
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.parent.ui.basic.withBackgroundAppColor
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withSizeAttr

/**
 * Class for UI control of [NoteActivity].
 */
class NoteScreen : ContainerPart(TestViewTag.NOTE) {

    private val toolbarHolder = getView(R.id.toolbar_holder)
    private val panelHolder = getView(R.id.panel_holder)
    private val fragmentContainer = getView(R.id.fragment_container)

    inline fun openText(
        func: TextNoteScreen.() -> Unit,
        state: NoteState,
        item: NoteItem.Text,
        isRankEmpty: Boolean
    ) {
        beforeOpen(state, item)
        TextNoteScreen(func, state, item, isRankEmpty)
    }

    inline fun openRoll(
        func: RollNoteScreen.() -> Unit,
        state: NoteState,
        item: NoteItem.Roll,
        isRankEmpty: Boolean
    ) {
        beforeOpen(state, item)
        RollNoteScreen(func, state, item, isRankEmpty)
    }

    fun beforeOpen(state: NoteState, item: NoteItem) {
        assert(item)

        /**
         * Was assertion error in tests where difference between times 1 minute. I think it was
         * happened when calendar time was around ~00:59 on note creation inside [DbDelegator].
         * But the time of actual creation was ~01:.. (after [DbDelegator] note was created).
         */
        if (state == NoteState.NEW) {
            item.create = getCalendarText()
        }
    }

    fun assert(item: NoteItem) {
        toolbarHolder.withBackgroundAppColor(theme, item.color, needDark = false)
            .withSizeAttr(heightAttr = android.R.attr.actionBarSize)
        panelHolder.withBackgroundAttr(R.attr.clPrimary)
            .withSize(heightId = R.dimen.note_panel_height)

        fragmentContainer.isDisplayed()
    }
}