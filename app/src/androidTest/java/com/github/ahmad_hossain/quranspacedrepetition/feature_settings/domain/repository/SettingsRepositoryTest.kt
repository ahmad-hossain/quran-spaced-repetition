package com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.data.data_source.UserPreferencesSerializer
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.data.repository.SettingsRepositoryImpl
import com.github.ahmad_hossain.quranspacedrepetition.feature_settings.domain.model.UserPreferences
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SettingsRepositoryTest {

    private val testContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())
    private val testDataStore: DataStore<UserPreferences> = DataStoreFactory.create(
        scope = testScope,
        serializer = UserPreferencesSerializer,
        produceFile = { testContext.dataStoreFile("test-prefs.json") }
    )
    private val repository: SettingsRepository = SettingsRepositoryImpl(testDataStore)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun foo() = testScope.runTest {
        val userPref = UserPreferences(
            startPage = 1,
            endPage = 2,
            notificationTime = LocalTime.of(3, 4)
        )
        repository.updateDatastore { userPref }
        assertThat(testDataStore.data.first()).isEqualTo(userPref)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testScope.cancel()
    }
}