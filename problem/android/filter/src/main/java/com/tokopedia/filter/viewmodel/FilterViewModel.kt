package com.tokopedia.filter.viewmodel

import com.tokopedia.filter.domain.IFilterUseCase
import com.tokopedia.filter.model.ProductsListItem
import kotlin.properties.Delegates

class FilterViewModel(
    private val filterUseCase: IFilterUseCase
) {

    private var _selectedMinPrice by Delegates.notNull<Int>()
    private var _selectedMaxPrice by Delegates.notNull<Int>()
    private var _selectedLocation: String? = null

    fun applyFilter() {
        filterUseCase.applyFilter(
            _selectedLocation,
            _selectedMinPrice,
            _selectedMaxPrice
        )
    }

    fun setProductListItem(item: ArrayList<ProductsListItem>?) {
        filterUseCase.setProductListItem(item)
    }

    fun setSelectedLocation(location: String) {
        _selectedLocation = location
    }

    fun setSelectedMinPrice(minPrice: Int) {
        _selectedMinPrice = minPrice
    }

    fun setSelectedMaxPrice(maxPrice: Int) {
        _selectedMaxPrice = maxPrice
    }

    fun getProductListItem(): List<ProductsListItem> = filterUseCase.getProductListItem()

    fun getSelectedLocation(): String? = _selectedLocation

    fun getSelectedMinPrice(): Int = _selectedMinPrice

    fun getSelectedMaxPrice(): Int = _selectedMaxPrice
}