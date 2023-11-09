package com.example.learnroomphilguide

import androidx.room.Entity
import androidx.room.PrimaryKey

//định nghĩa một Entity class tên Contact để sử dụng với Room database.
@Entity
data class Contact (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
)
