package sgtmelon.scriptum.app.repository

import android.content.Context
import android.widget.TextView
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.room.RoomDb

class DevelopRepo(private val context: Context) : IDevelopRepo {

    private fun openRoom() = RoomDb.getInstance(context)

    override fun listRankTable(textView: TextView) {
        val rankList = ArrayList<RankItem>()

        openRoom().apply { rankList.addAll(getRankDao().simple) }.close()

        textView.apply {
            text = ""
            append("Rank table:")
        }

        rankList.forEach {
            textView.apply {
                append("\n\n")
                append("ID: ${it.id} | PS: ${it.position}\n")
                append("NM: ${it.name}\n")
                append("CR: ${it.noteId.joinToString()}\n")
                append("VS: ${it.isVisible}")
            }
        }
    }

    override fun listNoteTable(textView: TextView) {
        val noteList = ArrayList<NoteItem>()

        openRoom().apply {
            noteList.addAll(getNoteDao().get(true))
            noteList.addAll(getNoteDao().get(false))
        }.close()

        textView.apply {
            text = ""
            append("Note table:")
        }

        noteList.forEach {
            val text = it.text.substring(0, Math.min(it.text.length, 40))
                    .replace("\n", " ")

            textView.apply {
                append("\n\n")
                append("ID: ${it.id} | CR: ${it.create} | CH: ${it.change}\n")

                if (it.name.isNotEmpty()) append("NM: ${it.name}\n")

                append("TX: $text ${if (it.text.length > 40) "..." else ""}\n")
                append("CL: ${it.color} | TP: ${it.type} | BN: ${it.isBin}\n")
                append("RK ID: ${it.rankId.joinToString()}\n")
                append("RK PS: ${it.rankPs.joinToString()}\n")
                append("ST: ${it.isStatus}")
            }

        }
    }

    override fun listRollTable(textView: TextView) {
        val rollList = ArrayList<RollItem>()

        openRoom().apply { rollList.addAll(getRollDao().get()) }.close()

        textView.apply {
            text = ""
            append("Roll table:")
        }

        rollList.forEach {
            val text = it.text.substring(0, Math.min(it.text.length, 40))
                    .replace("\n", " ")

            textView.apply {
                append("\n\n")
                append("ID: ${it.id} | ID_NT: ${it.noteId} | PS: ${it.position} | CH: ${it.isCheck}")
                append("\n")
                append("TX: $text ${if (it.text.length > 40) "..." else ""}")
            }
        }
    }

    companion object {
        fun getInstance(context: Context): IDevelopRepo = DevelopRepo(context)
    }

}