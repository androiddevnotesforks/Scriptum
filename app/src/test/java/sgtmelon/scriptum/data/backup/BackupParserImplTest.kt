package sgtmelon.scriptum.data.backup

import sgtmelon.scriptum.cleanup.parent.ParentBackupTest

/**
 * Test for [BackupParserImpl].
 */
class BackupParserImplTest : ParentBackupTest() {

    //    @MockK lateinit var context: Context
    //    @MockK lateinit var selector: BackupParserSelectorImpl
    //
    //        private val backupParser by lazy {
    //            BackupParserImpl(context, selector, colorConverter, typeConverter, stringConverter)
    //        }
    //
    //    private val spyBackupParser by lazy { spyk(backupParser) }
    //
    //    private val tagVersion = nextString()
    //    private val tagHash = nextString()
    //    private val tagDatabase = nextString()
    //
    //    @After override fun tearDown() {
    //        super.tearDown()
    //        confirmVerified(context, selector)
    //    }
    //
    //    @Test fun parse_badData() {
    //        val dataError = nextString()
    //        val versionError = JSONObject().apply { put(tagVersion, nextString()) }.toString()
    //        val hashError = JSONObject().apply { put(tagVersion, Random.nextInt()) }.toString()
    //        val databaseError = JSONObject().apply {
    //            put(tagVersion, Random.nextInt())
    //            put(tagHash, nextString())
    //        }.toString()
    //
    //        mockTag()
    //
    //        assertNull(spyBackupParser.convert(dataError))
    //        assertNull(spyBackupParser.convert(versionError))
    //        assertNull(spyBackupParser.convert(hashError))
    //        assertNull(spyBackupParser.convert(databaseError))
    //
    //        verifySequence {
    //            spyBackupParser.convert(dataError)
    //
    //            spyBackupParser.convert(versionError)
    //            context.getString(R.string.backup_version)
    //
    //            spyBackupParser.convert(hashError)
    //            context.getString(R.string.backup_version)
    //            context.getString(R.string.backup_hash)
    //
    //            spyBackupParser.convert(databaseError)
    //            context.getString(R.string.backup_version)
    //            context.getString(R.string.backup_hash)
    //            context.getString(R.string.backup_database)
    //        }
    //    }
    //
    //    @Test fun parse_badHash() {
    //        val data = getData()
    //        val backupJson = getBackupJson(nextString(), data, Random.nextInt())
    //
    //        mockTag()
    //        every { spyBackupParser.getHash(data) } returns nextString()
    //
    //        assertNull(spyBackupParser.convert(backupJson))
    //
    //        verifySequence {
    //            spyBackupParser.convert(backupJson)
    //
    //            context.getString(R.string.backup_version)
    //            context.getString(R.string.backup_hash)
    //            context.getString(R.string.backup_database)
    //            spyBackupParser.getHash(data)
    //        }
    //    }
    //
    //    @Test fun parse_goodData() {
    //        val model = mockk<ParserResult>()
    //        val data = getData()
    //        val hash = nextString()
    //        val version = Random.nextInt()
    //
    //        val backupJson = getBackupJson(hash, data, version)
    //
    //        mockTag()
    //        every { spyBackupParser.getHash(data) } returns hash
    //        every { selector.parse(data, version) } returns model
    //
    //        assertEquals(model, spyBackupParser.convert(backupJson))
    //
    //        verifySequence {
    //            spyBackupParser.convert(backupJson)
    //
    //            context.getString(R.string.backup_version)
    //            context.getString(R.string.backup_hash)
    //            context.getString(R.string.backup_database)
    //            spyBackupParser.getHash(data)
    //            selector.parse(data, version)
    //        }
    //    }
    //
    //
    //
    //    //region Private help functions
    //
    //    /**
    //     * Imitate the result of collected database.
    //     */
    //    private fun getData() = StringBuilder().apply {
    //        repeat(times = 5) { append(nextString()) }
    //    }.toString()
    //
    //    /**
    //     * Imitate the backup file content.
    //     */
    //    private fun getBackupJson(
    //        hash: String,
    //        data: String,
    //        version: Any = BackupParserImpl.VERSION
    //    ) = JSONObject().apply {
    //        put(tagVersion, version)
    //        put(tagHash, hash)
    //        put(tagDatabase, data)
    //    }.toString()
    //
    //    private fun mockTag() {
    //        every { context.getString(R.string.backup_version) } returns tagVersion
    //        every { context.getString(R.string.backup_hash) } returns tagHash
    //        every { context.getString(R.string.backup_database) } returns tagDatabase
    //    }
    //
    //    //endregion
    //

    // SELECTOR test code

    //
    //    private val colorConverter = ColorConverter()
    //    private val typeConverter = NoteTypeConverter()
    //    private val stringConverter = StringConverter()
    //
    //    private val selector by lazy {
    //        BackupParserSelectorImpl(colorConverter, typeConverter, stringConverter)
    //    }
    //    private val spySelector by lazy { spyk(selector) }
    //
    //    @Test fun parseByVersion() {
    //        val model = mockk<ParserResult>()
    //        val data = nextString()
    //
    //        every { spySelector.getModelV1(data) } returns model
    //
    //        assertNull(spySelector.parse(nextString(), version = -1))
    //        assertEquals(model, spySelector.parse(data, version = 1))
    //
    //        verifySequence {
    //            spySelector.parse(any(), version = -1)
    //
    //            spySelector.parse(data, version = 1)
    //            spySelector.getModelV1(data)
    //        }
    //    }
    //
    //    //region Version 1
    //
    //    @Test fun getModelV1() {
    //        FastMock.fireExtensions()
    //        every { any<BackupParserException>().record() } returns Unit
    //
    //        val badData = nextString()
    //        assertNull(spySelector.getModelV1(badData))
    //
    //        val goodData = JSONObject().apply {
    //            put(Note.TABLE, noteListJson)
    //            put(Roll.TABLE, rollListJson)
    //            put(RollVisible.TABLE, rollVisibleListJson)
    //            put(Rank.TABLE, rankListJson)
    //            put(Alarm.TABLE, alarmListJson)
    //        }.toString()
    //
    //        val parserResult = ParserResult(noteList, rollList, rollVisibleList, rankList, alarmList)
    //
    //        every { spySelector.getNoteTableV1(any()) } throws Throwable()
    //
    //        assertNull(spySelector.getModelV1(goodData))
    //
    //        every { spySelector.getNoteTableV1(any()) } returns noteList
    //        every { spySelector.getRollTableV1(any()) } throws Throwable()
    //
    //        assertNull(spySelector.getModelV1(goodData))
    //
    //        every { spySelector.getRollTableV1(any()) } returns rollList
    //        every { spySelector.getRollVisibleTableV1(any()) } throws Throwable()
    //
    //        assertNull(spySelector.getModelV1(goodData))
    //
    //        every { spySelector.getRollVisibleTableV1(any()) } returns rollVisibleList
    //        every { spySelector.getRankTableV1(any()) } throws Throwable()
    //
    //        assertNull(spySelector.getModelV1(goodData))
    //
    //        every { spySelector.getRankTableV1(any()) } returns rankList
    //        every { spySelector.getAlarmTableV1(any()) } throws Throwable()
    //
    //        assertNull(spySelector.getModelV1(goodData))
    //
    //        every { spySelector.getAlarmTableV1(any()) } returns alarmList
    //
    //        assertEquals(spySelector.getModelV1(goodData), parserResult)
    //
    //        verifySequence {
    //            spySelector.getModelV1(badData)
    //
    //            spySelector.getModelV1(goodData)
    //            spySelector.getNoteTableV1(any())
    //
    //            spySelector.getModelV1(goodData)
    //            spySelector.getNoteTableV1(any())
    //            spySelector.getRollTableV1(any())
    //
    //            spySelector.getModelV1(goodData)
    //            spySelector.getNoteTableV1(any())
    //            spySelector.getRollTableV1(any())
    //            spySelector.getRollVisibleTableV1(any())
    //
    //            spySelector.getModelV1(goodData)
    //            spySelector.getNoteTableV1(any())
    //            spySelector.getRollTableV1(any())
    //            spySelector.getRollVisibleTableV1(any())
    //            spySelector.getRankTableV1(any())
    //
    //            spySelector.getModelV1(goodData)
    //            spySelector.getNoteTableV1(any())
    //            spySelector.getRollTableV1(any())
    //            spySelector.getRollVisibleTableV1(any())
    //            spySelector.getRankTableV1(any())
    //            spySelector.getAlarmTableV1(any())
    //
    //            spySelector.getModelV1(goodData)
    //            spySelector.getNoteTableV1(any())
    //            spySelector.getRollTableV1(any())
    //            spySelector.getRollVisibleTableV1(any())
    //            spySelector.getRankTableV1(any())
    //            spySelector.getAlarmTableV1(any())
    //        }
    //    }
    //
    //    // TODO add tests with throws exception
    //
    //    // TODO add skip test (if converter return null)
    //    @Test fun getNoteTableV1() {
    //        val jsonArray = JSONArray(noteListJson)
    //        assertEquals(selector.getNoteTableV1(jsonArray), noteList)
    //    }
    //
    //    @Test fun getRollTableV1() {
    //        val jsonArray = JSONArray(rollListJson)
    //        assertEquals(selector.getRollTableV1(jsonArray), rollList)
    //    }
    //
    //    @Test fun getRollVisibleTableV1() {
    //        val jsonArray = JSONArray(rollVisibleListJson)
    //        assertEquals(selector.getRollVisibleTableV1(jsonArray), rollVisibleList)
    //    }
    //
    //    @Test fun getRankTableV1() {
    //        val jsonArray = JSONArray(rankListJson)
    //        assertEquals(selector.getRankTableV1(jsonArray), rankList)
    //    }
    //
    //    @Test fun getAlarmTableV1() {
    //        val jsonArray = JSONArray(alarmListJson)
    //        assertEquals(selector.getAlarmTableV1(jsonArray), alarmList)
    //    }
    //
    //    //endregion

}