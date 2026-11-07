package com.example.arablecoreapi.userinterface

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arablecoreapi.data.local.Tipo
import com.example.arablecoreapi.viewmodel.MainViewModel

@Composable
fun PerfilScreen(vm: MainViewModel) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Perfil", style = MaterialTheme.typography.headlineSmall)
        Text(vm.usuario?.nome ?: "", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp))
        Text(vm.usuario?.email ?: "")
        vm.usuario?.let { Text(Tipo.nome(it.tipo), style = MaterialTheme.typography.labelLarge) }
        Button(onClick = { vm.sair() }, modifier = Modifier.padding(top = 24.dp)) { Text("Sair") }
    }
}
