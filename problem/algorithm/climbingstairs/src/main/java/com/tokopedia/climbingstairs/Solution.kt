package com.tokopedia.climbingstairs

object Solution {
    fun climbStairs(n: Int): Long {
        // TODO, return in how many distinct ways can you climb to the top. Each time you can either climb 1 or 2 steps.
        // 1 <= n < 90
        if (n < 4) return n.toLong()

        var step = 2L
        var result = 3L

        (4..n).forEach { _ ->
            val temp = result
            result += step
            step = temp
        }
        return result
    }
}
