package com.tokopedia.filter.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProductsDataItem(
    val data: ProductsItem
)

data class ProductsItem(
    val products: List<ProductsListItem>
)

@Parcelize
data class ProductsListItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val priceInt: Int,
    val discountPercentage: Int,
    val slashedPriceInt: Int,
    val shop: ShopItem
) : Parcelable

@Parcelize
data class ShopItem(
    val id: Int,
    val name: String,
    val city: String
) : Parcelable

// Filter Model
@Parcelize
data class SelectedFilterItem(
    val selectedLocation: String?,
    val selectedMinPrice: Int,
    val selectedMaxPrice: Int
) : Parcelable