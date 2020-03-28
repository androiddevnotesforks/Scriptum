package sgtmelon.scriptum.data.repository.room

import android.content.Context
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.model.StringConverter
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.key.NoteType
import kotlin.math.min

/**
 * Repository of [RoomDb] which work with all tables data.
 *
 * @param context for open [RoomDb]
 */
class DevelopRepo(override val context: Context) : IDevelopRepo, IRoomWork {

    private val preferenceRepo: IPreferenceRepo = PreferenceRepo(context)

    override suspend fun getNoteTablePrint() = StringBuilder().apply {
        val list: MutableList<NoteEntity> = ArrayList()

        inRoom {
            list.addAll(noteDao.getByChange(bin = true))
            list.addAll(noteDao.getByChange(bin = false))
        }

        append("Note table:")

        list.forEach {
            val text = it.text.substring(0, min(it.text.length, b = 40))
                    .replace("\n", " ")

            append("\n\n")
            append("ID: ${it.id} | CR: ${it.create} | CH: ${it.change}\n")

            if (it.name.isNotEmpty()) append("NM: ${it.name}\n")

            append("TX: $text ${if (it.text.length > 40) "..." else ""}\n")
            append("CL: ${it.color} | TP: ${it.type} | BN: ${it.isBin}\n")
            append("RK ID: ${it.rankId}\n")
            append("RK PS: ${it.rankPs}\n")
            append("ST: ${it.isStatus}")
        }
    }.toString()

    override suspend fun getRollTablePrint() = StringBuilder().apply {
        val list: MutableList<RollEntity> = ArrayList()

        inRoom {
            noteDao.getByChange(bin = false)
                    .filter { it.type == NoteType.ROLL }
                    .map { it.id }
                    .forEach {noteId -> rollDao.get(noteId).forEach { list.add(it) } }

            noteDao.getByChange(bin = true)
                    .filter { it.type == NoteType.ROLL }
                    .map { it.id }
                    .forEach {noteId -> rollDao.get(noteId).forEach { list.add(it) } }
        }

        append("Roll table:")

        list.forEach {
            val text = it.text.substring(0, it.text.length.coerceAtMost(maximumValue = 40))
                    .replace("\n", " ")

            append("\n\n")
            append("ID: ${it.id} | ID_NT: ${it.noteId} | PS: ${it.position} | CH: ${it.isCheck}")
            append("\n")
            append("TX: $text ${if (it.text.length > 40) "..." else ""}")
        }
    }.toString()

    override suspend fun getRankTablePrint() = StringBuilder().apply {
        val list = ArrayList<RankEntity>().apply { inRoom { addAll(rankDao.get()) } }

        append("Rank table:")

        list.forEach {
            append("\n\n")
            append("ID: ${it.id} | PS: ${it.position} | VS: ${it.isVisible}\n")
            append("NM: ${it.name}\n")
            append("CR: ${StringConverter().toString(it.noteId)}")
        }
    }.toString()

    override suspend fun getPreferencePrint()  = StringBuilder().apply {
        with(preferenceRepo) {
            append("Preference:\n\n")
            append("Theme: $theme\n")
            append("Repeat: $repeat\n")
            append("Signal: $signal\n")
            append("Melody: $melodyUri\n")
            append("Volume: $volume\n")
            append("VolumeIncrease: $volumeIncrease\n")

            append("Sort: $sort\n")
            append("DefaultColor: $defaultColor\n")
            append("PauseSave: $pauseSaveOn\n")
            append("AutoSave: $autoSaveOn\n")
            append("SaveTime: $savePeriod")
        }
    }.toString()

}