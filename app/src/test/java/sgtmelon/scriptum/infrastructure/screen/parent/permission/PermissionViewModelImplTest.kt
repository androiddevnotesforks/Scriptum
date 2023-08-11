package sgtmelon.scriptum.infrastructure.screen.parent.permission

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.model.PermissionKey
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.test.common.nextString
import kotlin.random.Random

/**
 * Test for [PermissionViewModelImpl]
 */
class PermissionViewModelImplTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val viewModel by lazy { PermissionViewModelImpl(preferencesRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo)
    }

    @Test fun isCalled() {
        val key = PermissionKey(nextString())
        val result = Random.nextBoolean()

        every { preferencesRepo.isPermissionCalled(key) } returns result

        assertEquals(viewModel.isCalled(key), result)

        verifySequence {
            preferencesRepo.isPermissionCalled(key)
        }
    }

    @Test fun setCalled() {
        val key = PermissionKey(nextString())

        every { preferencesRepo.setPermissionCalled(key) } returns Unit

        viewModel.setCalled(key)

        verifySequence {
            preferencesRepo.setPermissionCalled(key)
        }
    }
}