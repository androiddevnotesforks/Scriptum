package sgtmelon.scriptum.cleanup.data.room.backup

import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult

/**
 * Interface for [BackupParserSelectorImpl].
 */
interface IBackupSelector {
    fun parseByVersion(roomData: String, version: Int): ParserResult?
}