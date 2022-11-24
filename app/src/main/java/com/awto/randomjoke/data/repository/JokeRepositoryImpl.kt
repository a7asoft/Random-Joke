package com.awto.randomjoke.data.repository

import com.awto.randomjoke.data.remote.JokeApi
import com.awto.randomjoke.data.remote.model.ErrorModel
import com.awto.randomjoke.data.remote.model.JokeResponseModel
import com.awto.randomjoke.domain.common.repository.JokeRepository
import com.awto.randomjoke.util.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class JokeRepositoryImpl @Inject constructor(private val jokeApi: JokeApi) : JokeRepository {
    override suspend fun getJoke(): Flow<BaseResult<JokeResponseModel, ErrorModel>> {
        return flow {
            val response = jokeApi.getRandomJoke()
            if (response.isSuccessful && response.code() == 200) {
                val body = response.body()!!
                emit(BaseResult.Success(body))
            } else {
                val type = object : TypeToken<ErrorModel>() {}.type
                val err = Gson().fromJson<ErrorModel>(
                    response.errorBody()!!.charStream(),
                    type
                )!!
                emit(BaseResult.Error(err))
            }
        }
    }
}