package sgtmelon.scriptum.test.integration.cipher

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.control.cipher.CipherControl
import sgtmelon.scriptum.test.parent.ParentTest

/**
 * Test for [CipherControl].
 */
@RunWith(AndroidJUnit4::class)
class CipherControlTest : ParentTest() {

    private val cipherControl = CipherControl()

    @Test fun encryptAndDecrypt() {
        val decryptText = "Hello my имя is Алексей\n123 =//:url.wood\n-_-|$%^&!@#=[{\"\"'/|\\}]"
        assertEquals(decryptText, cipherControl.decrypt(cipherControl.encrypt(decryptText)))
    }
}