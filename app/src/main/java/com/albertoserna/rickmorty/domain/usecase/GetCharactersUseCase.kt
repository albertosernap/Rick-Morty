package com.albertoserna.rickmorty.domain.usecase

import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(page: Int): Flow<List<Character>> {
        return repository.getCharacters(page)
    }
}