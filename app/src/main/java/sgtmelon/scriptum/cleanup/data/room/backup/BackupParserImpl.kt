package sgtmelon.scriptum.cleanup.data.room.backup

import org.json.JSONObject
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.exception.BackupParserException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Class for help control backup file parsing.
 */
class BackupParserImpl(
    private val dataSource: BackupDataSource,
    private val hashMaker: BackupHashMaker,
    private val selector: BackupParserSelector,
    private val colorConverter: ColorConverter,
    private val typeConverter: NoteTypeConverter,
    private val stringConverter: StringConverter
) : BackupParser {

    override fun parse(data: String): ParserResult? {
        try {
            val jsonObject = JSONObject(data)

            val version = jsonObject.getInt(dataSource.versionKey)
            val hash = jsonObject.getString(dataSource.hashKey)
            val database = jsonObject.getString(dataSource.databaseKey)

            if (hash != hashMaker.get(database)) return null

            return selector.parse(database, version)
        } catch (e: Throwable) {
            BackupParserException(e).record()
        }

        return null
    }

    companion object {
        /**
         * When update version need add case inside [BackupParserSelectorImpl].
         */
        const val VERSION = 1
    }
}