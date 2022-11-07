package sgtmelon.scriptum.parent.ui.model.key

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import sgtmelon.scriptum.R

/**
 * Class identifying pages with info.
 */
sealed class InfoCase(
    @IdRes val iconId: Int?,
    @StringRes val titleId: Int,
    @StringRes val detailsId: Int
) {

    object Rank : InfoCase(
        R.mipmap.img_info_rank,
        R.string.info_rank_empty_title,
        R.string.info_rank_empty_details
    )

    class Notes(isHidden: Boolean) : InfoCase(
        R.mipmap.img_info_notes,
        if (isHidden) R.string.info_notes_hide_title else R.string.info_notes_empty_title,
        if (isHidden) R.string.info_notes_hide_details else R.string.info_notes_empty_details
    )

    object Bin : InfoCase(
        R.mipmap.img_info_bin,
        R.string.info_bin_empty_title,
        R.string.info_bin_empty_details
    )

    object Notifications : InfoCase(
        R.mipmap.img_info_notifications,
        R.string.info_notification_empty_title,
        R.string.info_notification_empty_details
    )

    class Roll(isEmpty: Boolean, isHidden: Boolean) : InfoCase(
        iconId = null,
        when {
            isEmpty -> R.string.info_roll_empty_title
            isHidden -> R.string.info_roll_hide_title
            else -> throw IllegalArgumentException()
        },
        when {
            isEmpty -> R.string.info_roll_empty_details
            isHidden -> R.string.info_roll_hide_details
            else -> throw IllegalArgumentException()
        }
    )
}