package com.albertoserna.rickmorty.data.remote.api

import com.albertoserna.rickmorty.core.util.Either
import com.albertoserna.rickmorty.core.util.ErrorKeys
import com.albertoserna.rickmorty.data.remote.dto.CharacterDto
import com.albertoserna.rickmorty.data.remote.dto.CharacterResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

class CharacterRemoteDataSourceImpl(private val client: HttpClient) : CharacterRemoteDataSource {
    private val baseUrl = "https://rickandmortyapi.com/api"
    
    override suspend fun getCharacters(page: Int): Either<ErrorKeys, CharacterResponseDto> {
        return try {
            val response = client.get("$baseUrl/character/?page=$page")
            val charactersResponse: CharacterResponseDto = response.body()
            Either.Right(charactersResponse)
        } catch (e: ClientRequestException) {
            Either.Left(ErrorKeys.CLIENT_ERROR)
        } catch (e: Exception) {
            Either.Left(ErrorKeys.DATA_ERROR)
        }
    }
    
    override suspend fun getCharacter(id: Int): Either<ErrorKeys, CharacterDto> {
        return try {
            val response = client.get("$baseUrl/character/$id")
            val character: CharacterDto = response.body()
            Either.Right(character)
        } catch (e: ClientRequestException) {
            Either.Left(ErrorKeys.CLIENT_ERROR)
        } catch (e: Exception) {
            Either.Left(ErrorKeys.DATA_ERROR)
        }
    }
}