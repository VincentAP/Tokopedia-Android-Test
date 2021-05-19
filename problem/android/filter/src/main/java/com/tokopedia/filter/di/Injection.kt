package com.tokopedia.filter.di

import com.tokopedia.filter.domain.FilterInteractor
import com.tokopedia.filter.domain.IFilterUseCase
import com.tokopedia.filter.domain.IProductUseCase
import com.tokopedia.filter.domain.ProductInteractor

object Injection {

    fun provideProductUseCase(): IProductUseCase = ProductInteractor()

    fun provideFilterUseCase(): IFilterUseCase = FilterInteractor()
}