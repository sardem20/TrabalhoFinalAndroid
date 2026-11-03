package com.example.arablecoreapi.userinterface

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.arablecoreapi.viewmodel.MainViewModel

// Recebe a "key" da espécie pela rota e busca os dados dela na API.
@Composable
fun DetalheScreen(vm: MainViewModel, key: Int, onVoltar: () -> Unit) {
    LaunchedEffect(key) { vm.carregarDetalhe(key) }

    val especie = vm.especie
    val favorita = vm.favoritos.any { it.especieKey == key }

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
            }
            else -> CircularProgressIndicator()
        }
    }
}
