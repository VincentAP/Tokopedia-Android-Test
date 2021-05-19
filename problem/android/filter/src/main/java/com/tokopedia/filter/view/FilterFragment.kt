package com.tokopedia.filter.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider
import com.tokopedia.filter.BaseRoundedBottomSheetDialogFragment
import com.tokopedia.filter.R
import com.tokopedia.filter.di.Injection
import com.tokopedia.filter.model.ProductsListItem
import com.tokopedia.filter.model.SelectedFilterItem
import com.tokopedia.filter.toRupiahCurrencyFormat
import com.tokopedia.filter.viewmodel.FilterViewModel
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : BaseRoundedBottomSheetDialogFragment() {

    private var listener: ((productItem: List<ProductsListItem>, location: String?, minPrice: Int, maxPrice: Int) -> Unit)? =
        null

    private var productsListItem: ArrayList<ProductsListItem>? = null

    private var selectedFilterItem: SelectedFilterItem? = null

    private val locations: ArrayList<String> by extraNotNull(LOCATIONS_ITEM, arrayListOf())

    private val maxPrice: Int by extraNotNull(MAX_VALUE, 100)

    private val minPrice: Int by extraNotNull(MIN_VALUE, 0)

    private lateinit var filterViewModel: FilterViewModel

    private var applyButton: AppCompatButton? = null

    fun setOnFilterApplied(listener: (productItem: List<ProductsListItem>, location: String?, minPrice: Int, maxPrice: Int) -> Unit) =
        apply {
            this.listener = listener
        }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        bottomSheetDialog.setOnShowListener {
            val coordinator = (it as BottomSheetDialog)
                .findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)
            val containerLayout =
                it.findViewById<FrameLayout>(com.google.android.material.R.id.container)
            val buttonView =
                bottomSheetDialog.layoutInflater.inflate(R.layout.sticky_button_bottom_sheet, null)

            buttonView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM
            }
            containerLayout!!.addView(buttonView)

            buttonView.post {
                (coordinator!!.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    buttonView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    )
                    this.bottomMargin = buttonView.measuredHeight
                    containerLayout.requestLayout()
                }
            }

            applyButton = buttonView.findViewById(R.id.buttonApply)
            applyButton?.setOnClickListener {
                listener?.invoke(
                    filterViewModel.getProductListItem(),
                    filterViewModel.getSelectedLocation(),
                    filterViewModel.getSelectedMinPrice(),
                    filterViewModel.getSelectedMaxPrice()
                )
                dismiss()
            }
        }
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productsListItem = arguments?.getParcelableArrayList<ProductsListItem>(PRODUCT_LIST_ITEM)
        selectedFilterItem = arguments?.getParcelable(SELECTED_FILTER_ITEM)
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFilterViewModel()
        setupChip()
        setupSlider()
    }

    private fun setupChip() {
        // Variable to handle chip selection state
        var currentSelection: String? = null

        for (i in 0 until locations.size) {
            val chip =
                layoutInflater.inflate(R.layout.chip_item, chipGroupLocationFilter, false) as Chip
            chip.apply {
                text = locations[i]
                id = ViewCompat.generateViewId()
                setOnCheckedChangeListener { compoundButton, isChecked ->
                    val selectedLocation = if (isChecked) compoundButton.text.toString() else null
                    if (!currentSelection.equals(
                            selectedLocation,
                            true
                        ) && !selectedLocation.isNullOrEmpty()
                    ) {
                        filterViewModel.setSelectedLocation(selectedLocation.toString())
                        filterViewModel.applyFilter()
                        updateButtonText()
                    }
                    currentSelection = selectedLocation
                }
            }
            chipGroupLocationFilter.apply {
                addView(chip)

                selectedFilterItem?.let {
                    if (it.selectedLocation.equals(chip.text.toString(), true)) {
                        check(chip.id)
                    }
                }
            }
        }
    }

    private fun setupSlider() {
        sliderPrice.apply {
            valueFrom = minPrice.toFloat()
            valueTo = maxPrice.toFloat()

            if (selectedFilterItem != null) {
                selectedFilterItem?.let {
                    textCurrentMinimum.text =
                        getString(R.string.min_price, it.selectedMinPrice.toRupiahCurrencyFormat())
                    textCurrentMaximum.text =
                        getString(R.string.max_price, it.selectedMaxPrice.toRupiahCurrencyFormat())
                    setValues(it.selectedMinPrice.toFloat(), it.selectedMaxPrice.toFloat())
                }
            } else {
                textCurrentMinimum.text =
                    getString(R.string.min_price, minPrice.toRupiahCurrencyFormat())
                textCurrentMaximum.text =
                    getString(R.string.max_price, maxPrice.toRupiahCurrencyFormat())
                setValues(minPrice.toFloat(), maxPrice.toFloat())
            }

            setLabelFormatter { value: Float ->
                value.toRupiahCurrencyFormat()
            }

            addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                    // Responds to when slider's touch event is being started
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    filterViewModel.applyFilter()
                    updateButtonText()
                }
            })

            addOnChangeListener { _, _, _ ->
                filterViewModel.setSelectedMinPrice(this.values[0].toInt())
                filterViewModel.setSelectedMaxPrice(this.values[1].toInt())

                textCurrentMinimum.text = getString(
                    R.string.min_price,
                    filterViewModel.getSelectedMinPrice().toRupiahCurrencyFormat()
                )

                textCurrentMaximum.text = getString(
                    R.string.max_price,
                    filterViewModel.getSelectedMaxPrice().toRupiahCurrencyFormat()
                )
            }
        }
    }

    private fun updateButtonText() {
        applyButton?.apply {
            text = getString(R.string.show_n_items, filterViewModel.getProductListItem().size)
        }
    }

    private fun setupFilterViewModel() {
        filterViewModel = FilterViewModel(Injection.provideFilterUseCase())
        filterViewModel.apply {
            setProductListItem(productsListItem)
            if (selectedFilterItem != null) {
                setSelectedMinPrice(selectedFilterItem!!.selectedMinPrice)
                setSelectedMaxPrice(selectedFilterItem!!.selectedMaxPrice)
            } else {
                setSelectedMinPrice(minPrice)
                setSelectedMaxPrice(maxPrice)
            }
        }
    }

    companion object {
        @JvmField
        val TAG: String = FilterFragment::class.java.name
        private const val LOCATIONS_ITEM = "LOCATIONS_ITEM"
        private const val MAX_VALUE = "MAX_VALUE"
        private const val MIN_VALUE = "MIN_VALUE"
        private const val PRODUCT_LIST_ITEM = "PRODUCT_LIST_ITEM"
        private const val SELECTED_FILTER_ITEM = "SELECTED_FILTER_ITEM"

        @JvmStatic
        fun newInstance(
            productListItem: ArrayList<ProductsListItem>?,
            locations: ArrayList<String>,
            maxValue: Int,
            minValue: Int,
            selectedFilterItem: SelectedFilterItem?
        ) =
            FilterFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PRODUCT_LIST_ITEM, productListItem)
                    putStringArrayList(LOCATIONS_ITEM, locations)
                    putInt(MAX_VALUE, maxValue)
                    putInt(MIN_VALUE, minValue)
                    putParcelable(SELECTED_FILTER_ITEM, selectedFilterItem)
                }
            }
    }
}