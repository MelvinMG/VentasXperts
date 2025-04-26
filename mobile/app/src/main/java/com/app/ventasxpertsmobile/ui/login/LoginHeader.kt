package com.app.ventasxpertsmobile.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.R
import com.app.ventasxpertsmobile.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
@Composable
fun LoginHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Fondo azul con esquinas inferiores redondeadas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.40f) // se ajusta a  un 40% de la pantalla
                .background(
                    color = AzulPrincipal,
                    shape = RoundedCornerShape(
                        bottomStart = 30.dp,
                        bottomEnd = 30.dp
                    )
                )
                .align(Alignment.TopCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenido a VentasXperts",
                    color = Color.White,
                    style = AppTypography.headlineLarge
                )
                Spacer(modifier = Modifier.height(22.dp))
                Surface(
                    shape = RoundedCornerShape(100),
                    color = Blanco1,
                    shadowElevation = 6.dp,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_1),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewLoginHeader() {
    LoginHeader()
}
