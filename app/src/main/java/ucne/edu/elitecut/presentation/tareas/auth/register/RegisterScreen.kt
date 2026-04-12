package ucne.edu.elitecut.presentation.tareas.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.elitecut.presentation.utils.InputValidation
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) {
            onRegisterSuccess()
        }
    }

    RegisterBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = onNavigateToLogin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterBody(
    state: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(RegisterUiEvent.UserMessageShown)
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver al login", tint = MaterialTheme.colorScheme.primary)
                }
                Text("Registro de Barbero", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Únete a la Élite", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(4.dp))

            Text("Crea tu cuenta premium para empezar", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(28.dp))

            // Nombre Completo - solo letras y espacios, máx 50
            Text("Nombre Completo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.nombre,
                onValueChange = {
                    val filtered = InputValidation.filterNombreInput(it, 50)
                    onEvent(RegisterUiEvent.OnNombreChange(filtered))
                },
                placeholder = { Text("Ej. Juan Pérez", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                supportingText = { Text("Solo letras y espacios (${state.nombre.length}/50)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth().testTag("input_nombre"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Teléfono - formato XXX-XXX-XXXX
            Text("Teléfono", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.telefono,
                onValueChange = {
                    val formatted = InputValidation.formatPhoneInput(it)
                    onEvent(RegisterUiEvent.OnTelefonoChange(formatted))
                },
                placeholder = { Text("849-381-6768", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                supportingText = { Text("Formato: XXX-XXX-XXXX", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth().testTag("input_telefono"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fecha de Ingreso
            Text("Fecha de Ingreso", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.fechaIngreso, onValueChange = {},
                placeholder = { Text("Seleccionar fecha", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                trailingIcon = {
                    IconButton(onClick = { onEvent(RegisterUiEvent.ShowDatePicker) }) {
                        Icon(Icons.Default.CalendarToday, "Seleccionar fecha", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("input_fecha"),
                singleLine = true, readOnly = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Correo Electrónico - máx 60
            Text("Correo Electrónico", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.correo,
                onValueChange = {
                    val limited = InputValidation.limitLength(it, 60)
                    onEvent(RegisterUiEvent.OnCorreoChange(limited))
                },
                placeholder = { Text("usuario@gmail.com", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                supportingText = { Text("Debe ser una cuenta de Gmail (${state.correo.length}/60)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().testTag("input_correo"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña - máx 30
            Text("Contraseña", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    val limited = InputValidation.limitLength(it, 30)
                    onEvent(RegisterUiEvent.OnPasswordChange(limited))
                },
                placeholder = { Text("• • • • • • • •", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                trailingIcon = {
                    IconButton(onClick = { onEvent(RegisterUiEvent.TogglePasswordVisibility) }) {
                        Icon(if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Toggle password", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                supportingText = { Text("Máximo 30 caracteres (${state.password.length}/30)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().testTag("input_password"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirmar Contraseña - máx 30
            Text("Confirmar", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.confirmarPassword,
                onValueChange = {
                    val limited = InputValidation.limitLength(it, 30)
                    onEvent(RegisterUiEvent.OnConfirmarPasswordChange(limited))
                },
                placeholder = { Text("• • • • • • • •", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                trailingIcon = {
                    IconButton(onClick = { onEvent(RegisterUiEvent.ToggleConfirmarPasswordVisibility) }) {
                        Icon(if (state.isConfirmarPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Toggle confirm password", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                supportingText = { Text("Máximo 30 caracteres (${state.confirmarPassword.length}/30)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                visualTransformation = if (state.isConfirmarPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().testTag("input_confirmar"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { onEvent(RegisterUiEvent.Register) },
                modifier = Modifier.fillMaxWidth().height(52.dp).testTag("btn_register"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Text("Registrarse", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("¿Ya tienes una cuenta? ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Inicia sesión", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNavigateToLogin() })
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (state.showDatePicker) {
            val datePickerState = rememberDatePickerState(
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val todayUtc = java.time.LocalDate.now(java.time.ZoneOffset.UTC)
                            .atStartOfDay(java.time.ZoneOffset.UTC).toInstant().toEpochMilli()
                        return utcTimeMillis >= todayUtc
                    }
                }
            )
            DatePickerDialog(
                onDismissRequest = { onEvent(RegisterUiEvent.HideDatePicker) },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneOffset.UTC).toLocalDate()
                            val formatted = date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            onEvent(RegisterUiEvent.OnFechaIngresoChange(formatted))
                        }
                    }) { Text("Confirmar", color = MaterialTheme.colorScheme.primary) }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(RegisterUiEvent.HideDatePicker) }) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            ) { DatePicker(state = datePickerState) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterBodyPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        RegisterBody(state = RegisterUiState(), onEvent = {}, onNavigateToLogin = {})
    }
}
