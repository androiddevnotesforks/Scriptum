package sgtmelon.scriptum.tests.integration.cipher

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.system.dataSource.CipherDataSourceImpl
import sgtmelon.scriptum.source.ParentTest

/**
 * Test for [CipherDataSourceImpl].
 */
@RunWith(AndroidJUnit4::class)
class CipherDataSourceImplTest : ParentTest() {

    private val dataSource = CipherDataSourceImpl()

    @Test fun encryptAndDecrypt() {
        val decryptText = "Hello my имя is Алексей\n123 =//:url.wood\n-_-|$%^&!@#=[{\"\"'/|\\}]"
        assertEquals(dataSource.decrypt(dataSource.encrypt(decryptText)), decryptText)
    }

    @Test fun encryptWithEmptyText() = assertEquals(dataSource.encrypt(text = ""), "")

    @Test fun decryptWithEmptyText() = assertEquals(dataSource.decrypt(text = ""), "")

    @Test fun decryptBadData() {
        val badTrimText = """
            dHJhaWxlcgo8PCAvU2l6ZSAxNSAvUm9vdCAxIDAgUiAvSW5mbyAyIDAgUgovSUQgWyhcMDAyXDMz
            MHtPcFwyNTZbezU/VzheXDM0MXFcMzExKShcMDAyXDMzMHtPcFwyNTZbezU/VzheXDM0MXFcMzEx
            KV0KPj4Kc3RhcnR4cmVmCjY3MDEKJSVFT0YK

            --_=ic0008m4wtZ4TqBFd+sXC8--
        """

        assertNull(dataSource.decrypt(badTrimText))
    }
}