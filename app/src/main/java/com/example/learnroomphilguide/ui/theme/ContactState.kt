package com.example.learnroomphilguide.ui.theme

import com.example.learnroomphilguide.Contact
import com.example.learnroomphilguide.ContactEvent
import com.example.learnroomphilguide.SortType

data class ContactState(
    val contacts: List<Contact> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val isAddingContact: Boolean = false,
    val sortType: SortType = SortType.FIRST_NAME,
)
