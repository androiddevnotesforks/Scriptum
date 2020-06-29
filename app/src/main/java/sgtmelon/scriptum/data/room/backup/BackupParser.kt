package sgtmelon.scriptum.data.room.backup

import android.content.Context
import android.util.Log
import org.json.JSONObject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import java.security.MessageDigest

/**
 * Class for help control backup file parsing.
 */
class BackupParser(
        private val context: Context,
        private val selector: BackupSelector
) : IBackupParser {

    data class Model(
            val noteList: List<NoteEntity>,
            val rollList: List<RollEntity>,
            val rollVisibleList: List<RollVisibleEntity>,
            val rankList: List<RankEntity>,
            val alarmList: List<AlarmEntity>
    )

    override fun collect(model: Model): String = JSONObject().apply {
        val roomData = collectRoom(model)

        put(context.getString(R.string.backup_version), VERSION)
        put(context.getString(R.string.backup_hash), getHash(roomData))
        put(context.getString(R.string.backup_room), roomData)
    }.toString()

    @RunPrivate fun collectRoom(model: Model): String = JSONObject().apply {
        TODO()
    }.toString()


    override fun parse(data: String): Model? {
        try {
            val dataObject = JSONObject(data)

            val version = dataObject.getInt(context.getString(R.string.backup_version))
            val hash = dataObject.getString(context.getString(R.string.backup_hash))
            val roomData = dataObject.getString(context.getString(R.string.backup_room))

            if (hash != getHash(roomData)) return null

            return selector.parseByVersion(roomData, version)
        } catch (exception: Exception) {
            Log.e(TAG, exception.toString())
            return null
        }
    }


    @RunPrivate fun getHash(data: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hash = messageDigest.digest(data.toByteArray())

        return hashToHex(hash)
    }

    @RunPrivate fun hashToHex(hash: ByteArray): String = StringBuilder().apply {
        hash.map { Integer.toHexString(0xFF and it.toInt()) }.forEach {
            if (it.length == 1) append('0')

            append(it)
        }
    }.toString()

    companion object {
        private val TAG = BackupParser::class.java.simpleName

        /**
         * When update version need add case inside [BackupSelector].
         */
        const val VERSION = 1
    }

}