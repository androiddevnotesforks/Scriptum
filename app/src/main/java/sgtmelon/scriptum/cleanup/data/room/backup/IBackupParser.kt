package sgtmelon.scriptum.cleanup.data.room.backup

import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult

/**
 * Interface for [BackupParserImpl].
 */
interface IBackupParser {

    fun collect(model: ParserResult): String

    fun parse(data: String): ParserResult?
}