package sgtmelon.scriptum.data.room.backup

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import java.security.MessageDigest

/**
 * Class for help control backup file parsing.
 */
class BackupParser(private val context: Context) : IBackupParser {

    data class Model(
            val noteList: List<NoteEntity>,
            val rollList: List<RollEntity>,
            val rollVisibleList: List<RollVisibleEntity>,
            val rankList: List<RankEntity>,
            val alarmList: List<AlarmEntity>
    )

    override fun collect(model: Model): String = StringBuilder().apply {
        val roomResult = collectRoom(model)

        append(context.getString(R.string.backup_title)).append("\n")
        append(context.getString(R.string.backup_version)).append(VERSION).append("\n")
        append(context.getString(R.string.backup_hash)).append(getHash(roomResult)).append("\n\n")

        append(roomResult)
    }.toString()

    @RunPrivate fun collectRoom(model: Model): String = StringBuilder().apply {
        TODO()
    }.toString()

    override fun parse(data: String): Model? {
        TODO("Not yet implemented")
    }

    @RunPrivate fun getHash(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hash = messageDigest.digest(input.toByteArray())

        return hashToHex(hash)
    }

    @RunPrivate fun hashToHex(hash: ByteArray): String = StringBuilder().apply {
        hash.map { Integer.toHexString(0xFF and it.toInt()) }.forEach {
            if (it.length == 1) append('0')

            append(it)
        }
    }.toString()

    companion object {
        const val VERSION = 1
    }

}