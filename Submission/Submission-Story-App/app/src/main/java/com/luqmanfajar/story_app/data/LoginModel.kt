package com.luqmanfajar.story_app.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginModel (
    var authToken: String? = null,
    var isLogin: Boolean = false
): Parcelable

