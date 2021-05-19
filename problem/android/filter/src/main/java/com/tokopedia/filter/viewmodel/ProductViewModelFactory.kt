package com.tokopedia.filter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.filter.domain.IProductUseCase

class ProductViewModelFactory(
    private val productUseCase: IProductUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(IProductUseCase::class.java)
            .newInstance(productUseCase)
    }
}