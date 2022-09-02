package sgtmelon.scriptum.infrastructure.system.dataSource

import android.content.Context
import android.media.RingtoneManager
import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Class providing access to ringtones and work with [RingtoneManager].
 */
class RingtoneDataSourceImpl(private val context: Context) : RingtoneDataSource {

    private val ringtoneManager get() = RingtoneManager(context)

    override suspend fun getAlarmList(): List<MelodyItem> {
        val typeList = listOf(RingtoneManager.TYPE_ALARM, RingtoneManager.TYPE_RINGTONE)
        return getMelodyList(typeList)
    }

    private fun getMelodyList(typeList: List<Int>): List<MelodyItem> {
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