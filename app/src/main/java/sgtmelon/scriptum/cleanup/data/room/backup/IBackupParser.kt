package sgtmelon.scriptum.cleanup.data.room.backup

import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult

/**
 * Interface for [BackupParser].
 */
interface IBackupParser {

    fun collect(model: ParserResult): String

    fun parse(data: String): ParserResult?
}