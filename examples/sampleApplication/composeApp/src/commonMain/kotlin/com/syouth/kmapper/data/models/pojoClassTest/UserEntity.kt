@file:OptIn(ExperimentalUuidApi::class)

package com.syouth.kmapper.data.models.pojoClassTest

import com.syouth.kmapper.data.models.Creator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


/**
 * jakarta.persistence.Entity
 */

internal class UserEntity(
    var id: Uuid,
    var login: String,
    var firstname: String,
    var lastname: String,
    // oneToMany
    var addresses: MutableList<AddressEntity> = mutableListOf()
) {

    override fun toString(): String {
        return "UserEntity(id=$id, login=$login, firstname=$firstname, lastname=$lastname, addresses=$addresses)"
    }

    companion object : Creator<UserEntity> {
        override fun create() = UserEntity(
            id = Uuid.random(),
            login = "libero",
            firstname = "Herminia Bates",
            lastname = "Shelly Olson",
            addresses = mutableListOf(
                AddressEntity.create(),
                AddressEntity.create()
            )
        )
    }
}

/**
 * jakarta.persistence.Entity
 */

@ExperimentalUuidApi
internal class AddressEntity(
    var id: Uuid,
    var street: String,
    var town: String
) {
    override fun toString(): String {
        return "AddressEntity(id=$id, street=$street, town=$town)"
    }

    companion object : Creator<AddressEntity> {
        override fun create() = AddressEntity(
            id = Uuid.random(),
            street = "Elm Street",
            town = "Springfield"
        )
    }
}
