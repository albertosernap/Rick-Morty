package com.albertoserna.rickmorty.data.repository

import com.albertoserna.rickmorty.data.remote.api.RickMortyApi
import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.model.Location
import com.albertoserna.rickmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CharacterRepositoryImpl(
    private val api: RickMortyApi
) : CharacterRepository {
    
    override suspend fun getCharacters(page: Int): Flow<List<Character>> = flow {
        val response = api.getCharacters(page)
        val characters = response.results.map { dto ->
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
        emit(characters)
    }
    
    override suspend fun getCharacter(id: Int): Flow<Character> = flow {
        val dto = api.getCharacter(id)
        val character = Character(
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
        emit(character)
    }
}