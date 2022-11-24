package com.awto.randomjoke.di

import android.content.Context
import com.awto.randomjoke.BuildConfig
import com.awto.randomjoke.MyApplication
import com.awto.randomjoke.data.remote.JokeApi
import com.awto.randomjoke.data.repository.JokeRepositoryImpl
import com.awto.randomjoke.domain.common.repository.JokeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            client(okHttp)
            baseUrl(BuildConfig.BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(loggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor)
        }.build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor() : HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Singleton
    @Provides
    fun provideRepository(jokeApi: JokeApi): JokeRepository {
        return JokeRepositoryImpl(jokeApi)
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): JokeApi {
        return retrofit.create(JokeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): MyApplication {
        return context as MyApplication
    }
}