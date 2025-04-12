package com.kolu.mediconnect.presentation.screens.auth.login

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kolu.mediconnect.domain.model.UserData
import com.kolu.mediconnect.presentation.components.OutlinedTextFieldPassword
import com.kolu.mediconnect.presentation.screens.auth.AuthUiState
import com.kolu.mediconnect.presentation.screens.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onNavigateToRegisterScreen: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {

    val context = LocalContext.current
    val userData: UserData by authViewModel.userData.collectAsState()
    val password: String by authViewModel.password.collectAsState()
    val authUiState by authViewModel.authUiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(authUiState) {
        when (authUiState) {
            is AuthUiState.Success -> onLoginSuccess()
            is AuthUiState.Error -> {
                val message = (authUiState as AuthUiState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                authViewModel.resetState()
            }

            else -> {}
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //title
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 50.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            modifier = Modifier.width(300.dp),
            label = { Text(text = "Email") },
            value = userData.email,
            onValueChange = { authViewModel.setEmail(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldPassword(
            modifier = Modifier.width(300.dp),
            password = password,
            onPasswordChange = { authViewModel.setPassword(it) },
            passwordVisibility = passwordVisibility,
            onVisibilityChange = { passwordVisibility = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            keyboardController?.hide()

            authViewModel.login()
        }) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row {
            Text(
                text = "Don't have account? ",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier
                    .clickable {
                        onNavigateToRegisterScreen()
                    },
                text = "Register",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    BackHandler {
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}



