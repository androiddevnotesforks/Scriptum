package sgtmelon.scriptum.cleanup.ui.part.info

import sgtmelon.scriptum.R

/**
 * Part of UI abstraction for control notes page info.
 */
class NotesInfoContainer(isHide: Boolean) : ParentInfoContainer(R.mipmap.img_info_notes) {

    override val includeContainer = getViewById(R.id.notes_info_include)

    override val titleText = getView(R.id.info_title_text, if (isHide) {
        R.string.info_notes_hide_title
    } else {
        R.string.info_notes_empty_title
    })

    override val detailsText = getView(R.id.info_details_text, if (isHide) {
        R.string.info_notes_hide_details
    } else {
        R.string.info_notes_empty_details
    })

}