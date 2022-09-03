package sgtmelon.scriptum.cleanup.data.room.backup

import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult

interface BackupCollector {

    fun convert(model: ParserResult): String?
}