package com.albertoserna.rickmorty.ui.characters.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel = koinViewModel(),
    onCharacterClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        when (val currentState = state) {
            is CharacterListState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is CharacterListState.Success -> {
                LazyColumn {
                    items(currentState.characters) { character ->
                        CharacterItem(
                            character = character,
                            onCharacterClick = { onCharacterClick(character.id) }
                        )
                    }
                    item {
                        Button(
                            onClick = { viewModel.loadNextPage() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Load More")
                        }
                    }
                }
            }
            is CharacterListState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = currentState.message)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadCharacters() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: com.albertoserna.rickmorty.domain.model.Character,
    onCharacterClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onCharacterClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${character.species} - ${character.status}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}