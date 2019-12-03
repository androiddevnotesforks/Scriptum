package sgtmelon.scriptum.ui.item

import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Matcher
import sgtmelon.extension.formatPast
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.basic.extension.*
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

    override fun assert(item: NoteItem) = when (item.type) {
        NoteType.TEXT -> Text().assert(item)
        NoteType.ROLL -> Roll().assert(item)
    }

    private inner class Text : Parent(NoteType.TEXT) {

        val contentText = getChild(getViewById(R.id.note_text_content_text))

        override fun assert(item: NoteItem) {
            super.assert(item)

            contentText.isDisplayed().withText(item.text, R.attr.clContent, R.dimen.text_16sp)
        }

    }

    private inner class Roll : Parent(NoteType.ROLL) {

        fun getRow(p: Int) = Row(when (p) {
            0 -> R.id.note_roll_row0_container
            1 -> R.id.note_roll_row1_container
            2 -> R.id.note_roll_row2_container
            else -> R.id.note_roll_row3_container
        })

        override fun assert(item: NoteItem) {
            super.assert(item)

            (0 until 4).forEach { getRow(it).assert(item.rollList.getOrNull(it)) }
        }

        inner class Row(@IdRes parentId: Int) {
            val parentContainer = getChild(getViewById(parentId))

            val checkImage = getChild(
                    getViewById(R.id.note_roll_check_image).includeParent(getViewById(parentId))
            )

            val contentText = getChild(
                    getViewById(R.id.note_roll_content_text).includeParent(getViewById(parentId))
            )

            fun assert(item: RollItem?) {
                parentContainer.isDisplayed(visible = item != null)

                if (item == null) return

                checkImage.isDisplayed()
                        .withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                        .withDrawableAttr(when (item.isCheck) {
                            true -> R.drawable.ic_check_done
                            false -> R.drawable.ic_check_outline
                        }, R.attr.clContent)

                contentText.isDisplayed().withText(item.text, R.attr.clContent, R.dimen.text_16sp)
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

        open fun assert(item: NoteItem) {
            parentCard.isDisplayed().withBackgroundColor(theme, item.color)
            clickContainer.isDisplayed()

            val name = item.name
            nameText.isDisplayed(name.isNotEmpty()).withText(name, R.attr.clContent, R.dimen.text_16sp)

            infoLayout.assert(item)

            colorView.isDisplayed(visible = theme == Theme.DARK) {
                withSize(widthId = R.dimen.layout_8dp)
                withColorIndicator(R.drawable.ic_color_indicator, theme, item.color)
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

            fun assert(item: NoteItem) {
                val type = item.type

                parentContainer.isDisplayed()

                notificationImage.isDisplayed(item.haveAlarm())
                        .withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                        .withDrawableAttr(R.drawable.ic_notifications, R.attr.clNoteIndicator)

                bindImage.isDisplayed(item.isStatus)
                        .withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                        .withDrawableAttr(when (type) {
                            NoteType.TEXT -> R.drawable.ic_bind_text
                            NoteType.ROLL -> R.drawable.ic_bind_roll
                        }, R.attr.clNoteIndicator)

                rankImage.isDisplayed(item.haveRank())
                        .withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                        .withDrawableAttr(R.drawable.ic_rank, R.attr.clNoteIndicator)

                val visible = type == NoteType.ROLL
                progressText.isDisplayed(visible) {
                    withText(item.text, R.attr.clContentSecond, R.dimen.text_12sp)
                }

                val change = item.change.getCalendar().formatPast()
                changeText.isDisplayed().withText(change, R.attr.clContentSecond, R.dimen.text_12sp)
                
                val create = item.create.getCalendar().formatPast()
                createText.isDisplayed().withText(create, R.attr.clContentSecond, R.dimen.text_12sp)
            }
        }

    }

}