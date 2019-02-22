package org.givingkitchen.android.util

import com.google.firebase.storage.FirebaseStorage
import com.squareup.moshi.Moshi

object Services {
    val firebaseInstance = FirebaseStorage.getInstance()
    val moshi = Moshi.Builder().build()
}