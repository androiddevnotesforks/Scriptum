package sgtmelon.scriptum.cleanup.ui.item

import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import org.hamcrest.Matcher
import sgtmelon.extensions.formatPast
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.haveRank
import sgtmelon.scriptum.infrastructure.utils.extensions.note.hideChecked
import sgtmelon.scriptum.infrastructure.utils.extensions.type
import sgtmelon.scriptum.parent.ui.basic.withCardBackground
import sgtmelon.scriptum.parent.ui.basic.withColorIndicator
import sgtmelon.scriptum.parent.ui.feature.OpenNote
import sgtmelon.scriptum.parent.ui.feature.OpenNoteDialog
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.includeParent
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.longClick
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [NoteAdapter].
 */
class NoteItemUi(
    listMatcher: Matcher<View>,
    p: Int
) : RecyclerItemPart<NoteItem>(listMatcher, p),
    OpenNote.Callback,
    OpenNoteDialog.Callback {

    override fun assert(item: NoteItem) = when (item) {
        is NoteItem.Text -> Text().assert(item)
        is NoteItem.Roll -> Roll().assert(item)
    }

    override fun openClick(item: NoteItem) {
        when (item) {
            is NoteItem.Text -> Text().clickContainer.click()
            is NoteItem.Roll -> Roll().clickContainer.click()
        }
    }

    override fun dialogClick(item: NoteItem) {
        when (item) {
            is NoteItem.Text -> Text().clickContainer.longClick()
            is NoteItem.Roll -> Roll().clickContainer.longClick()
        }
    }

    //region Cleanup

    private inner class Text : Parent<NoteItem.Text>(NoteType.TEXT) {

        override val infoLayout = TextInfo()

        val contentText = getChild(getView(R.id.note_text_content_text))

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
                if (isVisible) list else list.hideChecked().takeIf { it.isNotEmpty() } ?: list
            }

            for (i in 0 until 4) {
                getRow(i).assert(visibleList.getOrNull(i))
            }
        }

        inner class Row(@IdRes parentId: Int) {
            val parentContainer = getChild(getView(parentId))

            val checkImage = getChild(
                getView(R.id.note_roll_check_image).includeParent(getView(parentId))
            )

            val contentText = getChild(
                getView(R.id.note_roll_content_text).includeParent(getView(parentId))
            )

            fun assert(item: RollItem?) {
                parentContainer.isDisplayed(value = item != null)

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

            private val visibleImage = getChild(getView(R.id.note_info_visible_image))
            private val progressText by lazy { getChild(getView(R.id.note_info_progress_text)) }

            override fun assert(item: NoteItem.Roll) {
                super.assert(item)

                visibleImage.isDisplayed(!item.isVisible) {
                    withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                }.withDrawableAttr(sgtmelon.iconanim.R.drawable.ic_visible_exit, R.attr.clIndicator)

                progressText.isDisplayed()
                    .withText(item.text, R.attr.clContentSecond, R.dimen.text_12sp)
            }
        }
    }

    private abstract inner class Parent<N : NoteItem>(type: NoteType) {

        val parentCard = getChild(
            getView(
                when (type) {
                    NoteType.TEXT -> R.id.note_text_parent_card
                    NoteType.ROLL -> R.id.note_roll_parent_card
                }
            )
        )

        val clickContainer = getChild(
            getView(
                when (type) {
                    NoteType.TEXT -> R.id.note_text_click_container
                    NoteType.ROLL -> R.id.note_roll_click_container
                }
            )
        )

        val nameText = getChild(
            getView(
                when (type) {
                    NoteType.TEXT -> R.id.note_text_name_text
                    NoteType.ROLL -> R.id.note_roll_name_text
                }
            )
        )

        abstract val infoLayout: Info<N>

        val colorView = getChild(
            getView(
                when (type) {
                    NoteType.TEXT -> R.id.note_text_color_view
                    NoteType.ROLL -> R.id.note_roll_color_view
                }
            )
        )

        open fun assert(item: N) {
            parentCard.isDisplayed().withCardBackground(
                theme,
                item.color,
                R.dimen.item_card_radius,
                R.dimen.item_card_elevation
            )
            clickContainer.isDisplayed()

            val name = item.name
            nameText.isDisplayed(name.isNotEmpty())
                .withText(name, R.attr.clContent, R.dimen.text_16sp)

            infoLayout.assert(item)

            colorView.isDisplayed(value = theme == ThemeDisplayed.DARK) {
                withSize(widthId = R.dimen.layout_8dp)
                withColorIndicator(R.drawable.ic_color_indicator, theme, item.color)
            }
        }

        abstract inner class Info<N : NoteItem> {
            private val parentContainer = getChild(getView(R.id.note_info_container))

            private val notificationImage = getChild(getView(R.id.note_info_notification_image))
            private val bindImage = getChild(getView(R.id.note_info_bind_image))
            private val rankImage = getChild(getView(R.id.note_info_rank_image))

            private val changeText = getChild(getView(R.id.note_info_change_text))
            private val createText = getChild(getView(R.id.note_info_create_text))

            @CallSuper open fun assert(item: N) {
                val type = item.type

                parentContainer.isDisplayed()

                notificationImage.isDisplayed(item.haveAlarm) {
                    withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                }.withDrawableAttr(R.drawable.ic_notifications, R.attr.clIndicator)

                bindImage.isDisplayed(item.isStatus) {
                    withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                }.withDrawableAttr(when (type) {
                    NoteType.TEXT -> R.drawable.ic_bind_text
                    NoteType.ROLL -> R.drawable.ic_bind_roll
                }, R.attr.clIndicator)

                rankImage.isDisplayed(item.haveRank) {
                    withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
                }.withDrawableAttr(R.drawable.ic_rank, R.attr.clIndicator)

                val change = item.change.toCalendar().formatPast()
                changeText.isDisplayed().withText(change, R.attr.clContentSecond, R.dimen.text_12sp)

                val create = item.create.toCalendar().formatPast()
                createText.isDisplayed().withText(create, R.attr.clContentSecond, R.dimen.text_12sp)
            }
        }
    }

    //endregion

}