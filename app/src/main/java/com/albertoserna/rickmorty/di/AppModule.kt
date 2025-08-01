package com.albertoserna.rickmorty.di

import com.albertoserna.rickmorty.data.remote.api.KtorClient
import com.albertoserna.rickmorty.data.remote.api.RickMortyApi
import com.albertoserna.rickmorty.data.repository.CharacterRepositoryImpl
import com.albertoserna.rickmorty.domain.repository.CharacterRepository
import com.albertoserna.rickmorty.domain.usecase.GetCharacterDetailUseCase
import com.albertoserna.rickmorty.domain.usecase.GetCharactersUseCase
import com.albertoserna.rickmorty.ui.characters.detail.CharacterDetailViewModel
import com.albertoserna.rickmorty.ui.characters.list.CharacterListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // API
    single { KtorClient.client }
    single { RickMortyApi(get()) }
    
    // Repository
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
    
    // Use Cases
    single { GetCharactersUseCase(get()) }
    single { GetCharacterDetailUseCase(get()) }
    
    // ViewModels
    viewModel { CharacterListViewModel(get()) }
    viewModel { CharacterDetailViewModel(get()) }
}