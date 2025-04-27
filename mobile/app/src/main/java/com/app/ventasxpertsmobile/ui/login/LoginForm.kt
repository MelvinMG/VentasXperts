package com.app.ventasxpertsmobile.ui.login
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.ui.theme.*

@Composable
fun LoginForm(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(22.dp))
            .background(Blanco1, RoundedCornerShape(22.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario", color = TextoInput, fontFamily = OpenSans) },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = Gris1)
                },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoInput, RoundedCornerShape(12.dp)),
                keyboardOptions = KeyboardOptions.Default,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = FondoInput,
                    unfocusedBorderColor = Gris2,
                    focusedBorderColor = AzulPrincipal,
                ),
                textStyle = AppTypography.bodyLarge.copy(
                    fontFamily = OpenSans,
                    color = Gris1
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = TextoInput, fontFamily = OpenSans) },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = Gris1)
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoInput, RoundedCornerShape(12.dp)),
                keyboardOptions = KeyboardOptions.Default,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = FondoInput,
                    unfocusedBorderColor = Gris2,
                    focusedBorderColor = AzulPrincipal,
                ),
                textStyle = AppTypography.bodyLarge.copy(
                    fontFamily = OpenSans,
                    color = Gris1
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = AzulPrincipal,
                fontSize = 14.sp,
                fontFamily = OpenSans,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { onLoginClick(username, password)
                            onLoginSuccess()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulPrincipal,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Iniciar Sesión", fontFamily = OpenSans, fontWeight = FontWeight.Bold)
            }
        }
    }
}
