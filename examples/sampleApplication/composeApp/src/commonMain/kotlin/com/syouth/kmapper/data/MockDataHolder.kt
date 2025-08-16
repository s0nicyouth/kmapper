package com.syouth.kmapper.data

import com.syouth.kmapper.data.models.Creator
import com.syouth.kmapper.data.models.bindTest.BindDto
import com.syouth.kmapper.data.models.exhaustiveTest.ExampleDto
import com.syouth.kmapper.data.models.listConvertionTest.ListDto
import com.syouth.kmapper.data.models.listConvertionTest.OtherListDto
import com.syouth.kmapper.data.models.mapTest.MapTestDto
import com.syouth.kmapper.data.models.moneyMapperTest.MoneyDto
import com.syouth.kmapper.data.models.pojoClassTest.AddressEntity
import com.syouth.kmapper.data.models.pojoClassTest.UserEntity
import kotlin.reflect.KClass
import kotlin.uuid.ExperimentalUuidApi

@ExperimentalUuidApi
internal class MockDataHolder {

    private val factories: Map<KClass<*>, Creator<*>> = buildMap {
        registerFactories(BindDto)
        registerFactories(ExampleDto)
        registerFactories(ListDto)
        registerFactories(OtherListDto)
        registerFactories(MapTestDto)
        registerFactories(MoneyDto)
        registerFactories(UserEntity)
        registerFactories(AddressEntity)
    }

    val randomDtoKClass: KClass<*> get() = factories.keys.random()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getDtoCreatorByType(type: KClass<T>): T {
        val creator: Creator<*> =
            factories[type] ?: throw IllegalArgumentException("No creator for $type")
        return creator.create() as T
    }

    private inline fun <reified T : Any> MutableMap<KClass<*>, Creator<*>>.registerFactories(creator: Creator<T>) =
        put(T::class, creator)

}