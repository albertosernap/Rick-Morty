package com.albertoserna.rickmorty.data.repository

import com.albertoserna.rickmorty.core.util.Either
import com.albertoserna.rickmorty.data.remote.api.CharacterRemoteDataSource
import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.model.Location
import com.albertoserna.rickmorty.domain.repository.CharacterRepository

class CharacterRepositoryImpl(
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {
    
    override suspend fun getCharacters(page: Int): List<Character> {
        val response = remoteDataSource.getCharacters(page)
        return when (response) {
            is Either.Right -> response.value.results.map { dto ->
                Character(
                    id = dto.id,
                    name = dto.name,
                    status = dto.status,
                    species = dto.species,
                    type = dto.type,
                    gender = dto.gender,
                    origin = Location(dto.origin.name, dto.origin.url),
                    location = Location(dto.location.name, dto.location.url),
                    image = dto.image,
                    episodes = dto.episode
                )
            }
            is Either.Left -> emptyList()
        }
    }
    
    override suspend fun getCharacter(id: Int): Character? {
        val response = remoteDataSource.getCharacter(id)
        return when (response) {
            is Either.Right -> Character(
                id = response.value.id,
                name = response.value.name,
                status = response.value.status,
                species = response.value.species,
                type = response.value.type,
                gender = response.value.gender,
                origin = Location(response.value.origin.name, response.value.origin.url),
                location = Location(response.value.location.name, response.value.location.url),
                image = response.value.image,
                episodes = response.value.episode
            )
            is Either.Left -> null
        }
    }
}