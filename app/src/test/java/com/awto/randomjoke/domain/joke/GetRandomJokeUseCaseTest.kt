package com.awto.randomjoke.domain.joke

import com.awto.randomjoke.data.remote.model.ErrorModel
import com.awto.randomjoke.data.remote.model.Flags
import com.awto.randomjoke.data.remote.model.JokeResponseModel
import com.awto.randomjoke.domain.common.repository.JokeRepository
import com.awto.randomjoke.util.BaseResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetRandomJokeUseCaseTest {

    @RelaxedMockK
    private lateinit var jokeRepository: JokeRepository

    lateinit var getRandomJokeUseCase: GetRandomJokeUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getRandomJokeUseCase = GetRandomJokeUseCase(jokeRepository)
    }

    @Test
    fun `when the api get fails then returns exception`() = runBlocking {
        //given
        coEvery {
            jokeRepository.getJoke()
        } returns flowOf()

        //when
        getRandomJokeUseCase()

        //then
        coVerify(exactly = 1) {
            jokeRepository.getJoke() is BaseResult.Exception<*>
        }
    }


    @Test
    fun `when the response is successful then return the mapped object`() = runBlocking {
        //given
        val flags = Flags(
            nsfw = false,
            religious = false,
            political = false,
            racist = false,
            sexist = false,
            explicit = false
        )
        val jokeTest = JokeResponseModel(
            error = false,
            category = "Programming",
            type = "twopart",
            setup = "Why does no one like SQLrillex?",
            delivery = "He keeps dropping the database.",
            flags = flags,
            id = 239,
            safe = false,
            lang = "en"
        )
        coEvery {
            jokeRepository.getJoke()
        } returns flowOf(BaseResult.Success(jokeTest))

        lateinit var data: JokeResponseModel
        //when
        val response = getRandomJokeUseCase.invoke()
        response.collect {
            when (it) {
                is BaseResult.Success -> {
                    data = it.data
                }
                else -> {}
            }
        }
        //then
        coVerify(exactly = 1) {
            jokeRepository.getJoke()
        }
        assert(jokeTest == data)
    }

    @Test
    fun `when the response is error then return the mapped error object`() = runBlocking {
        //given
        val jokeErrorTest = ErrorModel(
            error = true,
            internalError = false,
            code = 106,
            message = "No matching joke found",
            causedBy = listOf("No jokes were found that match your provided filter(s)."),
            additionalInfo = "The specified category/ies is/are invalid. Got: \"(empty)\" but possible categories are: \"Any, Misc, Programming, Dark, Pun, Spooky, Christmas\" (case insensitive).",
            timestamp = 1669276419646
        )
        coEvery {
            jokeRepository.getJoke()
        } returns flowOf(BaseResult.Error(jokeErrorTest))

        lateinit var data: ErrorModel
        //when
        val response = getRandomJokeUseCase.invoke()
        response.collect {
            when (it) {
                is BaseResult.Error -> {
                    data = it.rawResponse
                }
                else -> {}
            }
        }
        //then
        coVerify(exactly = 1) {
            jokeRepository.getJoke()
        }
        assert(jokeErrorTest == data)
    }

}