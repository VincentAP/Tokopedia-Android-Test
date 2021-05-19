package com.tokopedia.filter.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.filter.domain.IProductUseCase
import com.tokopedia.filter.model.ProductsDataItem
import com.tokopedia.filter.model.ProductsListItem
import com.tokopedia.filter.model.SelectedFilterItem

class ProductViewModel(
    private val productUseCase: IProductUseCase
) : ViewModel() {

    private var _selectedFilterItem: SelectedFilterItem? = null

    fun setItem(item: ProductsDataItem) {
        productUseCase.setItem(item)
    }

    fun getProductListItem(): ArrayList<ProductsListItem> = productUseCase.getProductListItem()

    fun getLocationsFilterType(): ArrayList<String> = productUseCase.getLocationsFilterType()

    fun getPriceFilterType(): Pair<Int, Int> =
        Pair(productUseCase.getMaxPrice(), productUseCase.getMinPrice())

    fun setSelectedFilterItem(
        location: String?,
        minPrice: Int,
        maxPrice: Int
    ) {
        _selectedFilterItem = SelectedFilterItem(location, minPrice, maxPrice)
    }

    fun getSelectedFilterItem(): SelectedFilterItem? = _selectedFilterItem
}