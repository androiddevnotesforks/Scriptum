package sgtmelon.scriptum.cleanup.presentation.control.system

import android.content.Context
import android.media.RingtoneManager
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IRingtoneControl
import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Class for help control [RingtoneManager].
 */
class RingtoneControl(private val context: Context) : IRingtoneControl {

    private val ringtoneManager get() = RingtoneManager(context)

    /**
     * Func which fill list with all [MelodyItem] for different [RingtoneManager] types.
     */
    override suspend fun getByType(typeList: List<Int>): List<MelodyItem> {
        val list = ArrayList<MelodyItem>()

        for (it in typeList) {
            val manager = ringtoneManager

            manager.setType(it)
            manager.cursor.apply {
                while (moveToNext()) {
                    val title = getString(RingtoneManager.TITLE_COLUMN_INDEX) ?: continue
                    val uri = getString(RingtoneManager.URI_COLUMN_INDEX) ?: continue
                    val id = getString(RingtoneManager.ID_COLUMN_INDEX) ?: continue

                    list.add(MelodyItem(title, uri, id))
                }
            }.close()
        }

        return list.distinctBy { it.title }.sortedBy { it.title }
    }

}