package sgtmelon.scriptum.app.repository

import android.content.Context
import android.text.TextUtils
import android.widget.TextView
import sgtmelon.scriptum.app.room.RoomDb

class DeveloperRepo(private val context: Context): IDeveloperRepo {

    private lateinit var db: RoomDb

    private fun openRoom() = RoomDb.getInstance(context)

    override fun listRankTable(textView: TextView) {
        db = RoomDb.getInstance(context)
        val rankList = db.daoRank().simple
        db.close()

        val annotation = "Rank Data Base: "
        textView.text = annotation

        for (i in rankList.indices) {
            val rankItem = rankList[i]

            textView.append("\n\n" +
                    "ID: " + rankItem.id + " | " +
                    "PS: " + rankItem.position + "\n" +
                    "NM: " + rankItem.name + "\n" +
                    "CR: " + TextUtils.join(", ", rankItem.noteId) + "\n" +
                    "VS: " + rankItem.isVisible)
        }
    }

    override fun listNoteTable(textView: TextView) {
        db = RoomDb.getInstance(context)
        val noteList = db.daoNote().get(true)
        noteList.addAll(db.daoNote().get(false))
        db.close()


        val annotation = "Note Data Base:"
        textView.text = annotation

        for (i in noteList.indices) {
            val noteItem = noteList[i]

            textView.append("\n\n" +
                    "ID: " + noteItem.id + " | " +
                    "CR: " + noteItem.create + " | " +
                    "CH: " + noteItem.change + "\n")

            val name = noteItem.name
            if (!TextUtils.isEmpty(name)) {
                textView.append("NM: $name\n")
            }

            val text = noteItem.text
            textView.append("TX: " + text.substring(0, Math.min(text.length, 45))
                    .replace("\n", " "))

            if (text.length > 40) {
                textView.append("...")
            }

            textView.append("\n")

            textView.append("CL: " + noteItem.color + " | " +
                    "TP: " + noteItem.type + " | " +
                    "BN: " + noteItem.isBin + "\n" +
                    "RK ID: " + TextUtils.join(", ", noteItem.rankId) + " | " +
                    "RK PS: " + TextUtils.join(", ", noteItem.rankPs) + "\n" +
                    "ST: " + noteItem.isStatus)
        }
    }

    override fun listRollTable(textView: TextView) {
        db = RoomDb.getInstance(context)
        val rollList = db.daoRoll().get()
        db.close()

        val annotation = "Roll Data Base:"
        textView.text = annotation

        for (i in rollList.indices) {
            val rollItem = rollList[i]

            textView.append("\n\n" +
                    "ID: " + rollItem.id + " | " +
                    "ID_NT: " + rollItem.noteId + " | " +
                    "PS: " + rollItem.position + " | " +
                    "CH: " + rollItem.isCheck + "\n")

            val text = rollItem.text
            textView.append("TX: " + text.substring(0, Math.min(text.length, 45))
                    .replace("\n", " "))

            if (text.length > 40) textView.append("...")
        }
    }

    companion object {
        fun getInstance(context: Context): IDeveloperRepo = DeveloperRepo(context)
    }

}