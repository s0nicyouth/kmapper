package com.syouth.kmapper.data

import com.syouth.kmapper.data.mappers.CommonMapper
import com.syouth.kmapper.data.mappers.bindTest.BindMapperImpl
import com.syouth.kmapper.data.mappers.exhanustiveTest.ExampleCommonMapperImpl
import com.syouth.kmapper.data.mappers.interfaceConverterTest.InterfaceConverterCommonMapperImpl
import com.syouth.kmapper.data.mappers.listConvertionTest.ListCommonMapperImpl
import com.syouth.kmapper.data.mappers.mapTest.MapTestCommonMapperImpl
import com.syouth.kmapper.data.mappers.moneyMapperTest.MoneyTestCommonMapperImpl
import com.syouth.kmapper.data.mappers.pojoClassTest.AddressEntityImpl
import com.syouth.kmapper.data.mappers.pojoClassTest.UserEntity2DomainCommonMapperImpl
import com.syouth.kmapper.data.models.bindTest.BindDto
import com.syouth.kmapper.data.models.exhaustiveTest.ExampleDto
import com.syouth.kmapper.data.models.interfaceConvertersTest.InterfaceTestDto
import com.syouth.kmapper.data.models.listConvertionTest.ListDto
import com.syouth.kmapper.data.models.mapTest.MapTestDto
import com.syouth.kmapper.data.models.moneyMapperTest.MoneyDto
import com.syouth.kmapper.data.models.pojoClassTest.AddressEntity
import com.syouth.kmapper.data.models.pojoClassTest.UserEntity
import kotlin.reflect.KClass
import kotlin.uuid.ExperimentalUuidApi

internal class MappersHolder {

    @OptIn(ExperimentalUuidApi::class)
    private val factories: Map<KClass<*>, CommonMapper<*, *>> = buildMap {
        registerMapper<BindDto>(creator = BindMapperImpl())
        registerMapper<ExampleDto>(ExampleCommonMapperImpl())
        registerMapper<InterfaceTestDto>(InterfaceConverterCommonMapperImpl())
        registerMapper<ListDto>(ListCommonMapperImpl())
        registerMapper<MapTestDto>(MapTestCommonMapperImpl())
        registerMapper<MoneyDto>(MoneyTestCommonMapperImpl())
        registerMapper<UserEntity>(UserEntity2DomainCommonMapperImpl())
        registerMapper<AddressEntity>(AddressEntityImpl())
        //registerMapper(DomainUser2EntityMapperImpl::class, DomainUser2EntityMapperImpl())
        //registerMapper(Simple2AdvancedUserMapperImpl::class, Simple2AdvancedUserMapperImpl())
        //registerMapper(RecursiveDataClassTestMapperImpl::class, RecursiveDataClassTestMapperImpl())

    }

    @Suppress("UNCHECKED_CAST")
    fun <DTO : Any, DOMAIN : Any> getMapperByDtoType(dtoType: KClass<DTO>):
            CommonMapper<DTO, DOMAIN> {
        return factories[dtoType] as? CommonMapper<DTO, DOMAIN>
            ?: throw IllegalStateException("No mapper found for type $dtoType")
    }

    private inline fun <reified T> MutableMap<KClass<*>, CommonMapper<*, *>>.registerMapper(creator: CommonMapper<T, *>) =
        put(T::class, creator)

}
