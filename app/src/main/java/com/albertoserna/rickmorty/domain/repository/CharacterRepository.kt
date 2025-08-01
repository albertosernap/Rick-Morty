package com.albertoserna.rickmorty.domain.repository

import com.albertoserna.rickmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(page: Int): Flow<List<Character>>
    suspend fun getCharacter(id: Int): Flow<Character>
}