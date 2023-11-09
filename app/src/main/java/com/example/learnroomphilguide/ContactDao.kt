package com.example.learnroomphilguide

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

interface ContactDao {

    @Upsert
    suspend fun UpsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("Select * from Contact order by firstName ASC")
    fun getContactByFirstName(): Flow<List<Contact>>

    @Query("Select * from Contact order by lastName ASC")
    fun getContactByLastName(): Flow<List<Contact>>

    @Query("Select * from Contact order by phoneNumber ASC")
    fun getContactByPhoneNumber(): Flow<List<Contact>>
}