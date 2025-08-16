package com.syouth.kmapper.domain.models.pojoClassTest

/**
 * Simple Pojo objects
 */
class SimpleUser(var firstname: String, var lastname: String)

class AdvancedUser(var firstname: String, var lastname: String, val hairColour: String? = null) {
    override fun toString(): String {
        return "AdvancedUser(firstname=$firstname, lastname=$lastname, hairColour=$hairColour)"
    }
}
