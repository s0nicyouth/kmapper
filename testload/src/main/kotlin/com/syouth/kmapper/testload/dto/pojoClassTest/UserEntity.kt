package com.syouth.kmapper.testload.dto.pojoClassTest

import java.util.UUID

/**
 * jakarta.persistence.Entity
 */
class UserEntity(
    var id: UUID,
    var login: String,
    var firstname: String,
    var lastname: String,
    // oneToMany
    var addresses: MutableList<AddressEntity> = mutableListOf()) {

    override fun toString() : String {
        return "UserEntity(id=$id, login=$login, firstname=$firstname, lastname=$lastname, addresses=$addresses)"
    }
}

/**
 * jakarta.persistence.Entity
 */
class AddressEntity(
    var id: UUID,
    var street: String,
    var town: String) {
    override fun toString(): String {
        return "AddressEntity(id=$id, street=$street, town=$town)"
    }
}
