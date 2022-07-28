package sgtmelon.scriptum.infrastructure.provider

import android.content.Context
import android.media.RingtoneManager
import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Class for help control [RingtoneManager].
 */
class RingtoneProviderImpl(private val context: Context) : RingtoneProvider {

    private val ringtoneManager get() = RingtoneManager(context)

    private val alarmTypeList = listOf(RingtoneManager.TYPE_ALARM, RingtoneManager.TYPE_RINGTONE)

    /**
     * Func which fill list with all [MelodyItem] for different [RingtoneManager] types.
     */
    override suspend fun getAlarmList(): List<MelodyItem> {
        val list = ArrayList<MelodyItem>()

        for (it in alarmTypeList) {
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