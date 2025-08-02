package com.albertoserna.rickmorty.data.remote.api

import com.albertoserna.rickmorty.core.util.Either
import com.albertoserna.rickmorty.core.util.ErrorKeys
import com.albertoserna.rickmorty.data.remote.dto.CharacterDto
import com.albertoserna.rickmorty.data.remote.dto.CharacterResponseDto

interface CharacterRemoteDataSource {
    suspend fun getCharacters(page: Int): Either<ErrorKeys, CharacterResponseDto>
    suspend fun getCharacter(id: Int): Either<ErrorKeys, CharacterDto>
}