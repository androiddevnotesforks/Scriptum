package sgtmelon.scriptum.cleanup.data.room.backup

import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult

interface BackupParserSelector {

    fun parse(roomData: String, version: Int): ParserResult?
}