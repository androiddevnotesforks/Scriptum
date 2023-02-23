package sgtmelon.scriptum.infrastructure.model.item

import android.media.RingtoneManager

/**
 * Model describes media files from [RingtoneManager].
 */
data class MelodyItem(val title: String, val uri: String) {

    constructor(title: String, uri: String, id: String) : this(title, uri = "$uri/$id")
}