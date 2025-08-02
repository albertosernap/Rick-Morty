package com.albertoserna.rickmorty.domain.usecase

import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.model.Location
import com.albertoserna.rickmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CharactersUseCaseTest {

    @Mock
    private lateinit var mockRepository: CharacterRepository

    private lateinit var charactersUseCase: CharactersUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        charactersUseCase = CharactersUseCaseImpl(mockRepository)
    }

    @Test
    fun `invoke returns list of characters from repository`() = runBlocking {
        // Given
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = Location("Earth", "https://rickandmortyapi.com/api/location/1"),
                location = Location("Earth", "https://rickandmortyapi.com/api/location/20"),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episodes = listOf("https://rickandmortyapi.com/api/episode/1")
            )
        )
        
        `when`(mockRepository.getCharacters(1)).thenReturn(characters)

        // When
        val result = charactersUseCase(1)

        // Then
        assertEquals(characters, result)
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runBlocking {
        // Given
        val emptyList = emptyList<Character>()
        `when`(mockRepository.getCharacters(1)).thenReturn(emptyList)

        // When
        val result = charactersUseCase(1)

        // Then
        assertEquals(emptyList, result)
    }
}