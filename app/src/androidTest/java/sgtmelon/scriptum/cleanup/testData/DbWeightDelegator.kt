package sgtmelon.scriptum.cleanup.testData

import android.content.Context
import kotlin.random.Random
import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.source.RoomWorker

/**
 * Data for weight tests.
 */
class DbWeightDelegator(override val database: Database) : RoomWorker {

    val textNote: NoteEntity
        get() = NoteEntity().apply {
            create = getCalendarText()
            change = getCalendarText()
            name = NAME
            text = "$TEXT\n\n".repeat(n = 100)
            color = Color.values().random()
            type = NoteType.TEXT
        }

    val rollList = ArrayList<RollEntity>().apply {
        for (i in 0 until 300) {
            add(RollEntity(
                position = i,
                isCheck = Random.nextBoolean(),
                text = "$i | " + "$ITEM | ".repeat((1..3).random())
            ))
        }
    }

    private companion object {
        const val NAME = "Don't Dream It's Over"
        const val TEXT = """
            There is freedom within, there is freedom without
            Try to catch the deluge in a paper cup
            There's a battle ahead, many battles are lost
            But you'll never see the end of the road
            While you're traveling with me
        """
        const val ITEM = "The Sun Ain't Gonna Shine Anymore"
    }
}