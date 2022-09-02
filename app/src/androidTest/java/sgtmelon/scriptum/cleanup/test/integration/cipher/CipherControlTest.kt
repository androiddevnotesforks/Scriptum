package sgtmelon.scriptum.cleanup.test.integration.cipher

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.control.cipher.CipherDataSourceImpl
import sgtmelon.scriptum.parent.ParentTest

/**
 * Test for [CipherDataSourceImpl].
 */
@RunWith(AndroidJUnit4::class)
class CipherControlTest : ParentTest() {

    private val dataSource = CipherDataSourceImpl()

    @Test fun encryptAndDecrypt() {
        val decryptText = "Hello my имя is Алексей\n123 =//:url.wood\n-_-|$%^&!@#=[{\"\"'/|\\}]"
        assertEquals(decryptText, dataSource.decrypt(dataSource.encrypt(decryptText)))
    }
}