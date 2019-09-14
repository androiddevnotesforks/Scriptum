package sgtmelon.scriptum.model.item

import android.media.RingtoneManager

/**
 * Модель для описания медиа файлов из [RingtoneManager]
 */
data class MelodyItem(val title: String, val uri: String) {

    constructor(title: String, uri: String, id: String) : this(title, uri = "$uri/$id")

}