package com.example.arablecoreapi.userinterface

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.arablecoreapi.data.local.Tipo
import com.example.arablecoreapi.data.local.UserEntity
import com.example.arablecoreapi.viewmodel.MainViewModel

// Recebe a "key" da espécie pela rota e busca os dados dela na API.
@Composable
fun DetalheScreen(vm: MainViewModel, key: Int, onVoltar: () -> Unit) {
    LaunchedEffect(key) { vm.carregarDetalhe(key) }

    val especie = vm.especie
    val favorita = vm.favoritos.any { it.especieKey == key }

    // Estados da caixa "Consultar sobre esta espécie".
    var consultando by remember { mutableStateOf(false) }
    var escolhido by remember { mutableStateOf<UserEntity?>(null) }
    var pergunta by remember { mutableStateOf("") }
    var enviado by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            IconButton(onClick = onVoltar) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
            if (especie != null && especie.key == key) {
                IconButton(onClick = { vm.alternarFavorito(especie) }) {
                    Icon(
                        if (favorita) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favoritar"
                    )
                }
            }
        }

        when {
            vm.erroDetalhe != null -> {
                Text(vm.erroDetalhe!!, color = MaterialTheme.colorScheme.error)
                Button(onClick = { vm.carregarDetalhe(key) }) { Text("Tentar novamente") }
            }
            especie != null && especie.key == key -> {
                Text(especie.nome, style = MaterialTheme.typography.headlineSmall, fontStyle = FontStyle.Italic)
                Text("Reino: ${especie.kingdom ?: "-"}", modifier = Modifier.padding(top = 12.dp))
                Text("Filo: ${especie.phylum ?: "-"}")
                Text("Classe: ${especie.classe ?: "-"}")
                Text("Ordem: ${especie.order ?: "-"}")
                Text("Família: ${especie.family ?: "-"}")
                Text("Gênero: ${especie.genus ?: "-"}")
                Text("Fonte: GBIF", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 16.dp))

                // Só o fazendeiro consulta especialistas.
                if (vm.usuario?.tipo == Tipo.FAZENDEIRO) {
                    Button(onClick = { consultando = true }, modifier = Modifier.padding(top = 16.dp)) {
                        Text("Consultar sobre esta espécie")
                    }
                    if (enviado) {
                        Text("Consulta enviada! Veja a resposta na aba Consultas.", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            else -> CircularProgressIndicator()
        }
    }

    if (consultando && especie != null) {
        AlertDialog(
            onDismissRequest = { consultando = false },
            title = { Text("Consultar sobre ${especie.nome}") },
            text = {
                Column {
                    if (vm.especialistas.isEmpty()) {
                        Text("Nenhum agrônomo ou botânico cadastrado ainda.")
                    }
                    vm.especialistas.forEach { esp ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = escolhido?.id == esp.id, onClick = { escolhido = esp })
                            Text("${esp.nome} (${Tipo.nome(esp.tipo)})")
                        }
                    }
                    OutlinedTextField(
                        value = pergunta, onValueChange = { pergunta = it },
                        label = { Text("Sua pergunta") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = escolhido != null && pergunta.isNotBlank(),
                    onClick = {
                        // A pergunta já vai com o nome da espécie na frente.
                        vm.enviarConsulta(escolhido!!, "Sobre ${especie.nome}: $pergunta")
                        consultando = false
                        pergunta = ""
                        enviado = true
                    }
                ) { Text("Enviar") }
            },
            dismissButton = { TextButton(onClick = { consultando = false }) { Text("Cancelar") } }
        )
    }
}
