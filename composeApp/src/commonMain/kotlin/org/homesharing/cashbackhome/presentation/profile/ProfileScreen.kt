package org.homesharing.cashbackhome.presentation.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.arrow_back
import cashbackhome.composeapp.generated.resources.back_button_description
import cashbackhome.composeapp.generated.resources.calendar
import cashbackhome.composeapp.generated.resources.default_profile_picture
import cashbackhome.composeapp.generated.resources.edit
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.homesharing.cashbackhome.data.local.database.entity.ThemeMode
import org.homesharing.cashbackhome.domain.model.AuthenticatedUser
import org.homesharing.cashbackhome.presentation.home.ChooseOrSaveButton
import org.homesharing.cashbackhome.presentation.theme.CashbackHomeTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Instant

private val ProfileTextSecondary = Color(0xFF8A8A8A)
private val ProfileBorder = Color(0xFFE5E5E5)
private val ProfileAccent = Color(0xFFB7DC3A)
private val ProfileAccentLight = Color(0xFFEAF5B8)
private val ProfileLogout = Color(0xFFC84646)
private val ProfileLogoutBackground = ProfileLogout.copy(alpha = 0.05f)
private val ProfileLogoutBorder = ProfileLogout.copy(alpha = 0.2f)

@Composable
internal fun ProfileScreenRoot(
    user: AuthenticatedUser,
    onUserChanged: (AuthenticatedUser) -> Unit,
    onBackClick: () -> Unit,
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    var userDraft by remember(user) { mutableStateOf(user) }
    val displayedUser = if (state.isEditMode) userDraft else user
    val screenState = state.copy(
        fields = ProfileEditFields(
            login = displayedUser.username,
            password = state.fields.password,
            email = displayedUser.email,
            birthDate = displayedUser.birthDate,
            phone = displayedUser.phone,
        )
    )

    ProfileScreen(
        state = screenState,
        onBackClick = onBackClick,
        onEditClick = {
            if (state.isEditMode) {
                onUserChanged(userDraft.trimmed())
            } else {
                userDraft = user
            }
            viewModel.onEditClick()
        },
        onThemeModeSelected = viewModel::onThemeModeSelected,
        onLoginChanged = { userDraft = userDraft.copy(username = it) },
        onPasswordChanged = viewModel::onPasswordChanged,
        onEmailChanged = { userDraft = userDraft.copy(email = it) },
        onBirthDateChanged = { userDraft = userDraft.copy(birthDate = it) },
        onPhoneChanged = { userDraft = userDraft.copy(phone = it) },
    )
}

private fun AuthenticatedUser.trimmed(): AuthenticatedUser =
    copy(
        username = username.trim(),
        email = email.trim(),
        birthDate = birthDate.trim(),
        phone = phone.trim(),
    )

@Composable
private fun ProfileScreen(
    state: ProfileScreenState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onBirthDateChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showBirthDatePicker by remember { mutableStateOf(false) }
    var showLogoutConfirmationDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val blockBorder = profileBlockBorderColor()
    val fieldBorder = if (state.isEditMode) {
        ProfileAccent.copy(alpha = 0.45f)
    } else {
        blockBorder
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(horizontal = 36.dp, vertical = 12.dp),
            ) { snackbarData ->
                Snackbar(
                    shape = RoundedCornerShape(26.dp),
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileTopBar(
                onBackClick = onBackClick,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 430.dp)
                    .padding(horizontal = 16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    ProfileAvatar(
                        onEditClick = onEditClick,
                    )
                    Spacer(modifier = Modifier.height(26.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        ProfileInfoField(
                            title = "Логин",
                            value = state.fields.login,
                            isEditMode = state.isEditMode,
                            borderColor = fieldBorder,
                            onValueChange = onLoginChanged,
                        )
                        ProfileInfoField(
                            title = "Пароль",
                            value = state.fields.password,
                            isEditMode = state.isEditMode,
                            borderColor = fieldBorder,
                            visualTransformation = if (isPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation(mask = '\u2022')
                            },
                            onValueChange = onPasswordChanged,
                            trailingIcon = {
                                EyeButton(
                                    isVisible = isPasswordVisible,
                                    onClick = { isPasswordVisible = !isPasswordVisible },
                                )
                            },
                        )
                        ProfileInfoField(
                            title = "Email",
                            value = state.fields.email,
                            isEditMode = state.isEditMode,
                            borderColor = fieldBorder,
                            onValueChange = onEmailChanged,
                        )
                        ProfileInfoField(
                            title = "Дата Рождения",
                            value = state.fields.birthDate,
                            isEditMode = state.isEditMode,
                            borderColor = fieldBorder,
                            onValueChange = onBirthDateChanged,
                            onClick = { showBirthDatePicker = true },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(Res.drawable.calendar),
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = ProfileAccent,
                                )
                            },
                        )
                        ProfileInfoField(
                            title = "Телефон",
                            value = state.fields.phone,
                            isEditMode = state.isEditMode,
                            borderColor = fieldBorder,
                            onValueChange = onPhoneChanged,
                            leadingIcon = {
                                PhoneIcon(
                                    modifier = Modifier.size(15.dp),
                                    color = ProfileAccent,
                                )
                            },
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    if (state.isEditMode) {
                        SaveButton(onClick = onEditClick)
                    } else {
                        ThemeSettingsCard(
                            themeMode = state.themeMode,
                            onThemeModeSelected = onThemeModeSelected,
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        LogoutButton(onClick = { showLogoutConfirmationDialog = true })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    if (showBirthDatePicker) {
        ProfileDatePickerDialog(
            initialDate = state.fields.birthDate,
            onDismiss = { showBirthDatePicker = false },
            onConfirm = { selectedDate ->
                onBirthDateChanged(formatProfileBirthDate(selectedDate))
                showBirthDatePicker = false
            },
        )
    }

    if (showLogoutConfirmationDialog) {
        LogoutConfirmationDialog(
            onDismiss = { showLogoutConfirmationDialog = false },
            onConfirm = {
                showLogoutConfirmationDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Аутентификация пользователя скоро появится")
                }
            },
        )
    }
}

@Composable
private fun profileBlockBorderColor(): Color =
    if (MaterialTheme.colorScheme.surface.luminance() < 0.5f) {
        MaterialTheme.colorScheme.surface
    } else {
        ProfileBorder
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileDatePickerDialog(
    initialDate: String,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
) {
    val initialDateMillis = parseProfileBirthDate(initialDate)?.toPickerMillis()
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        initialDisplayedMonthMillis = initialDateMillis,
        yearRange = 1900..2035,
        initialDisplayMode = DisplayMode.Picker,
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(max = 420.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
        ) {
            Column {
                DatePicker(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    showModeToggle = true,
                    title = {
                        Text(
                            text = "Выберите дату рождения",
                            modifier = Modifier.padding(
                                start = 28.dp,
                                top = 22.dp,
                                end = 28.dp,
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    headline = {
                        Text(
                            text = formatProfileDatePickerHeadline(state.selectedDateMillis),
                            modifier = Modifier.padding(
                                start = 28.dp,
                                top = 18.dp,
                                bottom = 18.dp,
                            ),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        headlineContentColor = MaterialTheme.colorScheme.onBackground,
                        weekdayContentColor = MaterialTheme.colorScheme.onBackground,
                        subheadContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationContentColor = MaterialTheme.colorScheme.onBackground,
                        dayContentColor = MaterialTheme.colorScheme.onBackground,
                        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                        selectedDayContentColor = MaterialTheme.colorScheme.background,
                        todayContentColor = MaterialTheme.colorScheme.primary,
                        todayDateBorderColor = MaterialTheme.colorScheme.primary,
                    ),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextButton(
                        onClick = { state.selectedDateMillis = null },
                    ) {
                        Text(
                            text = "Очистить",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    Row {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = "Отменить",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }

                        TextButton(
                            enabled = state.selectedDateMillis != null,
                            onClick = {
                                state.selectedDateMillis?.toPickerDate()?.let(onConfirm)
                            },
                        ) {
                            Text(
                                text = "OK",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LogoutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .widthIn(max = 360.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 24.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 14.dp,
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Выйти из аккаунта?",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Text(
                            text = "×",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Отменить",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    TextButton(onClick = onConfirm) {
                        Text(
                            text = "Выйти",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = ProfileLogout,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Профиль",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = stringResource(Res.string.back_button_description),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@Composable
private fun ProfileAvatar(
    onEditClick: () -> Unit,
) {
    Box(
        modifier = Modifier.size(144.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.default_profile_picture),
            contentDescription = "Фото профиля",
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = (-12).dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(ProfileAccent)
                .border(1.dp, ProfileAccent, CircleShape)
                .clickable(onClick = onEditClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.edit),
                contentDescription = "Редактировать",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(13.dp),
            )
        }
    }
}

@Composable
private fun ProfileInfoField(
    title: String,
    value: String,
    isEditMode: Boolean,
    borderColor: Color,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
    onClick: (() -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val fieldModifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(MaterialTheme.colorScheme.surface)
        .border(1.dp, borderColor, RoundedCornerShape(10.dp))
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    enabled = isEditMode,
                    onClick = onClick,
                )
            } else {
                Modifier
            }
        )
        .padding(horizontal = 12.dp)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
        )

        Row(
            modifier = fieldModifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke()
            if (leadingIcon != null) {
                Spacer(modifier = Modifier.width(6.dp))
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = isEditMode && onClick == null,
                singleLine = true,
                visualTransformation = visualTransformation,
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )
            trailingIcon?.invoke()
        }
    }
}

@Composable
private fun EyeButton(
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        EyeIcon(
            modifier = Modifier.size(20.dp),
            color = ProfileTextSecondary,
            isVisible = isVisible,
        )
    }
}

@Composable
private fun ThemeSettingsCard(
    themeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Тема оформления",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            ThemeOption(
                label = "Светлая",
                mode = ThemeMode.Light,
                selected = themeMode == ThemeMode.Light,
                onClick = onThemeModeSelected,
            )
            ThemeOption(
                label = "Тёмная",
                mode = ThemeMode.Dark,
                selected = themeMode == ThemeMode.Dark,
                onClick = onThemeModeSelected,
            )
            ThemeOption(
                label = "Системная",
                mode = ThemeMode.System,
                selected = themeMode == ThemeMode.System,
                onClick = onThemeModeSelected,
            )
        }
    }
}

@Composable
private fun ThemeOption(
    label: String,
    mode: ThemeMode,
    selected: Boolean,
    onClick: (ThemeMode) -> Unit,
) {
    Column(
        modifier = Modifier
            .width(92.dp)
            .clickable { onClick(mode) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        ThemePreviewCard(
            mode = mode,
            selected = selected,
        )

        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ThemePreviewCard(
    mode: ThemeMode,
    selected: Boolean,
) {
    val shape = RoundedCornerShape(10.dp)
    val blockBorder = profileBlockBorderColor()
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.onBackground
    } else {
        blockBorder
    }
    val borderWidth = if (selected) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .size(width = 84.dp, height = 78.dp)
            .clip(shape)
            .background(Color.White)
            .border(borderWidth, borderColor, shape),
    ) {
        when (mode) {
            ThemeMode.Light -> LightThemePreview()
            ThemeMode.Dark -> DarkThemePreview()
            ThemeMode.System -> SystemThemePreview()
        }

        if (selected) {
            SelectedThemeIndicator(
                isDarkThemePreview = mode == ThemeMode.Dark,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp),
            )
        }
    }
}

@Composable
private fun LightThemePreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFCFC)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color(0xFFF1F1F1)),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 12.dp),
        ) {
            Text(
                text = "Aa",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF1D1D1F),
            )
        }
    }
}

@Composable
private fun DarkThemePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111111))
            .padding(start = 10.dp, top = 32.dp),
    ) {
        Text(
            text = "Aa",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
        )
    }
}

@Composable
private fun SystemThemePreview() {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color(0xFF111111))
                .padding(start = 8.dp, top = 32.dp),
        ) {
            Text(
                text = "Aa",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color(0xFFFCFCFC))
                .padding(start = 8.dp, top = 32.dp),
        ) {
            Text(
                text = "Aa",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF1D1D1F),
            )
        }
    }
}

@Composable
private fun SelectedThemeIndicator(
    isDarkThemePreview: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isDarkThemePreview) MaterialTheme.colorScheme.primary else Color(0xFF111111)
    val checkColor = if (isDarkThemePreview) MaterialTheme.colorScheme.onPrimary else Color.White
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(10.dp)) {
            drawLine(
                color = checkColor,
                start = Offset(size.width * 0.08f, size.height * 0.52f),
                end = Offset(size.width * 0.38f, size.height * 0.82f),
                strokeWidth = 1.8.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawLine(
                color = checkColor,
                start = Offset(size.width * 0.38f, size.height * 0.82f),
                end = Offset(size.width * 0.94f, size.height * 0.16f),
                strokeWidth = 1.8.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
private fun SaveButton(onClick: () -> Unit) {
    ChooseOrSaveButton(
        onClick = onClick,
        text = "Сохранить"
    )
}

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(ProfileLogoutBackground)
            .border(1.dp, ProfileLogoutBorder, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LogoutIcon(
            modifier = Modifier.size(18.dp),
            color = ProfileLogout,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Выйти",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = ProfileLogout,
            maxLines = 1,
        )
    }
}

@Composable
private fun EyeIcon(
    modifier: Modifier,
    color: Color,
    isVisible: Boolean,
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.6.dp.toPx()
        drawOval(
            color = color,
            topLeft = Offset(size.width * 0.08f, size.height * 0.25f),
            size = Size(size.width * 0.84f, size.height * 0.5f),
            style = Stroke(width = strokeWidth),
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.13f,
            center = Offset(size.width / 2f, size.height / 2f),
            style = Stroke(width = strokeWidth),
        )
        if (!isVisible) {
            drawLine(
                color = color,
                start = Offset(size.width * 0.18f, size.height * 0.82f),
                end = Offset(size.width * 0.82f, size.height * 0.18f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
private fun PhoneIcon(
    modifier: Modifier,
    color: Color,
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.8.dp.toPx()
        val path = Path().apply {
            moveTo(size.width * 0.28f, size.height * 0.16f)
            cubicTo(
                size.width * 0.14f,
                size.height * 0.25f,
                size.width * 0.24f,
                size.height * 0.58f,
                size.width * 0.46f,
                size.height * 0.74f,
            )
            cubicTo(
                size.width * 0.64f,
                size.height * 0.88f,
                size.width * 0.86f,
                size.height * 0.8f,
                size.width * 0.82f,
                size.height * 0.66f,
            )
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
    }
}

@Composable
private fun LogoutIcon(
    modifier: Modifier,
    color: Color,
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.8.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.14f, size.height * 0.18f),
            end = Offset(size.width * 0.14f, size.height * 0.82f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.14f, size.height * 0.18f),
            end = Offset(size.width * 0.46f, size.height * 0.18f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.14f, size.height * 0.82f),
            end = Offset(size.width * 0.46f, size.height * 0.82f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.4f, size.height * 0.5f),
            end = Offset(size.width * 0.88f, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.7f, size.height * 0.34f),
            end = Offset(size.width * 0.88f, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.7f, size.height * 0.66f),
            end = Offset(size.width * 0.88f, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

private val RussianMonthNames = listOf(
    "января",
    "февраля",
    "марта",
    "апреля",
    "мая",
    "июня",
    "июля",
    "августа",
    "сентября",
    "октября",
    "ноября",
    "декабря",
)

private fun parseProfileBirthDate(value: String): LocalDate? {
    val isoDate = runCatching {
        LocalDate.parse(value.trim())
    }.getOrNull()
    if (isoDate != null) {
        return isoDate
    }

    val parts = value
        .trim()
        .replace(",", "")
        .split(" ")
        .filter { it.isNotBlank() }
    if (parts.size < 3) {
        return null
    }

    val day = parts[0].toIntOrNull() ?: return null
    val month = RussianMonthNames.indexOf(parts[1].lowercase()) + 1
    val year = parts[2].toIntOrNull() ?: return null
    if (month == 0) {
        return null
    }

    return runCatching {
        LocalDate(year, month, day)
    }.getOrNull()
}

private fun formatProfileBirthDate(date: LocalDate): String {
    val month = RussianMonthNames[date.month.number - 1]
    return "${date.day} $month, ${date.year}"
}

private fun formatProfileDatePickerHeadline(millis: Long?): String =
    millis?.toPickerDate()?.let(::formatProfileBirthDate) ?: "Дата рождения"

private fun LocalDate.toPickerMillis(): Long =
    atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

private fun Long.toPickerDate(): LocalDate =
    Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date

@Composable
@Preview
private fun ProfileScreenPreview() {
    CashbackHomeTheme {
        ProfileScreen(
            state = ProfileScreenState(),
            onBackClick = {},
            onEditClick = {},
            onThemeModeSelected = {},
            onLoginChanged = {},
            onPasswordChanged = {},
            onEmailChanged = {},
            onBirthDateChanged = {},
            onPhoneChanged = {},
        )
    }
}
