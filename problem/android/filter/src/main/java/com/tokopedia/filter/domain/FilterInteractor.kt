package com.tokopedia.filter.domain

import com.tokopedia.filter.model.ProductsListItem

class FilterInteractor : IFilterUseCase {

    private var productListItem: ArrayList<ProductsListItem>? = null
    private val item: MutableList<ProductsListItem> = mutableListOf()

    private var itemRemoved = false

    override fun setProductListItem(item: ArrayList<ProductsListItem>?) {
        productListItem = item
    }

    override fun applyFilter(
        location: String?,
        minPrice: Int,
        maxPrice: Int
    ) {
        item.clear()
        // Filter based on location
        location?.let { loc ->
            productListItem?.forEach {
                if (it.shop.city.equals(loc, true)) item.add(it)
            }
        }

        // Filter based on price
        itemRemoved = false
        if (item.isNotEmpty()) {
            val iterator = item.iterator()
            while (iterator.hasNext()) {
                val it = iterator.next()
                if (it.priceInt < minPrice || it.priceInt > maxPrice) {
                    iterator.remove()
                    itemRemoved = true
                }
            }
        } else {
            productListItem?.forEach {
                if (it.priceInt in minPrice..maxPrice) item.add(it)
            }
        }
    }

    override fun getProductListItem(): List<ProductsListItem> =
        if (item.isEmpty() && !itemRemoved) productListItem?.toList() ?: emptyList() else item
}