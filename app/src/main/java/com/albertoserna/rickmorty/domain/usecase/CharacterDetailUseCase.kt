package com.albertoserna.rickmorty.domain.usecase

import com.albertoserna.rickmorty.domain.model.Character

interface CharacterDetailUseCase {
    suspend operator fun invoke(id: Int): Character?
}