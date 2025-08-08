package com.playit.constants

object SpotifyConfig {
    const val CLIENT_ID = "9062075d4bba4193bbd86882e9204537"
    const val CLIENT_SECRET = "15089e0413e54df3a060cc257d554933"
    const val SCOPES = "user-read-currently-playing,playlist-modify-private,playlist-read-private,playlist-modify-public"
    const val REDIRECT_URI = "playit://auth"
    const val AUTH_URL = "https://accounts.spotify.com/authorize"
    const val TOKEN_URL = "https://accounts.spotify.com/api/token"
    const val URL_PARAMS = "?client_id=$CLIENT_ID&response_type=code&redirect_uri=$REDIRECT_URI&scope=$SCOPES&show_dialog=TRUE"
}