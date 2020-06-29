package sgtmelon.scriptum.data.room.backup

import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate

/**
 * Class for parsing different versions of backup files.
 *
 * Need parse here because it's not common thing, like [IBackupParser.collect]. Parsing may change
 * by versions and because of that we can't use here some annotations
 * like in [IBackupParser.collect].
 */
class BackupSelector {

    fun parseByVersion(roomData: String, version: Int): BackupParser.Model? {
        return when (version) {
            1 -> getVersion1(roomData)
            else -> null
        }
    }

    @RunPrivate fun getVersion1(roomData: String): BackupParser.Model {
        TODO()
    }

}