package sgtmelon.scriptum.cleanup.data.room.backup

import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult

interface BackupParser {

    fun parse(data: String): ParserResult?
}