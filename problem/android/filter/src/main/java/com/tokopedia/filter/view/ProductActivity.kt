package com.tokopedia.filter.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.filter.R
import com.tokopedia.filter.Util
import com.tokopedia.filter.adapter.RecyclerAdapter
import com.tokopedia.filter.di.Injection
import com.tokopedia.filter.model.ProductsDataItem
import com.tokopedia.filter.viewmodel.ProductViewModel
import com.tokopedia.filter.viewmodel.ProductViewModelFactory
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : AppCompatActivity() {

    private val recyclerAdapter: RecyclerAdapter by lazy { RecyclerAdapter() }

    private lateinit var productViewModel: ProductViewModel

    private lateinit var productItem: ProductsDataItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        initialize()
        initListener()
    }

    private fun initialize() {
        val productViewModelFactory = ProductViewModelFactory(Injection.provideProductUseCase())
        productViewModel =
            ViewModelProvider(this, productViewModelFactory).get(ProductViewModel::class.java)
        productItem = Util.getProductJson(applicationContext)

        productViewModel.setItem(productItem)

        product_list.apply {
            layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
            adapter = recyclerAdapter
        }
        recyclerAdapter.submitList(productItem.data.products)
    }

    private fun initListener() {
        buttonFilter.setOnClickListener {
            FilterFragment.newInstance(
                productViewModel.getProductListItem(),
                productViewModel.getLocationsFilterType(),
                productViewModel.getPriceFilterType().first,
                productViewModel.getPriceFilterType().second,
                productViewModel.getSelectedFilterItem()
            )
                .setOnFilterApplied { productItem, location, minPrice, maxPrice ->
                    if (productItem.isEmpty()) {
                        product_list.visibility = View.GONE
                        textNoData.visibility = View.VISIBLE
                    } else {
                        product_list.visibility = View.VISIBLE
                        textNoData.visibility = View.GONE
                        recyclerAdapter.submitList(productItem)
                        Handler(Looper.getMainLooper()).postDelayed({
                            product_list.scrollToPosition(0)
                        }, DELAY_TIME)
                    }
                    productViewModel.setSelectedFilterItem(location, minPrice, maxPrice)
                }
                .show(supportFragmentManager, FilterFragment.TAG)
        }
    }

    companion object {
        private const val DELAY_TIME = 200L
    }
}