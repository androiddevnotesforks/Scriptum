package sgtmelon.scriptum.cleanup.data.room.backup

import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult

interface BackupParserSelector {

    fun parse(data: String, version: Int): ParserResult?
}