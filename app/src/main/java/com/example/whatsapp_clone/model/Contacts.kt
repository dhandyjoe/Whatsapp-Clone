package com.example.whatsapp_clone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contacts(
    val name: String?,
    val phone: String?
) : Parcelable
