package com.app.ventasxpertsmobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.data.api.ApiClient
import com.app.ventasxpertsmobile.data.api.ApiService
import com.app.ventasxpertsmobile.data.model.Usuario
import com.app.ventasxpertsmobile.ui.theme.VentasXpertsMobileTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.material3.HorizontalDivider


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VentasXpertsMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaUsuarios(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ListaUsuarios(modifier: Modifier = Modifier) {
    val usuarios = remember { mutableStateListOf<Usuario>() }
    val cargando = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        apiService.getUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                cargando.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        usuarios.addAll(it)
                    }
                } else {
                    Log.e("API", "Error código: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                cargando.value = false
                Log.e("API", "Error de red: ${t.message}")
            }
        })
    }

    if (cargando.value) {
        Text("Cargando usuarios...", modifier = modifier.padding(16.dp))
    } else {
        LazyColumn(modifier = modifier.padding(16.dp)) {
            items(usuarios) { usuario ->
                Text("👤 ${usuario.username} - ${usuario.email}", style = MaterialTheme.typography.bodyLarge)
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUsuarios() {
    VentasXpertsMobileTheme {
        ListaUsuarios()
    }
}