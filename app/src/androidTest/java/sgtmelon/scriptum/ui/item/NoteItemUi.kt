package sgtmelon.scriptum.ui.item

import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Matcher
import sgtmelon.extension.formatPast
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.basic.extension.haveText
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.withColorIndicator
import sgtmelon.scriptum.basic.extension.withDrawableAttr
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.ui.ParentRecyclerItem

/**
 * Class for UI control of [NoteAdapter].
 */
class NoteItemUi(listMatcher: Matcher<View>, p: Int) :
        ParentRecyclerItem<NoteItem>(listMatcher, p) {

    override fun assert(model: NoteItem) = when (model.type) {
        NoteType.TEXT -> Text().assert(model)
        NoteType.ROLL -> Roll().assert(model)
    }

    private inner class Text : Parent(NoteType.TEXT) {

        val contentText = getChild(getViewById(R.id.note_text_content_text))

        override fun assert(model: NoteItem) {
            super.assert(model)

            contentText.haveText(model.text)
        }

    }

    private inner class Roll : Parent(NoteType.ROLL) {

        fun getRow(p: Int) = Row(when (p) {
            0 -> R.id.note_roll_row0_container
            1 -> R.id.note_roll_row1_container
            2 -> R.id.note_roll_row2_container
            else -> R.id.note_roll_row3_container
        })

        override fun assert(model: NoteItem) {
            super.assert(model)

            (0 until 4).forEach { getRow(it).assert(model.rollList.getOrNull(it)) }
        }

        inner class Row(@IdRes parentId: Int) {
            val parentContainer = getChild(getViewById(parentId))

            val checkImage = getChild(
                    getViewById(R.id.note_roll_check_image).includeParent(getViewById(parentId))
            )

            val contentText = getChild(
                    getViewById(R.id.note_roll_content_text).includeParent(getViewById(parentId))
            )

            fun assert(model: RollItem?) {
                parentContainer.isDisplayed(visible = model != null)

                if (model == null) return

                checkImage.isDisplayed().withDrawableAttr(
                        if (model.isCheck) {
                            R.drawable.ic_check_done
                        } else {
                            R.drawable.ic_check_outline
                        }, R.attr.clContent
                )

                contentText.isDisplayed().haveText(model.text)
            }
        }

    }

    private open inner class Parent(type: NoteType) {

        val parentCard = getChild(getViewById(when (type) {
            NoteType.TEXT -> R.id.note_text_parent_card
            NoteType.ROLL -> R.id.note_roll_parent_card
        }))

        val clickContainer = getChild(getViewById(when (type) {
            NoteType.TEXT -> R.id.note_text_click_container
            NoteType.ROLL -> R.id.note_roll_click_container
        }))

        val nameText = getChild(getViewById(when (type) {
            NoteType.TEXT -> R.id.note_text_name_text
            NoteType.ROLL -> R.id.note_roll_name_text
        }))

        val infoLayout = Info()

        val colorView = getChild(getViewById(when (type) {
            NoteType.TEXT -> R.id.note_text_color_view
            NoteType.ROLL -> R.id.note_roll_color_view
        }))

        /**
         * TODO #TEST assert card background color
         */
        open fun assert(model: NoteItem) {
            parentCard.isDisplayed()
            clickContainer.isDisplayed()

            val name = model.name
            nameText.isDisplayed(name.isNotEmpty()).haveText(name)

            infoLayout.assert(model)

            colorView.isDisplayed(visible = theme == Theme.DARK).apply{
                if (theme == Theme.DARK) withColorIndicator(
                        R.drawable.ic_color_indicator, theme, model.color
                )
            }
        }

        inner class Info {
            private val parentContainer = getChild(getViewById(R.id.note_info_container))

            private val notificationImage = getChild(getViewById(R.id.note_info_notification_image))
            private val bindImage = getChild(getViewById(R.id.note_info_bind_image))
            private val rankImage = getChild(getViewById(R.id.note_info_rank_image))

            private val progressText = getChild(getViewById(R.id.note_info_progress_text))

            private val changeText = getChild(getViewById(R.id.note_info_change_text))
            private val createText = getChild(getViewById(R.id.note_info_create_text))

            fun assert(model: NoteItem) {
                val type = model.type

                parentContainer.isDisplayed()

                notificationImage.isDisplayed(model.haveAlarm()).withDrawableAttr(
                        R.drawable.ic_notifications, R.attr.clNoteIndicator
                )

                bindImage.isDisplayed(model.isStatus).withDrawableAttr(when (type) {
                    NoteType.TEXT -> R.drawable.ic_bind_text
                    NoteType.ROLL -> R.drawable.ic_bind_roll
                }, R.attr.clNoteIndicator)

                rankImage.isDisplayed(model.haveRank()).withDrawableAttr(
                        R.drawable.ic_rank, R.attr.clNoteIndicator
                )

                val visible = type == NoteType.ROLL
                progressText.isDisplayed(visible).apply {
                    if (visible) haveText(model.text)
                }

                changeText.isDisplayed().haveText(model.change.getCalendar().formatPast())
                createText.isDisplayed().haveText(model.create.getCalendar().formatPast())
            }
        }

    }

}