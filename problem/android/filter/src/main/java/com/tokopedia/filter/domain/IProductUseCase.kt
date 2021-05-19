package com.tokopedia.filter.domain

import com.tokopedia.filter.model.ProductsDataItem
import com.tokopedia.filter.model.ProductsListItem

interface IProductUseCase {

    fun setItem(item: ProductsDataItem)

    fun getProductListItem(): ArrayList<ProductsListItem>

    fun getLocationsFilterType(): ArrayList<String>

    fun getMaxPrice(): Int

    fun getMinPrice(): Int
}