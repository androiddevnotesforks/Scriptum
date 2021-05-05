package sgtmelon.scriptum.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.domain.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.domain.model.item.NotificationItem.Note
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.parent.ParentTest

/**
 * Test for [NotificationItem]
 */
class NotificationItemTest : ParentTest() {

    //region Data

    private val firstItem = NotificationItem(
        Note(id = 122, name = "zibulba", color = 82, type = NoteType.TEXT),
        Alarm(id = 15, date = "14.092")
    )
    private val secondItem = NotificationItem(
        Note(id = 92, name = "buba", color = 5, type = NoteType.ROLL),
        Alarm(id = 6, date = "1hel92")
    )

    private val firstString = """{"NT_COLOR":82,"NT_ID":122,"NT_NAME":"zibulba","AL_ID":15,"NT_TYPE":0,"AL_DATE":"14.092"}"""
    private val secondString = """{"NT_COLOR":5,"NT_ID":92,"NT_NAME":"buba","AL_ID":6,"NT_TYPE":1,"AL_DATE":"1hel92"}"""
    private val wrongString = nextString()

    //endregion

    @Test fun getJson() {
        assertEquals(firstItem.toJson(), firstString)
        assertEquals(secondItem.toJson(), secondString)
    }

    @Test fun getItem() {
        assertEquals(firstItem, NotificationItem[firstString])
        assertEquals(secondItem, NotificationItem[secondString])

        assertNull(NotificationItem[wrongString])
    }
}