package sgtmelon.scriptum.repository.develop

import android.content.Context
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollItem
import sgtmelon.scriptum.screen.vm.DevelopViewModel

/**
 * Репозиторий обработки данных [RoomDb] для [DevelopViewModel]
 *
 * @param context для открытия [RoomDb]
 *
 * @author SerjantArbuz
 */
class DevelopRepo(private val context: Context) : IDevelopRepo {

    private fun openRoom() = RoomDb.getInstance(context)

    override suspend fun getNoteTableData() = StringBuilder().apply {
        val list: MutableList<NoteEntity>

        openRoom().apply {
            with(getNoteDao()) { list = get(true).apply { addAll(get(false)) } }
        }.close()

        append("Note table:")

        list.forEach {
            val text = it.text.substring(0, Math.min(it.text.length, 40))
                    .replace("\n", " ")

            append("\n\n")
            append("ID: ${it.id} | CR: ${it.create} | CH: ${it.change}\n")

            if (it.name.isNotEmpty()) append("NM: ${it.name}\n")

            append("TX: $text ${if (it.text.length > 40) "..." else ""}\n")
            append("CL: ${it.color} | TP: ${it.type} | BN: ${it.isBin}\n")
            append("RK ID: ${StringConverter().toString(it.rankId)}\n")
            append("RK PS: ${StringConverter().toString(it.rankPs)}\n")
            append("ST: ${it.isStatus}")
        }
    }.toString()

    override suspend fun getRollTableData() = StringBuilder().apply {
        val list: List<RollItem>

        openRoom().apply { list = getRollDao().get() }.close()

        append("Roll table:")

        list.forEach {
            val text = it.text.substring(0, Math.min(it.text.length, 40))
                    .replace("\n", " ")

            append("\n\n")
            append("ID: ${it.id} | ID_NT: ${it.noteId} | PS: ${it.position} | CH: ${it.isCheck}")
            append("\n")
            append("TX: $text ${if (it.text.length > 40) "..." else ""}")
        }
    }.toString()

    override suspend fun getRankTableData() = StringBuilder().apply {
        val list: MutableList<RankEntity>

        openRoom().apply { list = getRankDao().simple }.close()

        append("Rank table:")

        list.forEach {
            append("\n\n")
            append("ID: ${it.id} | PS: ${it.position} | VS: ${it.isVisible}\n")
            append("NM: ${it.name}\n")
            append("CR: ${StringConverter().toString(it.noteId)}\n")
        }
    }.toString()

    companion object {
        fun getInstance(context: Context): IDevelopRepo = DevelopRepo(context)
    }

}