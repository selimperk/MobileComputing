package com.example.myapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface ContactStore {
    fun getContact(by: Int): Contact?
    fun getAllContacts(): LiveData<List<Contact>>
    fun addContact(firstName: String, lastName: String)
}

object FakeContactStore: ContactStore {

    private val contacts = mutableListOf(
        Contact(0, "Hans", "Peter"),
        Contact(1, "Jonas", "Müller"),
        Contact(2, "Niels", "Sabine"),
    )

    private val liveContacts = MutableLiveData(contacts.toList())
    override fun getContact(by: Int): Contact? = contacts.firstOrNull { it.id == by }

    override fun getAllContacts(): LiveData<List<Contact>> = liveContacts

    override fun addContact(firstName: String, lastName: String) {
        val nextId = contacts.maxBy { it.id }.id + 1
        val newContact = Contact(nextId, firstName, lastName)
        contacts += newContact

        liveContacts.postValue(contacts.toList())
    }

}
