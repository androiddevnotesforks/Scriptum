package sgtmelon.scriptum.cleanup.ui.part.info

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.SimpleInfoPage

/**
 * Part of UI abstraction for control info. Use for simple constructions.
 */
class SimpleInfoContainer(page: SimpleInfoPage) : ParentInfoContainer(
    when (page) {
        SimpleInfoPage.RANK -> R.mipmap.img_info_rank
        SimpleInfoPage.BIN -> R.mipmap.img_info_bin
        SimpleInfoPage.NOTIFICATION -> R.mipmap.img_info_notifications
    }
) {

    override val includeContainer = getViewById(
        when (page) {
            SimpleInfoPage.RANK -> R.id.rank_info_include
            SimpleInfoPage.BIN -> R.id.bin_info_include
            SimpleInfoPage.NOTIFICATION -> R.id.info_include
        }
    )

    override val titleText = getView(
        R.id.info_title_text, when (page) {
            SimpleInfoPage.RANK -> R.string.info_rank_empty_title
            SimpleInfoPage.BIN -> R.string.info_bin_empty_title
            SimpleInfoPage.NOTIFICATION -> R.string.info_notification_empty_title
        }
    )

    override val detailsText = getView(
        R.id.info_details_text, when (page) {
            SimpleInfoPage.RANK -> R.string.info_rank_empty_details
            SimpleInfoPage.BIN -> R.string.info_bin_empty_details
        SimpleInfoPage.NOTIFICATION -> R.string.info_notification_empty_details
    })

}