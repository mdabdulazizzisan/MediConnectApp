package com.kolu.mediconnect.presentation.screens.auth.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.kolu.mediconnect.presentation.screens.auth.AuthUiState
import com.kolu.mediconnect.presentation.screens.auth.AuthViewModel
import com.kolu.mediconnect.presentation.components.OutlinedTextFieldPassword
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val userData by authViewModel.userData.collectAsState()
    val password by authViewModel.password.collectAsState()
    val authUiState by authViewModel.authUiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(authUiState) {
        when (authUiState) {
            is AuthUiState.Success -> onRegisterSuccess()
            is AuthUiState.Error -> {
                val message = (authUiState as AuthUiState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                authViewModel.resetState()
            }

            else -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //title
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 50.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        //name field
        OutlinedTextField(
            modifier = Modifier.width(300.dp),
            label = { Text(text = "Name") },
            value = userData.name,
            onValueChange = { authViewModel.setName(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.TextFields,
                    contentDescription = "Name Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        //age field
        OutlinedTextField(
            modifier = Modifier.width(300.dp),
            label = { Text(text = "Age") },
            value = if (userData.age == 0) "" else userData.age.toString(),
            onValueChange = { authViewModel.setAge(if(it.isEmpty()) 0 else it.toInt()) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Age Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        //phone number field
        OutlinedTextField(
            modifier = Modifier.width(300.dp),
            label = { Text(text = "Phone Number") },
            value = userData.phoneNumber,
            onValueChange = { authViewModel.setPhoneNumber(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone Number Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        //emergency contact field
        OutlinedTextField(
            modifier = Modifier.width(300.dp),
            label = { Text(text = "Emergency Contact") },
            value = userData.emergencyContact,
            onValueChange = { authViewModel.setEmergencyContact(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Report,
                    contentDescription = "Emergency Contact Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        //email field
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

        //password field
        OutlinedTextFieldPassword(
            modifier = Modifier.width(300.dp),
            password = password,
            onPasswordChange = { authViewModel.setPassword(it) },
            passwordVisibility = passwordVisibility,
            onVisibilityChange = { passwordVisibility = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (password.length < 6) {
                Toast.makeText(context, "password is too short", Toast.LENGTH_SHORT)
                    .show()
            } else {
                keyboardController?.hide()
                authViewModel.register()
            }
        }) {
            Text(text = "Register")
        }
    }
}