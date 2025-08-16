@file:OptIn(ExperimentalUuidApi::class)

package com.syouth.kmapper.data.mappers.pojoClassTest

import com.syouth.kmapper.data.mappers.CommonMapper
import com.syouth.kmapper.data.models.pojoClassTest.AddressEntity
import com.syouth.kmapper.data.models.pojoClassTest.UserEntity
import com.syouth.kmapper.domain.models.pojoClassTest.AdvancedUser
import com.syouth.kmapper.domain.models.pojoClassTest.DomainAddress
import com.syouth.kmapper.domain.models.pojoClassTest.DomainUser
import com.syouth.kmapper.domain.models.pojoClassTest.SimpleUser
import com.syouth.kmapper.processor_annotations.Mapper
import kotlin.uuid.ExperimentalUuidApi

/**
 * Initially created on 1/25/23.
 */
@Mapper
internal interface DomainUser2EntityCommonMapper : CommonMapper<DomainUser, UserEntity>

@Mapper
internal interface DomainAddress2EntityCommonMapper : CommonMapper<DomainAddress, AddressEntity>

@Mapper
internal interface UserEntity2DomainCommonMapper : CommonMapper<UserEntity, DomainUser>

@Mapper
internal interface AddressEntity : CommonMapper<AddressEntity, DomainAddress>

@Mapper
internal interface Simple2AdvancedUserCommonMapper : CommonMapper<SimpleUser, AdvancedUser>


