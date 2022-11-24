package com.awto.randomjoke.data.remote.model

import com.google.gson.annotations.SerializedName

data class JokeResponseModel (
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("category")
    val category: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("joke")
    val joke: String? = "",

    @SerializedName("setup")
    val setup: String?= "",

    @SerializedName("delivery")
    val delivery: String?= "",

    @SerializedName("flags")
    val flags: Flags,

    @SerializedName("id")
    val id: Int,

    @SerializedName("safe")
    val safe: Boolean,

    @SerializedName("lang")
    val lang: String
)

data class Flags (
    @SerializedName("nsfw")
    val nsfw: Boolean,

    @SerializedName("religious")
    val religious: Boolean,

    @SerializedName("political")
    val political: Boolean,

    @SerializedName("racist")
    val racist: Boolean,

    @SerializedName("sexist")
    val sexist: Boolean,

    @SerializedName("explicit")
    val explicit: Boolean
)
