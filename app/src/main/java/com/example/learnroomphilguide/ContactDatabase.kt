package com.example.learnroomphilguide

import androidx.room.Database
import androidx.room.RoomDatabase

//định nghĩa ContactDatabase - lớp Database sử dụng Room.
@Database( // Đánh dấu đây là lớp Database.
    entities = [Contact::class],
    version = 1
)

//Trả về đối tượng ContactDao để thực hiện các truy vấn và thao tác với dữ liệu Contact.
abstract class ContactDatabase: RoomDatabase() {
    abstract val dao: ContactDao
}