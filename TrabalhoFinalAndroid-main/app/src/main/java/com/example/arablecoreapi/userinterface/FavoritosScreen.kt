package com.example.arablecoreapi.userinterface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.arablecoreapi.viewmodel.MainViewModel

// Aba "Favoritos": dados vindos do Room, só do usuário logado.
@Composable
fun FavoritosScreen(vm: MainViewModel, onAbrirDetalhe: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Meus favoritos", style = MaterialTheme.typography.headlineSmall)

        if (vm.favoritos.isEmpty()) {
            Text("Nenhuma espécie salva ainda.", modifier = Modifier.padding(top = 8.dp))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(vm.favoritos, key = { it.especieKey }) { fav ->
                    Card(onClick = { onAbrirDetalhe(fav.especieKey) }, modifier = Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(fav.nome, fontStyle = FontStyle.Italic)
                                Text("Família: ${fav.familia ?: "-"}", style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { vm.removerFavorito(fav.especieKey) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Remover")
                            }
                        }
                    }
                }
            }
        }
    }
}
