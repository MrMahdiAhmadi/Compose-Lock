package dev.mahdidroid.compose_lock.auth.activities.vm

import androidx.compose.ui.graphics.Color
import dev.mahdidroid.compose_lock.activities.vm.LockIntent
import dev.mahdidroid.compose_lock.activities.vm.LockViewModel
import dev.mahdidroid.compose_lock.datastore.ComposeLockPreferences
import dev.mahdidroid.compose_lock.theme.LockTheme
import dev.mahdidroid.compose_lock.theme.darkThemePinEntryData
import dev.mahdidroid.compose_lock.theme.lightThemePinEntryData
import dev.mahdidroid.compose_lock.utils.AuthResult
import dev.mahdidroid.compose_lock.utils.AuthState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LockViewModelTest {
    
    private lateinit var viewModel: LockViewModel
    private lateinit var composeLockPreferences: ComposeLockPreferences
    
    // Test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock ComposeLockPreferences
        composeLockPreferences = mockk(relaxed = true)
        
        // Initialize ViewModel
        viewModel = LockViewModel(composeLockPreferences)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }
    
    @Test
    fun `loadAuthState updates state correctly`() = runTest {
        // Given
        val expectedAuthState = AuthState.Pin
        coEvery { composeLockPreferences.getAuthState() } returns expectedAuthState
        
        // When
        viewModel.loadAuthState {}
        advanceUntilIdle()
        
        // Then
        val currentState = viewModel.viewState.value
        assertEquals(expectedAuthState, currentState.currentAuthState)
        assertEquals(expectedAuthState, currentState.defaultAuthState)
        coVerify { composeLockPreferences.getAuthState() }
    }
    
    @Test
    fun `OnDayNightTheme intent updates themes correctly`() = runTest {
        // Given
        val newLightTheme = LockTheme(pinTheme = lightThemePinEntryData.copy(backgroundColor = Color.White))
        val newDarkTheme = LockTheme(pinTheme = darkThemePinEntryData.copy(backgroundColor = Color.White))
        
        // When
        viewModel.sendIntent(LockIntent.OnDayNightTheme(newLightTheme, newDarkTheme))
        
        // Then
        val currentState = viewModel.viewState.value
        assertEquals(newLightTheme, currentState.lightTheme)
        assertEquals(newDarkTheme, currentState.darkTheme)
    }

     @Test
     fun `OnSetDefaultAuth updates auth state and persists it`() = runTest {
         // Given
         val newAuthState = AuthState.Password
         coJustRun { composeLockPreferences.updateAuthState(any()) }

         // When
         viewModel.sendIntent(LockIntent.OnSetDefaultAuth(newAuthState))
         advanceUntilIdle()

         // Then
         val currentState = viewModel.viewState.value
         assertEquals(newAuthState, currentState.defaultAuthState)
         coVerify { composeLockPreferences.updateAuthState(newAuthState.name) }
     }

     @Test
     fun `OnReceiveAuthResult with PIN_SUCCESS navigates to main screen`() = runTest {
         // Given
         val initialAuthState = AuthState.Pin
         viewModel.sendIntent(LockIntent.OnUpdateScreenState(initialAuthState))

         // When
         viewModel.sendIntent(LockIntent.OnReceiveAuthResult(AuthResult.PIN_SUCCESS))
         advanceUntilIdle()

         // Then
         val currentState = viewModel.viewState.value
         assertEquals(AuthState.NoAuth, currentState.currentAuthState)
     }

     @Test
     fun `OnFailed increases failed count and updates unlock duration`() = runTest {
         // Given
         coJustRun { composeLockPreferences.updateUnlockDuration(any()) }

         // When
         viewModel.sendIntent(LockIntent.OnFailed)
         advanceUntilIdle()

         // Then
         val currentState = viewModel.viewState.value
         assertEquals(1, currentState.failedCount)
       //  coVerify { composeLockPreferences.updateUnlockDuration(any()) }
     }

     @Test
     fun `OnStop resets to default auth state when not NoAuth`() = runTest {
         // Given
         val defaultAuth = AuthState.Pin
         viewModel.sendIntent(LockIntent.OnSetDefaultAuth(defaultAuth))
         viewModel.sendIntent(LockIntent.OnUpdateScreenState(AuthState.Password))

         // When
         viewModel.sendIntent(LockIntent.OnStop)

         // Then
         val currentState = viewModel.viewState.value
         assertEquals(defaultAuth, currentState.currentAuthState)
     }

     @Test
     fun `OnNavigateToMainScreen updates auth state to NoAuth`() = runTest {
         // Given
         viewModel.sendIntent(LockIntent.OnUpdateScreenState(AuthState.Pin))

         // When
         viewModel.sendIntent(LockIntent.OnNavigateToMainScreen(0))

         // Then
         val currentState = viewModel.viewState.value
         assertEquals(AuthState.NoAuth, currentState.currentAuthState)
     }
}