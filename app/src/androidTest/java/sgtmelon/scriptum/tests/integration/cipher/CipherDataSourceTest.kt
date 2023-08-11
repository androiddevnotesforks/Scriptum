package sgtmelon.scriptum.tests.integration.cipher

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.system.dataSource.CipherDataSourceImpl
import sgtmelon.scriptum.source.ParentTest

/**
 * Test for [CipherDataSourceImpl].
 */
@RunWith(AndroidJUnit4::class)
class CipherDataSourceTest : ParentTest() {

    private val dataSource = CipherDataSourceImpl()

    @Test fun encryptAndDecrypt() {
        val decryptText = "Hello my имя is Алексей\n123 =//:url.wood\n-_-|$%^&!@#=[{\"\"'/|\\}]"
        assertEquals(dataSource.decrypt(dataSource.encrypt(decryptText)), decryptText)
    }

    @Test fun encryptWithEmptyText() = assertEquals(dataSource.encrypt(text = ""), "")

    @Test fun decryptWithEmptyText() = assertEquals(dataSource.decrypt(text = ""), "")

    // TODO test bad base64

}