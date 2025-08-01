package com.albertoserna.rickmorty.data.remote.api

import com.albertoserna.rickmorty.data.remote.dto.CharacterDto
import com.albertoserna.rickmorty.data.remote.dto.CharacterResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class RickMortyApi(private val client: HttpClient) {
    private val baseUrl = "https://rickandmortyapi.com/api"
    
    suspend fun getCharacters(page: Int): CharacterResponseDto {
        return client.get("$baseUrl/character/?page=$page").body()
    }
    
    suspend fun getCharacter(id: Int): CharacterDto {
        return client.get("$baseUrl/character/$id").body()
    }
}