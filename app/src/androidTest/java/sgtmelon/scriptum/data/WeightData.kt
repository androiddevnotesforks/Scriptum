package sgtmelon.scriptum.data

import android.content.Context
import sgtmelon.common.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.data.ColorData
import sgtmelon.scriptum.domain.model.key.NoteType
import kotlin.random.Random

/**
 * Data for weight tests
 */
class WeightData(
    private val context: Context,
    override val roomProvider: RoomProvider
) : IRoomWork {

    val textNote: NoteEntity
        get() = NoteEntity().apply {
            create = getTime()
            change = getTime()
            name = context.getString(R.string.test_note_name)
            text = "${context.getString(R.string.test_note_text)}\n\n".repeat(n = 100)
            color = (0 until ColorData.size).random()
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