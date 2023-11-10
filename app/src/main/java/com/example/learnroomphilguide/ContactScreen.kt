package com.example.learnroomphilguide

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnroomphilguide.ui.theme.ContactState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ContactEvent.ShowDialog)
            }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add contact",
                )
            }
        },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        if (state.isAddingContact){
            AddContactDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ){
                    SortType.values().forEach {
                        Row(
                            modifier = Modifier
                                .clickable {
                                onEvent(ContactEvent.SortContacts(it))
                            }
                        ){
                            RadioButton(
                                selected = state.sortType == it,
                                onClick = {
                                    onEvent(ContactEvent.SortContacts(it))
                                })
                            Text(
                                text = it.name,
                                modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }
            }
            items(state.contacts){contact ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "${
                            contact.firstName} ${contact.lastName}" ,
                            fontSize = 20.sp
                        )
                        Text(
                            text = contact.phoneNumber,
                            fontSize = 14.sp
                        )
                    }
                    IconButton(onClick = {
                        onEvent(ContactEvent.DeleteContacts(contact))
                    }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete contact",
                        )
                    }
                }
            }
        }
    }
}