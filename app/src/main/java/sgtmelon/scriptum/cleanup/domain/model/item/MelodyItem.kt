package sgtmelon.scriptum.cleanup.domain.model.item

import android.media.RingtoneManager

/**
 * Model which describes media files from [RingtoneManager].
 */
data class MelodyItem(val title: String, val uri: String) {

    constructor(title: String, uri: String, id: String) : this(title, uri = "$uri/$id")

}