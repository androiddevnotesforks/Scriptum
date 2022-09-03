package sgtmelon.scriptum.data.backup

import sgtmelon.scriptum.domain.model.result.ParserResult

interface BackupParser {

    fun convert(data: String): ParserResult.Import?
}