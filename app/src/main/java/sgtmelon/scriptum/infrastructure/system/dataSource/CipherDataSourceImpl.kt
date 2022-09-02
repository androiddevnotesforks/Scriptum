package sgtmelon.scriptum.infrastructure.system.dataSource

import android.util.Base64
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource

/**
 * Class encrypting/decrypting text with [Base64].
 */
class CipherDataSourceImpl : CipherDataSource {

    override fun encrypt(text: String): String {
        return String(Base64.encode(text.toByteArray(), Base64.URL_SAFE))
    }

    override fun decrypt(text: String): String {
        return String(Base64.decode(text.toByteArray(), Base64.URL_SAFE))
    }
}