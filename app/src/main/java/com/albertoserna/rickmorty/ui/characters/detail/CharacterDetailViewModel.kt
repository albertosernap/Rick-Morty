package com.albertoserna.rickmorty.ui.characters.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertoserna.rickmorty.domain.model.Character
import com.albertoserna.rickmorty.domain.usecase.CharacterDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val characterDetailUseCase: CharacterDetailUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow<CharacterDetailState>(CharacterDetailState.Loading)
    val state: StateFlow<CharacterDetailState> = _state
    
    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            _state.value = CharacterDetailState.Loading
            try {
                val character = characterDetailUseCase(id)
                if (character != null) {
                    _state.value = CharacterDetailState.Success(character)
                } else {
                    _state.value = CharacterDetailState.Error("Character not found")
                }
            } catch (e: Exception) {
                _state.value = CharacterDetailState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class CharacterDetailState {
    object Loading : CharacterDetailState()
    data class Success(val character: Character) : CharacterDetailState()
    data class Error(val message: String) : CharacterDetailState()
}