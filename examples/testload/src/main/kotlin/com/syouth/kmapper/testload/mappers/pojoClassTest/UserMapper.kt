package com.syouth.kmapper.testload.mappers.pojoClassTest

import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.testload.domain.pojoClassTest.DomainAddress
import com.syouth.kmapper.testload.domain.pojoClassTest.DomainUser
import com.syouth.kmapper.testload.domain.pojoClassTest.AdvancedUser
import com.syouth.kmapper.testload.domain.pojoClassTest.SimpleUser
import com.syouth.kmapper.testload.dto.pojoClassTest.AddressEntity
import com.syouth.kmapper.testload.dto.pojoClassTest.UserEntity

/**
 * Initially created on 1/25/23.
 */
@Mapper
internal interface DomainUser2EntityMapper {
    fun map(du: DomainUser) : UserEntity
}
@Mapper
internal interface DomainAddress2EntityMapper {
    fun map(da: DomainAddress) : AddressEntity
}

@Mapper
internal interface UserEntity2DomainMapper {
    fun map(ue: UserEntity) : DomainUser
}

@Mapper
internal interface AddressEntity {
    fun map(ae: AddressEntity) : DomainAddress
}

@Mapper
internal interface Simple2AdvancedUserMapper {
    fun map(su: SimpleUser) : AdvancedUser
}
