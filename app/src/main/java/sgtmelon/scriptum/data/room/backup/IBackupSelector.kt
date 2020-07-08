package sgtmelon.scriptum.data.room.backup

import sgtmelon.scriptum.domain.model.result.ParserResult

/**
 * Interface for [BackupSelector].
 */
interface IBackupSelector {
    fun parseByVersion(roomData: String, version: Int): ParserResult?
}