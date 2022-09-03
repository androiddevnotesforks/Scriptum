package sgtmelon.scriptum.data.backup

import io.mockk.every
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.parent.ParentBackupTest
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.exception.EnumConverterException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Test for [BackupJsonConverter].
 */
class BackupJsonConverterTest : ParentBackupTest() {

    private val colorConverter = ColorConverter()
    private val typeConverter = NoteTypeConverter()
    private val stringConverter = StringConverter()

    private val jsonConverter = BackupJsonConverter(colorConverter, typeConverter, stringConverter)

    @Test fun `toJson noteEntity`() {
        for ((i, entity) in noteEntityList.withIndex()) {
            assertEquals(jsonConverter.toJson(entity).toString(), noteJsonList[i])
        }
    }

    @Test fun `toJson rollEntity`() {
        for ((i, entity) in rollEntityList.withIndex()) {
            assertEquals(jsonConverter.toJson(entity).toString(), rollJsonList[i])
        }
    }

    @Test fun `toJson rollVisibleEntity`() {
        for ((i, entity) in rollVisibleEntityList.withIndex()) {
            assertEquals(jsonConverter.toJson(entity).toString(), rollVisibleJsonList[i])
        }
    }

    @Test fun `toJson rankEntity`() {
        for ((i, entity) in rankEntityList.withIndex()) {
            assertEquals(jsonConverter.toJson(entity).toString(), rankJsonList[i])
        }
    }

    @Test fun `toJson alarmEntity`() {
        for ((i, entity) in alarmEntityList.withIndex()) {
            assertEquals(jsonConverter.toJson(entity).toString(), alarmJsonList[i])
        }
    }

    @Test fun `toNoteV1 with bad result`() {
        FastMock.fireExtensions()
        every { any<EnumConverterException>().record() } returns Unit

        assertNull(jsonConverter.toNoteV1(JSONObject(noteBadColorJson)))
        assertNull(jsonConverter.toNoteV1(JSONObject(noteBadTypeJson)))
    }

    @Test fun toNoteV1() {
        for ((i, json) in noteJsonList.map { JSONObject(it) }.withIndex()) {
            assertEquals(jsonConverter.toNoteV1(json), noteEntityList[i])
        }
    }

    @Test fun toRollV1() {
        for ((i, json) in rollJsonList.map { JSONObject(it) }.withIndex()) {
            assertEquals(jsonConverter.toRollV1(json), rollEntityList[i])
        }
    }

    @Test fun toRollVisibleV1() {
        for ((i, json) in rollVisibleJsonList.map { JSONObject(it) }.withIndex()) {
            assertEquals(jsonConverter.toRollVisibleV1(json), rollVisibleEntityList[i])
        }
    }

    @Test fun toRankV1() {
        for ((i, json) in rankJsonList.map { JSONObject(it) }.withIndex()) {
            assertEquals(jsonConverter.toRankV1(json), rankEntityList[i])
        }
    }

    @Test fun toAlarmV1() {
        for ((i, json) in alarmJsonList.map { JSONObject(it) }.withIndex()) {
            assertEquals(jsonConverter.toAlarmV1(json), alarmEntityList[i])
        }
    }
}