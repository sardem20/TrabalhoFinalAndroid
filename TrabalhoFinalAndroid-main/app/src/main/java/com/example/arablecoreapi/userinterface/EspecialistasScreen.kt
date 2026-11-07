package com.example.arablecoreapi.userinterface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arablecoreapi.data.local.Tipo
import com.example.arablecoreapi.data.local.UserEntity
import com.example.arablecoreapi.viewmodel.MainViewModel

// Aba do fazendeiro: lista agrônomos e botânicos. Tocar em um abre a caixa para mandar a pergunta.
@Composable
fun EspecialistasScreen(vm: MainViewModel) {
    var escolhido by remember { mutableStateOf<UserEntity?>(null) }
    var pergunta by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Especialistas", style = MaterialTheme.typography.headlineSmall)

        if (vm.especialistas.isEmpty()) {
            Text("Nenhum agrônomo ou botânico cadastrado ainda.", modifier = Modifier.padding(top = 8.dp))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(vm.especialistas, key = { it.id }) { esp ->
                    Card(onClick = { escolhido = esp }, modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(esp.nome, style = MaterialTheme.typography.titleMedium)
                            Text(Tipo.nome(esp.tipo), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }

    escolhido?.let { esp ->
        AlertDialog(
            onDismissRequest = { escolhido = null; pergunta = "" },
            title = { Text("Consultar ${esp.nome}") },
            text = {
                OutlinedTextField(
                    value = pergunta, onValueChange = { pergunta = it },
                    label = { Text("Sua pergunta") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    enabled = pergunta.isNotBlank(),
                    onClick = {
                        vm.enviarConsulta(esp, pergunta)
                        escolhido = null
                        pergunta = ""
                    }
                ) { Text("Enviar") }
            },
            dismissButton = {
                TextButton(onClick = { escolhido = null; pergunta = "" }) { Text("Cancelar") }
            }
        )
    }
}
