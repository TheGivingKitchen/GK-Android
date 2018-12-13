package org.thegivingkitchen.android.thegivingkitchen.util

import com.google.firebase.storage.FirebaseStorage
import com.squareup.moshi.Moshi

object Firebase {
    val firebaseInstance = FirebaseStorage.getInstance()
    val moshi = Moshi.Builder().build()
}