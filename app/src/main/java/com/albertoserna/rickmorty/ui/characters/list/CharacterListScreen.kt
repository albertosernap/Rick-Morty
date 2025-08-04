package com.albertoserna.rickmorty.ui.characters.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.ui.components.CachedImage
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.draw.clip
import com.albertoserna.rickmorty.ui.components.AppScaffold

@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel = koinViewModel(),
    onCharacterClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    AppScaffold(
        title = "Characters"
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state) {
                is CharacterListState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CharacterListState.Success -> {
                    CharacterPager(
                        characters = currentState.characters,
                        onCharacterClick = onCharacterClick,
                        onLoadMore = { viewModel.loadNextPage() }
                    )
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
}

@Composable
fun CharacterPager(
    characters: List<Character>,
    onCharacterClick: (Int) -> Unit,
    onLoadMore: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { characters.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            val character = characters[page]
            CharacterCard(
                character = character,
                onSeeMoreClick = { onCharacterClick(character.id) }
            )
        }

        // Progreso tipo barra horizontal moderna
        LinearProgressIndicator(
            progress = (pagerState.currentPage + 1f) / characters.size,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .padding(horizontal = 32.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // Indicador de página (número actual)
        Text(
            text = "${pagerState.currentPage + 1} / ${characters.size}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Cargar más si estamos al final
        if (pagerState.currentPage >= characters.size - 3) {
            OutlinedButton(
                onClick = onLoadMore,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Cargar más personajes")
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: Character,
    onSeeMoreClick: () -> Unit
) {
    val cardColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .height(420.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // 👈 Quita la sombra
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.extraLarge) // 👈 Recorte uniforme
        ) {
            // Imagen del personaje
            CachedImage(
                imageUrl = character.image,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.extraLarge) // 👈 Asegura esquinas redondeadas
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${character.species} - ${character.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onSeeMoreClick,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(44.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Ver más")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
