package com.albertoserna.rickmorty.domain.usecase

import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.repository.CharacterRepository

class CharactersUseCaseImpl(private val repository: CharacterRepository) : CharactersUseCase {
    override suspend operator fun invoke(page: Int): List<Character> {
        return repository.getCharacters(page)
    }
}