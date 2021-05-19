package com.tokopedia.filter.domain

import com.tokopedia.filter.model.ProductsListItem

interface IFilterUseCase {

    fun setProductListItem(item: ArrayList<ProductsListItem>?)

    fun applyFilter(
        location: String?,
        minPrice: Int,
        maxPrice: Int
    )

    fun getProductListItem(): List<ProductsListItem>
}