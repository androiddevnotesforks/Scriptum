package sgtmelon.scriptum.data.backup

import java.security.MessageDigest
import sgtmelon.common.test.annotation.RunPrivate

class BackupHashMaker {

    fun get(data: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hash = messageDigest.digest(data.toByteArray())

        return hashToHex(hash)
    }

    @RunPrivate fun hashToHex(hash: ByteArray): String = StringBuilder().apply {
        for (it in hash.map { Integer.toHexString(0xFF and it.toInt()) }) {
            if (it.length == 1) append('0')

            append(it)
        }
    }.toString()
}