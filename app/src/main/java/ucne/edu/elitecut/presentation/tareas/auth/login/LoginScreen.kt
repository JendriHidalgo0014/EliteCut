package ucne.edu.elitecut.presentation.tareas.auth.login

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.elitecut.R
import ucne.edu.elitecut.presentation.utils.InputValidation
import ucne.edu.elitecut.ui.theme.MaterialTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn && state.userRole != null) {
            onLoginSuccess(state.userRole!!)
        }
    }

    LoginBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
fun LoginBody(
    state: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(LoginUiEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading && state.correo.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("loading"),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo_elite_cut),
                        contentDescription = "Elite Cut Logo",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Título "INICIAR SESIÓN"
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 28.sp
                                )
                            ) {
                                append("INICIAR ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 28.sp
                                )
                            ) {
                                append("SESIÓN")
                            }
                        },
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "BIENVENIDO DE VUELTA A ELITE CUT",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.5.sp
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Label EMAIL
                    Text(
                        text = "EMAIL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo Email - máx 60 caracteres
                    OutlinedTextField(
                        value = state.correo,
                        onValueChange = {
                            val limited = InputValidation.limitLength(it, 60)
                            onEvent(LoginUiEvent.OnCorreoChange(limited))
                        },
                        placeholder = {
                            Text(
                                text = "ejemplo@gmail.com",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_correo"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Label CONTRASEÑA + ¿Olvidaste?
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CONTRASEÑA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "¿OLVIDASTE TU CONTRASEÑA?",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo Contraseña - máx 30 caracteres
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = {
                            val limited = InputValidation.limitLength(it, 30)
                            onEvent(LoginUiEvent.OnPasswordChange(limited))
                        },
                        placeholder = {
                            Text(
                                text = "• • • • • • • •",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { onEvent(LoginUiEvent.TogglePasswordVisibility) }) {
                                Icon(
                                    imageVector = if (state.isPasswordVisible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        visualTransformation = if (state.isPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_password"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón Iniciar Sesión
                    Button(
                        onClick = { onEvent(LoginUiEvent.Login) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_login"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "INICIAR SESIÓN",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Link a registro
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "¿No tienes cuenta?  ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "REGÍSTRATE",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToRegister() }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Footer
                    Text(
                        text = "© 2026 ELITE CUT BARBERSHOP. ALL RIGHTS RESERVED.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginBodyPreview() {
    MaterialTheme(darkTheme = true, dynamicColor = false) {
        LoginBody(
            state = LoginUiState(
                isLoading = false,
                correo = "",
                password = ""
            ),
            onEvent = {},
            onNavigateToRegister = {}
        )
    }
}
