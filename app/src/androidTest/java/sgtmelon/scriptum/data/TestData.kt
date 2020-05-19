package sgtmelon.scriptum.data

import sgtmelon.extension.getRandomFutureTime
import sgtmelon.extension.getText
import sgtmelon.extension.getTime
import sgtmelon.extension.nextString
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.converter.model.RollConverter
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.data.ColorData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.domain.model.key.NoteType
import kotlin.random.Random
import sgtmelon.scriptum.basic.extension.getTime as getCalendarTime

/**
 * Class which fill db and provide data for tests.
 */
class TestData(
        override val roomProvider: RoomProvider,
        private val preferenceRepo: IPreferenceRepo
) : IRoomWork {

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()
    private val rankConverter = RankConverter()
    private val alarmConverter = AlarmConverter()

    val textNote: NoteEntity
        get() = NoteEntity().apply {
            create = getTime()
            change = getTime()
            name = Random.nextString()
            text = Random.nextString().repeat(n = (1 until 10).random())
            color = (0 until ColorData.size).random()
            type = NoteType.TEXT
        }

    val rollNote: NoteEntity
        get() = NoteEntity().apply {
            create = getTime()
            change = getTime()
            name = Random.nextString()
            color = (0 until ColorData.size).random()
            type = NoteType.ROLL
        }

    val rollList: ArrayList<RollEntity>
        get() = ArrayList<RollEntity>().apply {
            (0 until (1 until 6).random()).forEach {
                add(rollEntity.apply {
                    position = it
                    text = "$it | $text"
                })
            }
        }

    val rollEntity: RollEntity
        get() = RollEntity().apply {
            isCheck = Random.nextBoolean()
            text = Random.nextString()
        }

    val rankEntity: RankEntity get() = RankEntity(name = Random.nextString())


    fun createNote(): NoteItem = if (Random.nextBoolean()) createText() else createRoll()

    fun createText(): NoteItem.Text {
        val color = preferenceRepo.defaultColor ?: throw NullPointerException()
        return NoteItem.Text.getCreate(color)
    }

    fun createRoll(): NoteItem.Roll {
        val color = preferenceRepo.defaultColor ?: throw NullPointerException()
        return NoteItem.Roll.getCreate(color)
    }


    fun insertRank(entity: RankEntity = rankEntity): RankItem {
        inRoomTest { entity.id = rankDao.insert(entity) }

        return rankConverter.toItem(entity)
    }

    fun insertRankForNotes(): Pair<RankItem, NoteItem> {
        val noteItem = insertNote()

        val rankItem = insertRank(rankEntity.apply {
            noteId.add(noteItem.id)
        })

        inRoomTest {
            noteDao.update(noteConverter.toEntity(noteItem.apply {
                rankId = rankItem.id
                rankPs = rankItem.position
            }))
        }

        return Pair(rankItem, noteItem)
    }

    fun insertRankForBin(): Pair<RankItem, NoteItem> {
        val noteItem = insertNoteToBin()

        val rankItem = insertRank(rankEntity.apply {
            noteId.add(noteItem.id)
        })

        inRoomTest {
            noteDao.update(noteConverter.toEntity(noteItem.apply {
                rankId = rankItem.id
                rankPs = rankItem.position
            }))
        }

        return Pair(rankItem, noteItem)
    }

    fun insertText(entity: NoteEntity = textNote): NoteItem.Text {
        inRoomTest { entity.id = noteDao.insert(entity) }

        val item = noteConverter.toItem(entity)

        if (item !is NoteItem.Text) throw IllegalAccessException("Wrong note type")

        return item
    }

    fun insertTextToBin(note: NoteEntity = textNote) = insertText(note.apply { isBin = true })

    fun insertRoll(note: NoteEntity = rollNote,
                   list: ArrayList<RollEntity> = rollList): NoteItem.Roll {
        inRoomTest {
            note.id = noteDao.insert(note)
            list.forEach {
                it.noteId = note.id
                rollDao.insert(it)
            }
        }

        // TODO REFACTOR
        val item = noteConverter.toItem(note, rollConverter.toItem(list))

        if (item !is NoteItem.Roll) throw IllegalAccessException("Wrong note type")

        item.updateComplete()

        inRoomTest { noteDao.update(noteConverter.toEntity(item)) }

        return item
    }

    fun insertRollToBin(note: NoteEntity = rollNote,
                        list: ArrayList<RollEntity> = rollList): NoteItem.Roll {
        return insertRoll(note.apply { isBin = true }, list)
    }


    fun insertNote(time: String? = null): NoteItem {
        val entity = if (Random.nextBoolean()) textNote else rollNote

        if (time != null) {
            entity.change = time
            entity.create = time
        }

        return when(entity.type) {
            NoteType.TEXT -> insertText(entity)
            NoteType.ROLL -> insertRoll(entity)
        }
    }

    fun insertNoteToBin(time: String? = null): NoteItem {
        val entity = if (Random.nextBoolean()) textNote else rollNote

        if (time != null) {
            entity.change = time
            entity.create = time
        }

        return when(entity.type) {
            NoteType.TEXT -> insertTextToBin(entity)
            NoteType.ROLL -> insertRollToBin(entity)
        }
    }

    fun insertNotification(noteItem: NoteItem = insertNote(),
                           date: String = getRandomFutureTime()): NoteItem {
        noteItem.alarmDate = date

        inRoomTest { noteItem.alarmId = alarmDao.insert(alarmConverter.toEntity(noteItem)) }

        return noteItem
    }


    fun fillRank(count: Int = 10) = ArrayList<RankItem>().apply {
        (0 until count).forEach {
            add(insertRank(rankEntity.apply {
                name = "$it | $name"
                position = it
                isVisible = Random.nextBoolean()
            }))
        }
    }

    fun fillRankRelation(count: Int = 10) = ArrayList<RankItem>().apply {
        (0 until count).forEach {
            val noteCount = (0 until 5).random()
            val noteList = ArrayList<NoteItem>().apply {
                repeat(noteCount) { add(insertNote()) }
            }

            val rankItem = insertRank(RankEntity(
                    noteId = noteList.map { item -> item.id }.toMutableList(),
                    position = it,
                    name = "$it | ${Random.nextString()}",
                    isVisible = Random.nextBoolean()
            ))

            add(rankItem)

            inRoomTest {
                noteList.forEach { item ->
                    noteDao.update(noteConverter.toEntity(item.apply {
                        rankId = rankItem.id
                        rankPs = rankItem.position
                    }))
                }
            }
        }
    }

    fun fillNotes(count: Int = 10) = ArrayList<NoteItem>().apply {
        (count downTo 0).forEach {
            add(insertNote(getCalendarTime(it).getText()))
        }
    }

    fun fillBin(count: Int = 10) = ArrayList<NoteItem>().apply {
        (count downTo 0).forEach {
            add(insertNoteToBin(getCalendarTime(it).getText()))
        }
    }

    fun fillNotification(count: Int = 10) = repeat(count) {
        insertNotification(date = getRandomFutureTime())
    }


    fun clear() = apply { inRoomTest { clearAllTables() } }

}