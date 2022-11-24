package com.awto.randomjoke.domain.common.repository

import com.awto.randomjoke.data.remote.model.ErrorModel
import com.awto.randomjoke.data.remote.model.JokeResponseModel
import com.awto.randomjoke.util.BaseResult
import kotlinx.coroutines.flow.Flow

interface JokeRepository {
    suspend fun getJoke(): Flow<BaseResult<JokeResponseModel, ErrorModel>>
}