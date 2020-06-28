package sgtmelon.scriptum.data.room.backup

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.extension.substringBeforeOrNull
import sgtmelon.scriptum.extension.substringBetweenOrNull
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


    @RunPrivate fun getVersionTag() = with(context) {
        Pair(getString(R.string.tag_version_start), getString(R.string.tag_version_end))
    }

    @RunPrivate fun getHashTag() = with(context) {
        Pair(getString(R.string.tag_hash_start), getString(R.string.tag_hash_end))
    }

    @RunPrivate fun getRoomTag() = with(context) {
        Pair(getString(R.string.tag_room_start), getString(R.string.tag_room_end))
    }


    override fun collect(model: Model): String? = StringBuilder().apply {
        val versionTag = getVersionTag()
        val hashTag = getHashTag()
        val roomTag = getRoomTag()

        val roomResult = collectRoom(model)

        append(versionTag.first).append(VERSION).append(versionTag.second).append("\n")
        append(hashTag.first).append(getHash(roomResult)).append(hashTag.second).append("\n")
        append(roomTag.first).append(roomResult).append(roomTag.second)
    }.toString()

    @RunPrivate fun collectRoom(model: Model): String = StringBuilder().apply {
        TODO()
    }.toString()


    override fun parse(data: String): Model? {
        val versionTag = getVersionTag()
        val hashTag = getHashTag()
        val roomTag = getRoomTag()

        val version = data.substringBeforeOrNull(hashTag.first)
                ?.substringBetweenOrNull(versionTag.first, versionTag.second)
                ?.toIntOrNull() ?: return null

        val hash = data.substringBeforeOrNull(roomTag.first)
                ?.substringBetweenOrNull(hashTag.first, hashTag.second) ?: return null

        val roomData = data.substringBetweenOrNull(roomTag.first, roomTag.second) ?: return null

        if (hash != getHash(roomData)) return null

        return parseByVersion(roomData, version)
    }

    @RunPrivate fun parseByVersion(roomData: String, version: Int): Model? {
        TODO()
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
        const val VERSION = 1
    }

}