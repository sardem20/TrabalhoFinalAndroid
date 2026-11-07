package com.example.arablecoreapi.userinterface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.example.arablecoreapi.data.local.ConsultaEntity
import com.example.arablecoreapi.data.local.Tipo
import com.example.arablecoreapi.viewmodel.MainViewModel

// Fazendeiro: vê as perguntas que fez e as respostas.
// Agrônomo/botânico: vê as perguntas recebidas e responde.
@Composable
fun ConsultasScreen(vm: MainViewModel) {
    val souFazendeiro = vm.usuario?.tipo == Tipo.FAZENDEIRO
    var respondendo by remember { mutableStateOf<ConsultaEntity?>(null) }
    var resposta by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            if (souFazendeiro) "Minhas consultas" else "Consultas recebidas",
            style = MaterialTheme.typography.headlineSmall
        )

        if (vm.consultas.isEmpty()) {
            Text("Nenhuma consulta ainda.", modifier = Modifier.padding(top = 8.dp))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(vm.consultas, key = { it.id }) { c ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                if (souFazendeiro) "Para: ${c.especialistaNome} (${Tipo.nome(c.especialistaTipo)})"
                                else "De: ${c.fazendeiroNome}",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(c.pergunta, modifier = Modifier.padding(top = 4.dp))
                            if (c.resposta != null) {
                                Text(
                                    "Resposta: ${c.resposta}",
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            } else if (souFazendeiro) {
                                Text("Aguardando resposta...", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
                            } else {
                                Button(onClick = { respondendo = c }, modifier = Modifier.padding(top = 8.dp)) {
                                    Text("Responder")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    respondendo?.let { c ->
        AlertDialog(
            onDismissRequest = { respondendo = null; resposta = "" },
            title = { Text("Responder ${c.fazendeiroNome}") },
            text = {
                OutlinedTextField(
                    value = resposta, onValueChange = { resposta = it },
                    label = { Text("Sua resposta") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    enabled = resposta.isNotBlank(),
                    onClick = {
                        vm.responderConsulta(c.id, resposta)
                        respondendo = null
                        resposta = ""
                    }
                ) { Text("Enviar") }
            },
            dismissButton = {
                TextButton(onClick = { respondendo = null; resposta = "" }) { Text("Cancelar") }
            }
        )
    }
}
