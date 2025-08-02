package com.albertoserna.rickmorty.domain.usecase

import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.model.Location
import com.albertoserna.rickmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CharacterDetailUseCaseTest {

    @Mock
    private lateinit var mockRepository: CharacterRepository

    private lateinit var characterDetailUseCase: CharacterDetailUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        characterDetailUseCase = CharacterDetailUseCaseImpl(mockRepository)
    }

    @Test
    fun `invoke returns character from repository when found`() = runBlocking {
        // Given
        val character = Character(
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
        
        `when`(mockRepository.getCharacter(1)).thenReturn(character)

        // When
        val result = characterDetailUseCase(1)

        // Then
        assertEquals(character, result)
    }

    @Test
    fun `invoke returns null when character not found in repository`() = runBlocking {
        // Given
        `when`(mockRepository.getCharacter(999)).thenReturn(null)

        // When
        val result = characterDetailUseCase(999)

        // Then
        assertNull(result)
    }
}