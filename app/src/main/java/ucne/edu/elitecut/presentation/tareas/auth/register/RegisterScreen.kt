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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import ucne.edu.elitecut.presentation.utils.FutureDatePickerDialog
import ucne.edu.elitecut.presentation.utils.InputValidation
import ucne.edu.elitecut.presentation.utils.eliteCutTextFieldColors
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

data class PasswordFieldConfig(
    val label: String,
    val testTag: String,
    val supportingLength: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordFieldSection(
    config: PasswordFieldConfig,
    value: String,
    isVisible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    colors: TextFieldColors
) {
    val visibilityIcon = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
    val visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
    Text(config.label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("• • • • • • • •", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(visibilityIcon, "Toggle password", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        supportingText = { Text("Máximo 30 caracteres (${config.supportingLength}/30)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        visualTransformation = visualTransformation,
        modifier = Modifier.fillMaxWidth().testTag(config.testTag),
        singleLine = true, shape = RoundedCornerShape(12.dp), colors = colors
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
    val textFieldColors = eliteCutTextFieldColors()
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
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
            Text("Nombre Completo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(RegisterUiEvent.OnNombreChange(InputValidation.filterNombreInput(it, 50))) },
                placeholder = { Text("Ej. Juan Pérez", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                supportingText = { Text("Solo letras y espacios (${state.nombre.length}/50)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth().testTag("input_nombre"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("Teléfono", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.telefono,
                onValueChange = { onEvent(RegisterUiEvent.OnTelefonoChange(InputValidation.formatPhoneInput(it))) },
                placeholder = { Text("849-381-6768", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                supportingText = { Text("Formato: XXX-XXX-XXXX", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth().testTag("input_telefono"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(12.dp))
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
            Text("Correo Electrónico", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.correo,
                onValueChange = { onEvent(RegisterUiEvent.OnCorreoChange(InputValidation.limitLength(it, 60))) },
                placeholder = { Text("usuario@gmail.com", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                supportingText = { Text("Debe ser una cuenta de Gmail (${state.correo.length}/60)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().testTag("input_correo"),
                singleLine = true, shape = RoundedCornerShape(12.dp), colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(12.dp))
            PasswordFieldSection(
                config = PasswordFieldConfig(label = "Contraseña", testTag = "input_password", supportingLength = state.password.length),
                value = state.password, isVisible = state.isPasswordVisible,
                onValueChange = { onEvent(RegisterUiEvent.OnPasswordChange(InputValidation.limitLength(it, 30))) },
                onToggleVisibility = { onEvent(RegisterUiEvent.TogglePasswordVisibility) },
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(12.dp))
            PasswordFieldSection(
                config = PasswordFieldConfig(label = "Confirmar", testTag = "input_confirmar", supportingLength = state.confirmarPassword.length),
                value = state.confirmarPassword, isVisible = state.isConfirmarPasswordVisible,
                onValueChange = { onEvent(RegisterUiEvent.OnConfirmarPasswordChange(InputValidation.limitLength(it, 30))) },
                onToggleVisibility = { onEvent(RegisterUiEvent.ToggleConfirmarPasswordVisibility) },
                colors = textFieldColors
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
            FutureDatePickerDialog(
                onDateSelected = { onEvent(RegisterUiEvent.OnFechaIngresoChange(it)) },
                onDismiss = { onEvent(RegisterUiEvent.HideDatePicker) }
            )
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
