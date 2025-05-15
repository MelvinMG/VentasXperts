package com.app.ventasxpertsmobile.ui.login


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.data.api.LoginRequest
import com.app.ventasxpertsmobile.data.api.TokenResponse
import com.app.ventasxpertsmobile.data.api.UsuarioResponse
import com.app.ventasxpertsmobile.data.auth.TokenManager
import com.app.ventasxpertsmobile.data.auth.UserSessionManager
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import com.app.ventasxpertsmobile.ui.navigation.NavigationItem
import com.app.ventasxpertsmobile.ui.theme.AppTypography
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import com.app.ventasxpertsmobile.ui.theme.Blanco1
import com.app.ventasxpertsmobile.ui.theme.FondoInput
import com.app.ventasxpertsmobile.ui.theme.Gris1
import com.app.ventasxpertsmobile.ui.theme.Gris2
import com.app.ventasxpertsmobile.ui.theme.OpenSans
import com.app.ventasxpertsmobile.ui.theme.TextoInput
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun LoginForm(onLoginSuccess: (String) -> Unit) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

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
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = Gris1) },
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
                textStyle = AppTypography.bodyLarge.copy(fontFamily = OpenSans, color = Gris1)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = TextoInput, fontFamily = OpenSans) },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Gris1) },
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
                textStyle = AppTypography.bodyLarge.copy(fontFamily = OpenSans, color = Gris1)
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

            if (loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        loading = true
                        val loginRequest = LoginRequest(username, password)
                        RetrofitClient.api.login(loginRequest).enqueue(object : Callback<TokenResponse> {
                            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                                if (response.isSuccessful) {
                                    val tokens = response.body()
                                    val accessToken = tokens?.access

                                    if (accessToken != null) {
                                        TokenManager.saveAccessToken(context, accessToken)

                                        RetrofitClient.api.getCurrentUser().enqueue(object : Callback<UsuarioResponse> {
                                            override fun onResponse(call: Call<UsuarioResponse>, response: Response<UsuarioResponse>) {
                                                loading = false
                                                if (response.isSuccessful) {
                                                    val user = response.body()
                                                    if (user != null) {
                                                        UserSessionManager.saveRoles(context, user.roles.toSet())
                                                        val fullName = "${user.nombre} ${user.apPaterno}"
                                                        UserSessionManager.saveUserFullName(context, fullName)

                                                        val route = when {
                                                            "Administrador" in user.roles -> NavigationItem.Usuarios.route
                                                            "Gerente" in user.roles -> NavigationItem.Bitacora.route
                                                            "Cajero" in user.roles -> NavigationItem.Catalogo.route
                                                            else -> NavigationItem.Catalogo.route
                                                        }

                                                        onLoginSuccess(route) // envía la ruta hacia MainActivity
                                                    } else {
                                                        Toast.makeText(context, "Error al obtener usuario", Toast.LENGTH_SHORT).show()
                                                    }
                                                } else {
                                                    Toast.makeText(context, "Error al obtener usuario", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                                                loading = false
                                                Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        })

                                    } else {
                                        loading = false
                                        Toast.makeText(context, "Error: Token inválido", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    loading = false
                                    Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                                loading = false
                                Toast.makeText(context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrincipal,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Iniciar Sesión", fontFamily = OpenSans, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
            }
        }
    }
}
