package sgtmelon.scriptum.ui.item

import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import org.hamcrest.Matcher
import sgtmelon.extension.formatPast
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.extension.hide
import sgtmelon.scriptum.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.ui.ParentRecyclerItem

/**
 * Class for UI control of [NoteAdapter].
 */
class NoteItemUi(listMatcher: Matcher<View>, p: Int) :
    ParentRecyclerItem<NoteItem>(listMatcher, p) {

    override fun assert(item: NoteItem) = when (item) {
        is NoteItem.Text -> Text().assert(item)
        is NoteItem.Roll -> Roll().assert(item)
    }

    private inner class Text : Parent<NoteItem.Text>(NoteType.TEXT) {

        override val infoLayout = TextInfo()

        val contentText = getChild(getViewById(R.id.note_text_content_text))

        override fun assert(item: NoteItem.Text) {
            super.assert(item)

            contentText.isDisplayed().withText(item.text, R.attr.clContent, R.dimen.text_16sp)
        }

        inner class TextInfo : Info<NoteItem.Text>()
    }

    private inner class Roll : Parent<NoteItem.Roll>(NoteType.ROLL) {

        override val infoLayout = RollInfo()

        fun getRow(p: Int) = Row(when (p) {
            0 -> R.id.note_roll_row0_container
            1 -> R.id.note_roll_row1_container
            2 -> R.id.note_roll_row2_container
            else -> R.id.note_roll_row3_container
        })

        override fun assert(item: NoteItem.Roll) {
            super.assert(item)

            val visibleList = with(item) {
                if (isVisible) list else list.hide().takeIf { it.isNotEmpty() } ?: list
            }

            for (i in 0 until 4) {
                getRow(i).assert(visibleList.getOrNull(i))
            }
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
                parentContainer.isDisplayed(isVisible = item != null)

                if (item == null) return

                checkImage.isDisplayed {
                    withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                }.withDrawableAttr(when (item.isCheck) {
                    true -> R.drawable.ic_check_done
                    false -> R.drawable.ic_check_outline
                }, R.attr.clContent)

                contentText.isDisplayed().withText(item.text, R.attr.clContent, R.dimen.text_16sp)
            }
        }

        inner class RollInfo : Info<NoteItem.Roll>() {

            private val visibleImage = getChild(getViewById(R.id.note_info_visible_image))
            private val progressText by lazy { getChild(getViewById(R.id.note_info_progress_text)) }

            override fun assert(item: NoteItem.Roll) {
                super.assert(item)

                visibleImage.isDisplayed(!item.isVisible) {
                    withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                }.withDrawableAttr(R.drawable.ic_visible_exit, R.attr.clIndicator)

                progressText.isDisplayed()
                    .withText(item.text, R.attr.clContentSecond, R.dimen.text_12sp)
            }
        }
    }

    private abstract inner class Parent<N : NoteItem>(type: NoteType) {

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

        abstract val infoLayout: Info<N>

        val colorView = getChild(getViewById(when (type) {
            NoteType.TEXT -> R.id.note_text_color_view
            NoteType.ROLL -> R.id.note_roll_color_view
        }))

        open fun assert(item: N) {
            parentCard.isDisplayed().withCardBackground(theme, item.color)
            clickContainer.isDisplayed()

            val name = item.name
            nameText.isDisplayed(name.isNotEmpty())
                .withText(name, R.attr.clContent, R.dimen.text_16sp)

            infoLayout.assert(item)

            colorView.isDisplayed(isVisible = theme == Theme.DARK) {
                withSize(widthId = R.dimen.layout_8dp)
                withColorIndicator(R.drawable.ic_color_indicator, theme, item.color)
            }
        }

        abstract inner class Info<N : NoteItem> {
            private val parentContainer = getChild(getViewById(R.id.note_info_container))

            private val notificationImage = getChild(getViewById(R.id.note_info_notification_image))
            private val bindImage = getChild(getViewById(R.id.note_info_bind_image))
            private val rankImage = getChild(getViewById(R.id.note_info_rank_image))

            private val changeText = getChild(getViewById(R.id.note_info_change_text))
            private val createText = getChild(getViewById(R.id.note_info_create_text))

            @CallSuper open fun assert(item: N) {
                val type = item.type

                parentContainer.isDisplayed()

                notificationImage.isDisplayed(item.haveAlarm()) {
                    withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                }.withDrawableAttr(R.drawable.ic_notifications, R.attr.clIndicator)

                bindImage.isDisplayed(item.isStatus) {
                    withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                }.withDrawableAttr(when (type) {
                    NoteType.TEXT -> R.drawable.ic_bind_text
                    NoteType.ROLL -> R.drawable.ic_bind_roll
                }, R.attr.clIndicator)

                rankImage.isDisplayed(item.haveRank()) {
                    withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                }.withDrawableAttr(R.drawable.ic_rank, R.attr.clIndicator)

                val change = item.change.getCalendar().formatPast()
                changeText.isDisplayed().withText(change, R.attr.clContentSecond, R.dimen.text_12sp)

                val create = item.create.getCalendar().formatPast()
                createText.isDisplayed().withText(create, R.attr.clContentSecond, R.dimen.text_12sp)
            }
        }

    }

}