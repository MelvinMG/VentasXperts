package com.app.ventasxpertsmobile.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.ui.theme.Blanco1

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val offsetValue = screenHeight * 0.25f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco1)
    ) {
        LoginHeader()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = offsetValue)
                .align(Alignment.TopCenter)
        ) {
            LoginForm(onLoginSuccess = onLoginSuccess)
        }
    }
}

