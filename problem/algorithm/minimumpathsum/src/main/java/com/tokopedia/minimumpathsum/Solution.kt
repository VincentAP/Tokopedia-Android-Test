package com.tokopedia.minimumpathsum

object Solution {
    fun minimumPathSum(matrix: Array<IntArray>): Int {
        // TODO, find a path from top left to bottom right which minimizes the sum of all numbers along its path, and return the sum
        // below is stub
        val m = matrix[0].size // column
        val n = matrix.size // row
        val dp = Array(n) { IntArray(m) }

        // O(n.m)
        for (i in 0 until n) {
            for (j in 0 until m) {
                if (i == 0 && j == 0) {
                    dp[i][j] = matrix[i][j]
                } else if (i == 0) {
                    // Sum with the left value
                    dp[i][j] = matrix[i][j] + dp[i][j - 1]
                } else if (j == 0) {
                    // Sum with value above
                    dp[i][j] = matrix[i][j] + dp[i - 1][j]
                } else {
                    // Sum with value above
                    val value1 = matrix[i][j] + dp[i - 1][j]

                    // Sum with the left value
                    val value2 = matrix[i][j] + dp[i][j - 1]

                    // Get The Minimum Value
                    val minValue = if (value1 < value2) value1 else value2

                    dp[i][j] = minValue
                }
            }
        }

        return dp[n - 1][m - 1]
    }
}
