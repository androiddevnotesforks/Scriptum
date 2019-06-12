package sgtmelon.scriptum.model.item

import android.media.RingtoneManager

/**
 * Модель для описания медиа файлов из [RingtoneManager]

 * @author SerjantArbuz
 */
data class MelodyItem(val title: String, val uri: String) {

    constructor(title: String, uri: String, id: String) : this(title, uri = "$uri/$id")

}