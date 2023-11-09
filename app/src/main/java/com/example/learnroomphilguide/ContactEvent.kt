package com.example.learnroomphilguide

//định nghĩa các sự kiện liên quan đến màn hình quản lý danh bạ Contact.
sealed interface ContactEvent {
    //Lưu liên hệ
    object SaveContact: ContactEvent
    //Đặt giá trị cho các trường
    data class SetFirstName(val firstName: String):ContactEvent
    data class SetLastName(val lastName: String):ContactEvent
    data class SetPhoneNumber(val phoneNumber: String):ContactEvent
    //ShowDialog, HideDialog: Hiển thị/ẩn dialog
    object ShowDialog: ContactEvent
    object HideDialog: ContactEvent
    //SortContacts
    data class SortContacts(val sortType:SortType): ContactEvent
    //Xóa liên hệ
    data class DeleteContacts(val contact: Contact): ContactEvent
}