package com.tokopedia.filter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.*
import com.tokopedia.filter.model.ProductsListItem
import kotlinx.android.synthetic.main.product_item.view.*

class RecyclerAdapter :
    ListAdapter<ProductsListItem, RecyclerAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(create(parent))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ProductsListItem, position: Int) {
            with(itemView) {
                val layoutParams =
                    cardProductItemWrapper.layoutParams as ViewGroup.MarginLayoutParams
                if (position == itemCount - 1) {
                    layoutParams.setMargins(
                        Util.dpToPx(20, context),
                        Util.dpToPx(10, context),
                        0,
                        Util.dpToPx(150, context)
                    )
                } else {
                    layoutParams.setMargins(
                        Util.dpToPx(20, context),
                        Util.dpToPx(10, context),
                        0,
                        Util.dpToPx(10, context)
                    )
                }
                cardProductItemWrapper.requestLayout()

                imageProduct.loadImageUrl(item.imageUrl)
                textProductName.text = item.name
                textPrice.text = item.priceInt.toRupiahCurrencyFormat()
                textLocation.text = item.shop.city
                textShopName.text = item.shop.name

                if (item.discountPercentage > 0) {
                    linearDiscountWrapper.visibility = View.VISIBLE
                    textDiscount.text =
                        context.getString(R.string.discount_amount, item.discountPercentage)
                    textDiscountPrice.text = item.slashedPriceInt.getSlashedPrice()
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductsListItem>() {
            override fun areItemsTheSame(
                oldItem: ProductsListItem,
                newItem: ProductsListItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ProductsListItem,
                newItem: ProductsListItem
            ): Boolean = oldItem == newItem
        }

        @JvmStatic
        fun create(parent: ViewGroup): View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
    }
}