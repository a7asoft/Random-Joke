package com.awto.randomjoke.presentation.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awto.randomjoke.data.remote.model.ErrorModel
import com.awto.randomjoke.data.remote.model.JokeResponseModel
import com.awto.randomjoke.domain.joke.GetRandomJokeUseCase
import com.awto.randomjoke.util.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomJokeUseCase: GetRandomJokeUseCase
) : ViewModel() {

    //state get random joke
    private val stateJoke = MutableStateFlow<JokesState>(
        JokesState.Init
    )
    val mStateJoke: StateFlow<JokesState> get() = stateJoke

    //observable random joke
    private val joke = MutableSharedFlow<JokeResponseModel>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val mJoke: Flow<JokeResponseModel> get() = joke.distinctUntilChanged()

    init {
        getRandomJoke()
    }

    //observable methods
    private fun setLoading() {
        stateJoke.value = JokesState.IsLoading(true)
    }

    private fun hideLoading() {
        stateJoke.value = JokesState.IsLoading(false)
    }

    private fun showError(error: ErrorModel) {
        stateJoke.value = JokesState.ShowError(error)
    }

    fun getRandomJoke() {
        viewModelScope.launch {
            getRandomJokeUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch {
                    hideLoading()
                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> {
                            joke.tryEmit(result.data)
                        }
                        is BaseResult.Error -> {
                            showError(result.rawResponse)
                        }
                    }
                }
        }
    }
}


sealed class JokesState {
    object Init : JokesState()
    data class IsLoading(val isLoading: Boolean) : JokesState()
    data class ShowError(val error: ErrorModel) : JokesState()
}