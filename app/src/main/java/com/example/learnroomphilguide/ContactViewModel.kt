package com.example.learnroomphilguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnroomphilguide.ui.theme.ContactState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//ContactViewModel để quản lý state và xử lý các sự kiện cho màn hình danh bạ liên hệ.
class ContactViewModel(
    private val dao: ContactDao //đầu vào là 1 đối tượng ở class ContactDao
):ViewModel() {
    //khởi tạo một MutableStateFlow để lưu trữ giá trị kiểu sắp xếp danh bạ, mặc định bao đầu là sắp xếp theo firstName
    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)
    @OptIn(ExperimentalCoroutinesApi::class)
    //sử dụng flatMapLatest để switch giữa các luồng dữ liệu danh sách liên hệ dựa trên giá trị của _sortType.
    private val _contacts = _sortType.flatMapLatest { sortType ->
         when(sortType){
             SortType.FIRST_NAME -> dao.getContactByFirstName()
             SortType.LAST_NAME -> dao.getContactByLastName()
             SortType.PHONE_NUMBER -> dao.getContactByPhoneNumber()
         }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList()) //Snapshot trạng thái của luồng để tránh rò rỉ bộ nhớ.
    //khởi tạo một MutableStateFlow để lưu trữ trạng thái hiện tại của ui
    private val _state = MutableStateFlow(ContactState())
    //sử dụng combine() để kết hợp 3 luồng dữ liệu _state, _sortType và _contacts thành một luồng duy nhất cung cấp cho UI.
    val state = combine(_state,_sortType,_contacts) //Kết hợp nhiều luồng dữ liệu thành một.
    {state,sortType,contacts ->
        //Trả về một bản sao của state, cập nhật giá trị mới từ các luồng khác.
        state.copy(
            contacts = contacts,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())//Snapshot trạng thái của luồng để tránh rò rỉ bộ nhớ.

    //Hàm xử lý các sự kiện ContactEvent.
    fun onEvent(event: ContactEvent){
        when(event) //Kiểm tra xem event thuộc loại nào để xử lý phù hợp.
        {
            //Nếu là sự kiện xóa contacts
            is ContactEvent.DeleteContacts -> {
                //Khởi chạy một coroutine trong viewModelScope để xóa liên hệ.
                viewModelScope.launch {
                    //Gọi phương thức xóa trên dao, truyền vào đối tượng liên hệ cần xóa.
                    dao.deleteContact(event.contact)
                }
            }

            //Nếu là sự kiện ẩn dialog
            ContactEvent.HideDialog -> {
                //Cập nhật lại luồng trạng thái _state.
                _state.update {
                    it.copy(
                        //Trả về một bản sao của trạng thái hiện tại, đặt isAddingContact = false để tắt dialog.
                        isAddingContact = false
                    )
                }
            }
            //Nếu là sự kiện nhập firstName
            ContactEvent.SaveContact -> {
                //Lấy các giá trị firstName, lastName, phoneNumber hiện tại từ trạng thái state.
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber

                //Kiểm tra validate dữ liệu đầu vào, nếu đang trống thì thoát khỏi hàm.
                if(firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()){
                    return
                }

                //Khởi tạo đối tượng Contact mới với các giá trị nhập.
                val contact = Contact(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber
                )

                //Sử dụng viewModelScope khởi chạy coroutine gọi dao để lưu liên hệ mới xuống database.
                viewModelScope.launch {
                    dao.UpsertContact(contact)
                }

                //Cập nhật lại trạng thái _state:
                _state.update {
                    it.copy(
                        //Đóng dialog thêm liên hệ: isAddingContact = false
                        isAddingContact = false,
                        //Xóa các giá trị đã nhập.
                        firstName = "",
                        lastName = "",
                        phoneNumber = ""
                    )

                }
            }
            //Nếu là sự kiện nhập firstName
            is ContactEvent.SetFirstName -> {
                //Cập nhật lại luồng trạng thái _state.
                _state.update {
                    //Tạo bản sao của trạng thái hiện tại, gán giá trị firstName bằng giá trị nhập từ event.
                    it.copy(
                        firstName = event.firstName
                    )
                }
            }
            //Nếu là sự kiện nhập lastName
            is ContactEvent.SetLastName -> {
                //Cập nhật lại luồng trạng thái _state.
                _state.update {
                    //Tạo bản sao của trạng thái hiện tại, gán giá trị lastName bằng giá trị nhập từ event.
                    it.copy(
                        lastName = event.lastName
                    )
                }
            }
            //Nếu là sự kiện nhập PhoneNumber
            is ContactEvent.SetPhoneNumber -> {
                //Cập nhật lại luồng trạng thái _state.
                _state.update {
                    //Tạo bản sao của trạng thái hiện tại, gán giá trị PhoneNumber bằng giá trị nhập từ event.
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }
            //Nếu là sự kiện hiện dialog
            ContactEvent.ShowDialog -> {
                //Cập nhật lại luồng trạng thái _state.
                _state.update {
                    //Trả về một bản sao của trạng thái hiện tại, đặt isAddingContact = true để bật dialog.
                    it.copy(
                        isAddingContact = true
                    )
                }
            }
            //Nếu là sự kiện thay đổi kiểu sắp xếp.
            is ContactEvent.SortContacts -> {
                //Gán giá trị mới cho _sortType bằng giá trị sortType từ sự kiện.
                _sortType.value = event.sortType
            }
        }
    }
}