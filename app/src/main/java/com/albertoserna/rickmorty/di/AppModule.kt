package com.albertoserna.rickmorty.di

import com.albertoserna.rickmorty.data.remote.api.CharacterRemoteDataSource
import com.albertoserna.rickmorty.data.remote.api.CharacterRemoteDataSourceImpl
import com.albertoserna.rickmorty.data.remote.api.KtorClient
import com.albertoserna.rickmorty.data.repository.CharacterRepositoryImpl
import com.albertoserna.rickmorty.domain.repository.CharacterRepository
import com.albertoserna.rickmorty.domain.usecase.CharacterDetailUseCase
import com.albertoserna.rickmorty.domain.usecase.CharacterDetailUseCaseImpl
import com.albertoserna.rickmorty.domain.usecase.CharactersUseCase
import com.albertoserna.rickmorty.domain.usecase.CharactersUseCaseImpl
import com.albertoserna.rickmorty.ui.characters.detail.CharacterDetailViewModel
import com.albertoserna.rickmorty.ui.characters.list.CharacterListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // API
    single { KtorClient.client }
    single<CharacterRemoteDataSource> { CharacterRemoteDataSourceImpl(get()) }
    
    // Repository
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
    
    // Use Cases
    single<CharactersUseCase> { CharactersUseCaseImpl(get()) }
    single<CharacterDetailUseCase> { CharacterDetailUseCaseImpl(get()) }
    
    // ViewModels
    viewModel { CharacterListViewModel(get()) }
    viewModel { CharacterDetailViewModel(get()) }
}