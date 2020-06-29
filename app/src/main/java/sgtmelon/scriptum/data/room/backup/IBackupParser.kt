package sgtmelon.scriptum.data.room.backup

/**
 * Interface for [BackupParser].
 */
interface IBackupParser {

    fun collect(model: BackupParser.Model): String

    fun parse(data: String): BackupParser.Model?

}