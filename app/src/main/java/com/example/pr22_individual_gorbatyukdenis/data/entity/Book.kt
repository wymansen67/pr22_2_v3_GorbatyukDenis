package com.example.pr22_individual_gorbatyukdenis.data.entity

import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import android.text.format.DateFormat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey (autoGenerate = true) var bid: Int? = null,
    @ColumnInfo (name = "title") var title: String?,
    @ColumnInfo (name = "author") var author: String?,
    @ColumnInfo (name = "publish_date") var publish_date: String?,
    @ColumnInfo (name = "publisher") var publisher: String?
)