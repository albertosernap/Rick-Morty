package com.albertoserna.rickmorty.domain.usecase

import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.repository.CharacterRepository

class GetCharacterDetailUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(id: Int): Character {
        return repository.getCharacter(id)
    }
}