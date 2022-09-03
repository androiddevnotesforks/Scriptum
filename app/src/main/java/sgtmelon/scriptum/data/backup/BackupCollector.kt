package sgtmelon.scriptum.data.backup

import sgtmelon.scriptum.domain.model.result.ParserResult

interface BackupCollector {

    fun convert(result: ParserResult.Export): String?
}