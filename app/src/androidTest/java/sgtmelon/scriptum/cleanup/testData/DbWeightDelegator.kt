package sgtmelon.scriptum.cleanup.testData

import android.content.Context
import kotlin.random.Random
import sgtmelon.common.utils.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.parent.RoomWorker

/**
 * Data for weight tests
 */
class DbWeightDelegator(
    private val context: Context,
    override val database: Database
) : RoomWorker {

    val textNote: NoteEntity
        get() = NoteEntity().apply {
            create = getTime()
            change = getTime()
            name = context.getString(R.string.test_note_name)
            text = "${context.getString(R.string.test_note_text)}\n\n".repeat(n = 100)
            color = Color.values().random()
            type = NoteType.TEXT
        }

    val rollList = ArrayList<RollEntity>().apply {
        for (i in 0 until 300) {
            add(RollEntity(
                position = i,
                isCheck = Random.nextBoolean(),
                text = "$i | " + "${context.getString(R.string.test_roll_text)} | ".repeat((1..3).random())
            ))
        }
    }
}