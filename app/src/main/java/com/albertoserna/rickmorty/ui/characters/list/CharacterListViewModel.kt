package com.albertoserna.rickmorty.ui.characters.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.usecase.CharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val charactersUseCase: CharactersUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow<CharacterListState>(CharacterListState.Loading)
    val state: StateFlow<CharacterListState> = _state
    
    private var currentPage = 1
    private var allCharacters = listOf<Character>()
    private var speciesFilter: String? = null
    private var statusFilter: String? = null
    
    init {
        loadCharacters()
    }
    
    fun loadCharacters() {
        viewModelScope.launch {
            _state.value = CharacterListState.Loading
            try {
                val characters = charactersUseCase(currentPage)
                allCharacters = characters
                _state.value = CharacterListState.Success(applyFilters(characters))
            } catch (e: Exception) {
                _state.value = CharacterListState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun loadNextPage() {
        currentPage++
        viewModelScope.launch {
            try {
                val newCharacters = charactersUseCase(currentPage)
                allCharacters = allCharacters + newCharacters
                _state.value = CharacterListState.Success(applyFilters(allCharacters))
            } catch (e: Exception) {
                // Mantener la página actual en caso de error
                currentPage--
                _state.value = CharacterListState.Error(e.message ?: "Error loading more characters")
            }
        }
    }
    
    fun setSpeciesFilter(species: String?) {
        speciesFilter = species
        updateFilteredCharacters()
    }
    
    fun setStatusFilter(status: String?) {
        statusFilter = status
        updateFilteredCharacters()
    }
    
    private fun updateFilteredCharacters() {
        if (_state.value is CharacterListState.Success) {
            _state.update { CharacterListState.Success(applyFilters(allCharacters)) }
        }
    }
    
    private fun applyFilters(characters: List<Character>): List<Character> {
        return characters.filter { character ->
            (speciesFilter == null || character.species.equals(speciesFilter, ignoreCase = true)) &&
            (statusFilter == null || character.status.equals(statusFilter, ignoreCase = true))
        }
    }
}

sealed class CharacterListState {
    object Loading : CharacterListState()
    data class Success(val characters: List<Character>) : CharacterListState()
    data class Error(val message: String) : CharacterListState()
}