package sgtmelon.scriptum.cleanup.testData

import kotlin.random.Random
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getRandomFutureTime
import sgtmelon.common.utils.getText
import sgtmelon.common.utils.getTime
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.data.room.extension.inRoomTest
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.bin.BinNoteDialogTest
import sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.notes.NotesNoteDialogTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.test.common.nextString

/**
 * Class which fill db and provide data for tests.
 */
// TODO replace unsafe operations
class DbDelegator(
    override val roomProvider: RoomProvider,
    private val preferencesRepo: PreferencesRepo
) : IRoomWork {

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()
    private val rankConverter = RankConverter()
    private val alarmConverter = AlarmConverter()

    /**
     * For [NotesNoteDialogTest] and [BinNoteDialogTest] need filled [NoteEntity.name] by default.
     */
    val textNote: NoteEntity
        get() = NoteEntity().apply {
            create = getTime()
            change = getTime()
            name = nextString()
            text = nextString().repeat(n = (1 until 10).random())
            color = Color.values().random()
            type = NoteType.TEXT
        }

    /**
     * For [NotesNoteDialogTest] and [BinNoteDialogTest] need filled [NoteEntity.name] by default.
     */
    val rollNote: NoteEntity
        get() = NoteEntity().apply {
            create = getTime()
            change = getTime()
            name = nextString()
            color = Color.values().random()
            type = NoteType.ROLL
        }

    val isVisible: Boolean get() = Random.nextBoolean()

    val rollList: ArrayList<RollEntity>
        get() = ArrayList<RollEntity>().apply {
            for (i in 0 until (1 until 6).random()) {
                add(rollEntity.apply {
                    position = i
                    text = "$i | $text"
                })
            }
        }

    val rollEntity: RollEntity
        get() = RollEntity().apply {
            isCheck = Random.nextBoolean()
            text = nextString()
        }

    val rankEntity: RankEntity get() = RankEntity(name = nextString())


    fun createNote(): NoteItem = if (Random.nextBoolean()) createText() else createRoll()

    fun createText(): NoteItem.Text = NoteItem.Text.getCreate(preferencesRepo.defaultColor)

    fun createRoll(): NoteItem.Roll = NoteItem.Roll.getCreate(preferencesRepo.defaultColor)


    fun insertRank(entity: RankEntity = rankEntity): RankItem {
        inRoomTest { entity.id = rankDao.insertSafe(entity) ?: throw NullPointerException() }

        return rankConverter.toItem(entity)
    }

    fun insertRankForNotes(
        entity: RankEntity = rankEntity,
        type: NoteType = NoteType.values().random()
    ): Pair<RankItem, NoteItem> {
        val noteItem = insertNote(type = type)

        val rankItem = insertRank(entity.apply {
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

    fun insertRankForBin(
        entity: RankEntity = rankEntity,
        type: NoteType = NoteType.values().random()
    ): Pair<RankItem, NoteItem> {
        val noteItem = insertNoteToBin(type = type)

        val rankItem = insertRank(entity.apply {
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

    fun insertTextToBin(entity: NoteEntity = textNote) = insertText(entity.apply { isBin = true })

    fun insertRoll(
        entity: NoteEntity = rollNote,
        isVisible: Boolean = this.isVisible,
        list: ArrayList<RollEntity> = rollList
    ): NoteItem.Roll {
        inRoomTest {
            entity.id = noteDao.insert(entity)

            for (it in list) {
                it.noteId = entity.id
                rollDao.insert(it)
            }

            rollVisibleDao.insert(RollVisibleEntity(noteId = entity.id, value = isVisible))
        }

        val item = noteConverter.toItem(entity, isVisible, rollConverter.toItem(list))

        if (item !is NoteItem.Roll) throw IllegalAccessException("Wrong note type")

        item.updateComplete()

        inRoomTest { noteDao.update(noteConverter.toEntity(item)) }

        return item
    }

    fun insertRollToBin(
        entity: NoteEntity = rollNote,
        isVisible: Boolean = this.isVisible,
        list: ArrayList<RollEntity> = rollList
    ): NoteItem.Roll {
        return insertRoll(entity.apply { isBin = true }, isVisible, list)
    }


    fun insertNote(
        time: String? = null,
        type: NoteType = NoteType.values().random()
    ): NoteItem {
        val entity = when (type) {
            NoteType.TEXT -> textNote
            NoteType.ROLL -> rollNote
        }

        if (time != null) {
            entity.change = time
            entity.create = time
        }

        return when (type) {
            NoteType.TEXT -> insertText(entity)
            NoteType.ROLL -> insertRoll(entity)
        }
    }

    fun insertNoteToBin(
        time: String? = null,
        type: NoteType = NoteType.values().random()
    ): NoteItem {
        val entity = when (type) {
            NoteType.TEXT -> textNote
            NoteType.ROLL -> rollNote
        }

        if (time != null) {
            entity.change = time
            entity.create = time
        }

        return when (type) {
            NoteType.TEXT -> insertTextToBin(entity)
            NoteType.ROLL -> insertRollToBin(entity)
        }
    }

    fun insertNotification(
        item: NoteItem = insertNote(),
        date: String = getRandomFutureTime()
    ): NoteItem {
        item.alarmDate = date

        inRoomTest { item.alarmId = alarmDao.insert(alarmConverter.toEntity(item)) }

        return item
    }


    fun fillRank(count: Int = 10) = ArrayList<RankItem>().apply {
        for (i in 0 until count) {
            add(insertRank(rankEntity.apply {
                name = "$i | $name"
                position = i
                isVisible = Random.nextBoolean()
            }))
        }
    }

    fun fillRankRelation(count: Int = 10) = ArrayList<RankItem>().apply {
        for (i in 0 until count) {
            val noteCount = (0 until 5).random()
            val noteList = ArrayList<NoteItem>().apply {
                repeat(noteCount) { add(insertNote()) }
            }

            val rankItem = insertRank(RankEntity(
                noteId = noteList.map { item -> item.id }.toMutableList(),
                position = i,
                name = "$i | ${nextString()}",
                isVisible = Random.nextBoolean()
            ))

            add(rankItem)

            inRoomTest {
                for (item in noteList) {
                    noteDao.update(noteConverter.toEntity(item.apply {
                        rankId = rankItem.id
                        rankPs = rankItem.position
                    }))
                }
            }
        }
    }

    fun fillNotes(count: Int = 10) = ArrayList<NoteItem>().apply {
        for (i in count downTo 0) {
            add(insertNote(getCalendarWithAdd(i).getText()))
        }
    }

    fun fillBin(count: Int = 10) = ArrayList<NoteItem>().apply {
        for (i in count downTo 0) {
            add(insertNoteToBin(getCalendarWithAdd(i).getText()))
        }
    }

    fun fillNotification(count: Int = 10) = repeat(count) {
        insertNotification(date = getRandomFutureTime())
    }

    fun clear() = apply { inRoomTest { clearAllTables() } }

}