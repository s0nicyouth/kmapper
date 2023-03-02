package com.syouth.kmapper.testload

import com.syouth.kmapper.testload.domain.pojoClassTest.DomainAddress
import com.syouth.kmapper.testload.domain.pojoClassTest.DomainUser
import com.syouth.kmapper.testload.domain.pojoClassTest.SimpleUser
import com.syouth.kmapper.testload.mappers.pojoClassTest.DomainUser2EntityMapperImpl
import com.syouth.kmapper.testload.mappers.pojoClassTest.UserEntity2DomainMapperImpl
import com.syouth.kmapper.testload.mappers.pojoClassTest.Simple2AdvancedUserMapperImpl

import java.util.UUID

fun main() {
    val addressBaker = DomainAddress(UUID.randomUUID(), "10 Baker St", "London")
    val addressScot = DomainAddress(UUID.randomUUID(), "10 Wood St", "Edinburgh")
    val user = DomainUser(
        UUID.randomUUID(), "sherlock", "Sherlock", "Holmes",
        listOf(addressBaker, addressScot))

    val domainUser2EntityMapper = DomainUser2EntityMapperImpl()
    val userEntity = domainUser2EntityMapper.map(user)
    println(userEntity)

    val entity2DomainMapper = UserEntity2DomainMapperImpl()
    println(entity2DomainMapper.map(userEntity))


    val simpleUser = SimpleUser("Sherlock", "Holmes")
    val simple2AdvancedUserMapper = Simple2AdvancedUserMapperImpl()
    val advancedUser = simple2AdvancedUserMapper.map(simpleUser)
    println(advancedUser)

}
