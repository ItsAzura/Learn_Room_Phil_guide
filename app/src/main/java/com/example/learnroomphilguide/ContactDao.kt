package com.example.learnroomphilguide

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

//định nghĩa interface ContactDao để thao tác với dữ liệu Contact trong Room database.
@Dao
interface ContactDao {
    @Upsert //Chèn hoặc cập nhật một đối tượng Contact.
    suspend fun UpsertContact(contact: Contact)
    @Delete // Xóa một đối tượng Contact.
    suspend fun deleteContact(contact: Contact)

    // Các phương thức trả về danh sách Contact được sắp xếp theo firstName, lastName hoặc phoneNumber.
    @Query("Select * from Contact order by firstName ASC")
    fun getContactByFirstName(): Flow<List<Contact>>
    @Query("Select * from Contact order by lastName ASC")
    fun getContactByLastName(): Flow<List<Contact>>
    @Query("Select * from Contact order by phoneNumber ASC")
    fun getContactByPhoneNumber(): Flow<List<Contact>>
}