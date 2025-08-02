package com.albertoserna.rickmorty.domain.usecase

import com.albertoserna.rickmorty.domain.model.Character

interface CharactersUseCase {
    suspend operator fun invoke(page: Int): List<Character>
}