package com.tokopedia.filter.domain

import com.tokopedia.filter.model.ProductsDataItem
import com.tokopedia.filter.model.ProductsListItem

class ProductInteractor : IProductUseCase {

    private var productListItem: ArrayList<ProductsListItem> = arrayListOf()
    private var locationsFilter: ArrayList<String> = arrayListOf()
    private var maxPrice = Int.MIN_VALUE
    private var minPrice = Int.MAX_VALUE

    override fun setItem(item: ProductsDataItem) {
        val totalFrequentMap = mutableMapOf<String, Int>()
        productListItem.apply {
            clear()
            addAll(item.data.products)
        }
        locationsFilter.clear()

        item.data.products.forEach {
            val currentCity = it.shop.city
            if (totalFrequentMap[currentCity] != null) {
                val currentCityOccurrence = totalFrequentMap[currentCity] ?: 0
                totalFrequentMap[currentCity] = currentCityOccurrence + 1
            } else totalFrequentMap[it.shop.city] = 1

            // Find Max Price
            if (it.priceInt > maxPrice) maxPrice = it.priceInt

            // Find Min Price
            if (it.priceInt < minPrice) minPrice = it.priceInt
        }

        val result = totalFrequentMap.toList().sortedByDescending { (_, value) -> value }.toMap()

        var idx = 0
        for (key in result.keys) {
            if (idx == 2) break
            locationsFilter.add(key)
            idx++
        }
        locationsFilter.add("Other")
    }

    override fun getProductListItem(): ArrayList<ProductsListItem> = productListItem

    override fun getLocationsFilterType(): ArrayList<String> = locationsFilter

    override fun getMaxPrice(): Int = maxPrice

    override fun getMinPrice(): Int = minPrice
}