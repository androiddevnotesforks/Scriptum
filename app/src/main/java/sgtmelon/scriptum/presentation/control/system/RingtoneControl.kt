package sgtmelon.scriptum.presentation.control.system

import android.content.Context
import android.media.RingtoneManager
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.presentation.control.system.callback.IRingtoneControl

/**
 * Class for help control [RingtoneManager].
 */
class RingtoneControl(private val context: Context) : IRingtoneControl {

    /**
     * TODO #RELEASE add suspend
     */

    private val ringtoneManager get() = RingtoneManager(context)

    /**
     * Func which fill list with all [MelodyItem] for current [RingtoneManager] type.
     */
    override fun getByType(type: Int): List<MelodyItem> = ArrayList<MelodyItem>().apply {
        val manager = ringtoneManager

        manager.setType(type)
        manager.cursor.apply {
            while (moveToNext()) {
                val title = getString(RingtoneManager.TITLE_COLUMN_INDEX) ?: continue
                val uri = getString(RingtoneManager.URI_COLUMN_INDEX) ?: continue
                val id = getString(RingtoneManager.ID_COLUMN_INDEX) ?: continue

                add(MelodyItem(title, uri, id))
            }
        }.close()
    }

}