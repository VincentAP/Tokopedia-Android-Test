package com.tokopedia.oilreservoir

/**
 * Created by fwidjaja on 2019-09-24.
 */
object Solution {
    fun collectOil(height: IntArray): Int {
        // TODO, return the amount of oil blocks that could be collected
        // below is stub

        if (height.isEmpty()) return 0

        var result = 0
        var leftIdx = 0
        var rightIdx = height.size - 1

        var maxLeft = height[0]
        var maxRight = height[rightIdx]

        while (leftIdx <= rightIdx) {
            var tempResult = 0
            var subs = 0
            if (maxLeft <= maxRight) {
                // Calculate total collected oil
                val currentHeight = height[leftIdx]
                subs = maxLeft - currentHeight

                // Check if height[leftIdx] > maxLeft
                if (currentHeight > maxLeft) maxLeft = currentHeight

                // Update leftIdx
                leftIdx++
            } else {
                // Calculate total collected oil
                val currentHeight = height[rightIdx]
                subs = maxRight - currentHeight

                // Check if height[i] > maxRight
                if (currentHeight > maxRight) maxRight = currentHeight

                // Update rightIdx
                rightIdx--
            }
            tempResult = if (subs < 0) 0 else subs
            result += tempResult
        }
        return result
    }
}
