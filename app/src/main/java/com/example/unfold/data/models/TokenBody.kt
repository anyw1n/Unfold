package com.example.unfold.data.models

import com.example.unfold.data.Api
import com.example.unfold.util.RedirectUri

data class TokenBody(
    val code: String,
    val client_id: String = Api.ClientId,
    val client_secret: String = Api.ClientSecret,
    val redirect_uri: String = RedirectUri,
    val grant_type: String = "authorization_code",
)
