package com.albertoserna.rickmorty.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponseDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)

@Serializable
data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationDto,
    val location: LocationDto,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)

@Serializable
data class LocationDto(
    val name: String,
    val url: String
)