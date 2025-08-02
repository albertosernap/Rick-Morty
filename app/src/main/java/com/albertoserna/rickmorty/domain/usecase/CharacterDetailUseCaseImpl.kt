package com.albertoserna.rickmorty.domain.usecase

import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.repository.CharacterRepository

class CharacterDetailUseCaseImpl(private val repository: CharacterRepository) : CharacterDetailUseCase {
    override suspend operator fun invoke(id: Int): Character? {
        return repository.getCharacter(id)
    }
}