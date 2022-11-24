package com.awto.randomjoke.domain.joke

import com.awto.randomjoke.data.remote.model.ErrorModel
import com.awto.randomjoke.data.remote.model.JokeResponseModel
import com.awto.randomjoke.domain.common.repository.JokeRepository
import com.awto.randomjoke.util.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRandomJokeUseCase @Inject constructor(private val jokeRepository: JokeRepository){
    suspend fun invoke(): Flow<BaseResult<JokeResponseModel, ErrorModel, Exception>> {
        return jokeRepository.getJoke()
    }
}