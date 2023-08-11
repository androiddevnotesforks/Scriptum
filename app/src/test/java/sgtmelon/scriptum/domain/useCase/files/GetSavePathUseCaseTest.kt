package sgtmelon.scriptum.domain.useCase.files

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.test.common.nextString

/**
 * Test for [GetSavePathUseCase].
 */
class GetSavePathUseCaseTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var dataSource: FileDataSource

    private val useCase by lazy { GetSavePathUseCase(dataSource) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(dataSource)
    }

    @Test fun invoke() {
        val path = nextString()

        every { dataSource.savePath } returns path

        assertEquals(useCase(), path)

        verifySequence {
            dataSource.savePath
        }
    }

}