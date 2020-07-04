package sgtmelon.scriptum.data.room.backup

/**
 * Interface for [BackupSelector].
 */
interface IBackupSelector {
    fun parseByVersion(roomData: String, version: Int): BackupParser.Model?
}