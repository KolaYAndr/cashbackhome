package org.homesharing.cashbackhome.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.data.local.database.CardCashbackDao
import org.homesharing.cashbackhome.data.local.database.entity.ProfileSettings
import org.homesharing.cashbackhome.data.local.database.entity.ThemeMode

private const val ThemeSettingsId = 1L
private const val StopTimeoutMillis = 5_000L

internal data class ProfileEditFields(
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val birthDate: String = "",
    val phone: String = "",
)

internal data class ProfileScreenState(
    val fields: ProfileEditFields = ProfileEditFields(),
    val themeMode: ThemeMode = ThemeMode.System,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = true,
)

internal class ProfileViewModel(
    private val dao: CardCashbackDao,
) : ViewModel() {
    private val networkDataSource = MockPasswordNetworkDataSource()
    private val passwordFlow = MutableStateFlow<String?>(null)
    private val passwordDraftFlow = MutableStateFlow("")
    private val isEditModeFlow = MutableStateFlow(false)

    val screenState = combine(
        passwordFlow,
        passwordDraftFlow,
        isEditModeFlow,
        dao.getProfileSettings(),
    ) { password, passwordDraft, isEditMode, settings ->
        val visiblePassword = if (isEditMode) {
            passwordDraft
        } else {
            password.orEmpty()
        }

        ProfileScreenState(
            fields = ProfileEditFields(password = visiblePassword),
            themeMode = settings?.themeMode ?: ThemeMode.System,
            isEditMode = isEditMode,
            isLoading = password == null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(StopTimeoutMillis),
        initialValue = ProfileScreenState(),
    )

    init {
        viewModelScope.launch {
            if (dao.getProfileSettings().first() == null) {
                dao.upsertProfileSettings(ProfileSettings(settingsId = ThemeSettingsId))
            }
        }
        viewModelScope.launch {
            val password = networkDataSource.fetchPassword()
            passwordFlow.value = password
            passwordDraftFlow.value = password
        }
    }

    fun onEditClick() {
        if (isEditModeFlow.value) {
            savePassword()
        } else {
            passwordDraftFlow.value = passwordFlow.value.orEmpty()
            isEditModeFlow.value = true
        }
    }

    fun onThemeModeSelected(themeMode: ThemeMode) {
        viewModelScope.launch {
            dao.upsertProfileSettings(
                ProfileSettings(
                    settingsId = ThemeSettingsId,
                    themeMode = themeMode,
                )
            )
        }
    }

    fun onPasswordChanged(value: String) {
        passwordDraftFlow.value = value
    }

    private fun savePassword() {
        viewModelScope.launch {
            val savedPassword = networkDataSource.savePassword(passwordDraftFlow.value)
            passwordFlow.value = savedPassword
            passwordDraftFlow.value = savedPassword
            isEditModeFlow.value = false
        }
    }
}

private class MockPasswordNetworkDataSource {
    private var password = "password"

    suspend fun fetchPassword(): String {
        delay(250)
        return password
    }

    suspend fun savePassword(newPassword: String): String {
        delay(100)
        password = newPassword
        return password
    }
}
