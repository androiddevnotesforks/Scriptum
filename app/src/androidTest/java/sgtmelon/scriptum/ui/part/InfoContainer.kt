package sgtmelon.scriptum.ui.part

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.withDrawableAttr
import sgtmelon.scriptum.data.InfoPage
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.NotificationScreen
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.main.RankScreen

/**
 * Part of UI abstraction for control info inside:
 * [RankScreen], [NotesScreen], [BinScreen], [NotificationScreen]
 */
class InfoContainer(private val page: InfoPage, hide: Boolean = false) : ParentUi() {

    private val infoContainer = getViewById(when (page) {
        InfoPage.RANK -> R.id.rank_info_include
        InfoPage.NOTES -> R.id.notes_info_include
        InfoPage.BIN -> R.id.bin_info_include
        InfoPage.NOTIFICATION -> R.id.notification_info_include
    })

    private val infoImage = getViewById(R.id.info_image).includeParent(infoContainer)

    private val infoTitleText = getView(R.id.info_title_text, when (page) {
        InfoPage.RANK -> R.string.info_rank_empty_title
        InfoPage.NOTES -> if (hide) R.string.info_notes_hide_title else R.string.info_notes_empty_title
        InfoPage.BIN -> R.string.info_bin_empty_title
        InfoPage.NOTIFICATION -> R.string.info_notification_empty_title
    })

    private val infoDetailsText = getView(R.id.info_details_text, when (page) {
        InfoPage.RANK -> R.string.info_rank_empty_details
        InfoPage.NOTES -> if (hide) {
            R.string.info_notes_hide_details
        } else {
            R.string.info_notes_empty_details
        }
        InfoPage.BIN -> R.string.info_bin_empty_details
        InfoPage.NOTIFICATION -> R.string.info_notification_empty_details
    })

    fun assert(visible: Boolean) {
        infoContainer.isDisplayed(visible)

        infoImage.isDisplayed(visible).withDrawableAttr(when (page) {
            InfoPage.RANK -> R.mipmap.img_info_rank
            InfoPage.NOTES -> R.mipmap.img_info_notes
            InfoPage.BIN -> R.mipmap.img_info_bin
            InfoPage.NOTIFICATION -> R.mipmap.img_info_notifications
        }, attrColor = R.attr.clContent)

        infoTitleText.isDisplayed(visible)
        infoDetailsText.isDisplayed(visible)
    }

}