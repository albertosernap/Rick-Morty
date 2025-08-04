package com.albertoserna.rickmorty.ui.characters.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.usecase.CharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val charactersUseCase: CharactersUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow<CharacterListState>(CharacterListState.Loading)
    val state: StateFlow<CharacterListState> = _state
    
    private var currentPage = 1
    
    init {
        loadCharacters()
    }
    
    fun loadCharacters() {
        viewModelScope.launch {
            _state.value = CharacterListState.Loading
            try {
                val characters = charactersUseCase(currentPage)
                _state.value = CharacterListState.Success(characters)
            } catch (e: Exception) {
                _state.value = CharacterListState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun loadNextPage() {
        currentPage++
        viewModelScope.launch {
            try {
                val currentCharacters = when (val currentState = _state.value) {
                    is CharacterListState.Success -> currentState.characters
                    else -> emptyList()
                }
                
                val newCharacters = charactersUseCase(currentPage)
                _state.value = CharacterListState.Success(currentCharacters + newCharacters)
            } catch (e: Exception) {
                // Mantener la página actual en caso de error
                currentPage--
                _state.value = CharacterListState.Error(e.message ?: "Error loading more characters")
            }
        }
    }
}

sealed class CharacterListState {
    object Loading : CharacterListState()
    data class Success(val characters: List<Character>) : CharacterListState()
    data class Error(val message: String) : CharacterListState()
}