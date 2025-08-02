package com.albertoserna.rickmorty.data.repository

import com.albertoserna.rickmorty.core.util.Either
import com.albertoserna.rickmorty.core.util.ErrorKeys
import com.albertoserna.rickmorty.data.remote.api.CharacterRemoteDataSource
import com.albertoserna.rickmorty.data.remote.dto.CharacterDto
import com.albertoserna.rickmorty.data.remote.dto.CharacterResponseDto
import com.albertoserna.rickmorty.data.remote.dto.InfoDto
import com.albertoserna.rickmorty.data.remote.dto.LocationDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CharacterRepositoryImplTest {

    @Mock
    private lateinit var mockRemoteDataSource: CharacterRemoteDataSource

    private lateinit var characterRepository: CharacterRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        characterRepository = CharacterRepositoryImpl(mockRemoteDataSource)
    }

    @Test
    fun `getCharacters returns list of characters when remote call is successful`() = runBlocking {
        // Given
        val characterDto = CharacterDto(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = LocationDto("Earth", "https://rickandmortyapi.com/api/location/1"),
            location = LocationDto("Earth", "https://rickandmortyapi.com/api/location/20"),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episode = listOf("https://rickandmortyapi.com/api/episode/1"),
            url = "https://rickandmortyapi.com/api/character/1",
            created = "2017-11-04T18:48:46.250Z"
        )
        
        val responseDto = CharacterResponseDto(
            info = InfoDto(
                count = 826,
                pages = 42,
                next = "https://rickandmortyapi.com/api/character/?page=2",
                prev = null
            ),
            results = listOf(characterDto)
        )
        
        `when`(mockRemoteDataSource.getCharacters(1)).thenReturn(Either.Right(responseDto))

        // When
        val result = characterRepository.getCharacters(1)

        // Then
        assertEquals(1, result.size)
        assertEquals("Rick Sanchez", result[0].name)
        assertEquals("Alive", result[0].status)
        assertEquals("Human", result[0].species)
        assertEquals("Male", result[0].gender)
        assertEquals("Earth", result[0].origin.name)
        assertEquals("Earth", result[0].location.name)
        assertEquals(1, result[0].episodes.size)
    }

    @Test
    fun `getCharacters returns empty list when remote call fails`() = runBlocking {
        // Given
        `when`(mockRemoteDataSource.getCharacters(1)).thenReturn(Either.Left(ErrorKeys.NETWORK_ERROR))

        // When
        val result = characterRepository.getCharacters(1)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getCharacter returns character when remote call is successful`() = runBlocking {
        // Given
        val characterDto = CharacterDto(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = LocationDto("Earth", "https://rickandmortyapi.com/api/location/1"),
            location = LocationDto("Earth", "https://rickandmortyapi.com/api/location/20"),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episode = listOf("https://rickandmortyapi.com/api/episode/1"),
            url = "https://rickandmortyapi.com/api/character/1",
            created = "2017-11-04T18:48:46.250Z"
        )
        
        `when`(mockRemoteDataSource.getCharacter(1)).thenReturn(Either.Right(characterDto))

        // When
        val result = characterRepository.getCharacter(1)

        // Then
        assertEquals(1, result?.id)
        assertEquals("Rick Sanchez", result?.name)
        assertEquals("Alive", result?.status)
        assertEquals("Human", result?.species)
        assertEquals("Male", result?.gender)
        assertEquals("Earth", result?.origin?.name)
        assertEquals("Earth", result?.location?.name)
        assertEquals(1, result?.episodes?.size)
    }

    @Test
    fun `getCharacter returns null when remote call fails`() = runBlocking {
        // Given
        `when`(mockRemoteDataSource.getCharacter(999)).thenReturn(Either.Left(ErrorKeys.CLIENT_ERROR))

        // When
        val result = characterRepository.getCharacter(999)

        // Then
        assertNull(result)
    }
}