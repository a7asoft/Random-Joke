# Random Joke
## _The simplest jokes app ever_

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

Random Joke is a simple app to read random jokes, some of them are really good.
This project was created with the objective of demonstrating the use of good practices in Android development using MVVM + Clean architecture + Coroutines + Kotlin Flow + Hilt + Retrofit + SplashScreen API

## Features
- Get a categorized joke from a public API.
- Quick jump to other jokes
- Share jokes with other people


## Datasource
Data source used for this project:
- [JokesAPI](https://v2.jokeapi.dev/joke/Any) - Jokes public API


## Unit tests
Unit tests were applied to the use case with the possible edge cases that could occur.
In this example, a test case is designed for when the request response is satisfactory.
```
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
```

## Error handling

A complete verification of possible errors was carried out, so that the app reacts to any change in status or response from the server

```
    override suspend fun getJoke(): Flow<BaseResult<JokeResponseModel, ErrorModel, Exception>> {
        return flow {
            try {
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
            } catch (e: Exception) {
                emit(BaseResult.Exception(e))
            }
        }
    }
```
