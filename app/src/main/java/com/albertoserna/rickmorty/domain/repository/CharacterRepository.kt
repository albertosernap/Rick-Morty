package com.albertoserna.rickmorty.domain.repository

import com.albertoserna.rickmorty.domain.model.Character

interface CharacterRepository {
    suspend fun getCharacters(page: Int): List<Character>
    suspend fun getCharacter(id: Int): Character
}