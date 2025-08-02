package com.albertoserna.rickmorty.data.remote.api

import com.albertoserna.rickmorty.core.util.Either
import com.albertoserna.rickmorty.core.util.ErrorKeys
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CharacterRemoteDataSourceImplTest {

    private lateinit var mockEngine: MockEngine
    private lateinit var httpClient: HttpClient
    private lateinit var characterRemoteDataSource: CharacterRemoteDataSource

    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            when {
                request.url.toString().contains("/character/?page=1") -> {
                    respond(
                        content = """
                            {
                                "info": {
                                    "count": 826,
                                    "pages": 42,
                                    "next": "https://rickandmortyapi.com/api/character/?page=2",
                                    "prev": null
                                },
                                "results": [
                                    {
                                        "id": 1,
                                        "name": "Rick Sanchez",
                                        "status": "Alive",
                                        "species": "Human",
                                        "type": "",
                                        "gender": "Male",
                                        "origin": {
                                            "name": "Earth",
                                            "url": "https://rickandmortyapi.com/api/location/1"
                                        },
                                        "location": {
                                            "name": "Earth",
                                            "url": "https://rickandmortyapi.com/api/location/20"
                                        },
                                        "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                                        "episode": [
                                            "https://rickandmortyapi.com/api/episode/1"
                                        ],
                                        "url": "https://rickandmortyapi.com/api/character/1",
                                        "created": "2017-11-04T18:48:46.250Z"
                                    }
                                ]
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                request.url.toString().contains("/character/1") -> {
                    respond(
                        content = """
                            {
                                "id": 1,
                                "name": "Rick Sanchez",
                                "status": "Alive",
                                "species": "Human",
                                "type": "",
                                "gender": "Male",
                                "origin": {
                                    "name": "Earth",
                                    "url": "https://rickandmortyapi.com/api/location/1"
                                },
                                "location": {
                                    "name": "Earth",
                                    "url": "https://rickandmortyapi.com/api/location/20"
                                },
                                "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                                "episode": [
                                    "https://rickandmortyapi.com/api/episode/1"
                                ],
                                "url": "https://rickandmortyapi.com/api/character/1",
                                "created": "2017-11-04T18:48:46.250Z"
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                request.url.toString().contains("/character/999") -> {
                    respond(
                        content = "{\"error\":\"Character not found\"}",
                        status = HttpStatusCode.NotFound,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> {
                    respond(
                        content = "Error",
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }

        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            expectSuccess = true // Esto hará que los códigos de error HTTP generen excepciones
        }

        characterRemoteDataSource = CharacterRemoteDataSourceImpl(httpClient)
    }

    @Test
    fun `getCharacters returns success with characters when API call is successful`() = runBlocking {
        // When
        val result = characterRemoteDataSource.getCharacters(1)

        // Then
        assertTrue(result is Either.Right)
        val charactersResponse = (result as Either.Right).value
        assertEquals(826, charactersResponse.info.count)
        assertEquals(1, charactersResponse.results.size)
        assertEquals("Rick Sanchez", charactersResponse.results[0].name)
    }

    @Test
    fun `getCharacter returns success with character when API call is successful`() = runBlocking {
        // When
        val result = characterRemoteDataSource.getCharacter(1)

        // Then
        assertTrue(result is Either.Right)
        val character = (result as Either.Right).value
        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)
        assertEquals("Alive", character.status)
    }

    @Test
    fun `getCharacter returns CLIENT_ERROR when character not found`() = runBlocking {
        // When
        val result = characterRemoteDataSource.getCharacter(999)

        // Then
        assertTrue(result is Either.Left)
        assertEquals(ErrorKeys.CLIENT_ERROR, (result as Either.Left).value)
    }

    @Test
    fun `getCharacters returns DATA_ERROR when exception occurs`() = runBlocking {
        // Given
        val brokenClient = HttpClient(MockEngine { 
            throw Exception("Network error")
        })
        val brokenDataSource = CharacterRemoteDataSourceImpl(brokenClient)

        // When
        val result = brokenDataSource.getCharacters(1)

        // Then
        assertTrue(result is Either.Left)
        assertEquals(ErrorKeys.DATA_ERROR, (result as Either.Left).value)
    }
}