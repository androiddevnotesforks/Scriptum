package sgtmelon.scriptum.data.backup

import sgtmelon.scriptum.cleanup.parent.ParentBackupTest

/**
 * Test for [BackupParserImpl].
 */
class BackupParserImplTest : ParentBackupTest() {
    //
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
}