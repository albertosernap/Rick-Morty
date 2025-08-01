package com.albertoserna.rickmorty.ui.characters.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
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
            getCharactersUseCase(currentPage)
                .catch { e ->
                    _state.value = CharacterListState.Error(e.message ?: "Unknown error")
                }
                .collect { characters ->
                    _state.value = CharacterListState.Success(characters)
                }
        }
    }
    
    fun loadNextPage() {
        currentPage++
        loadCharacters()
    }
}

sealed class CharacterListState {
    object Loading : CharacterListState()
    data class Success(val characters: List<Character>) : CharacterListState()
    data class Error(val message: String) : CharacterListState()
}