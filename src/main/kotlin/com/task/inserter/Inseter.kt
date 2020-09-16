package com.task.inserter

/**
 * Stores insertion parameters and perform brackets insertion.
 * @param isWrapped wrap the whole string into brackets or not
 * @param isCenter insert empty brackets pair in the middle of the string or not
 * @param bracketSequence cyclic sequence of brackets to insert
 */
class Inserter(
    private val isWrapped: Boolean,
    private val isCenter: Boolean,
    bracketSequence: BracketSequence
) {
    private val bracketSequence = bracketSequence.clone()

    /**
     * Inserts brackets between characters of the string
     */
    fun insert(string: String): String {
        bracketSequence.reset()
        var prefixBracket = ""
        if (isWrapped) {
            prefixBracket = bracketSequence.next()
        }
        val innerString = unwrappedInsert(string)
        return prefixBracket + innerString + BracketSequence.getPair(prefixBracket)
    }

    private fun unwrappedInsert(string: String, index: Int = 0): String {
        val pairIndex = string.length - index - 1
        if (index >= pairIndex || (pairIndex == index + 1 && !isCenter)) {
            return string.substring((index..pairIndex))
        }

        val brace = bracketSequence.next()
        val pairBrace = BracketSequence.getPair(brace)

        return (string[index] + brace
                + unwrappedInsert(string, index + 1)
                + pairBrace + string[pairIndex])
    }
}

/**
 * Cyclic sequence of brackets
 */
class BracketSequence private constructor(private val brackets: List<String>, private var index : Int = 0) {
    companion object {
        /**
         * List of allowed brackets
         */
        val allowedSymbols = listOf("{", "[", "(")

        private val pairs = mapOf("{" to "}", "[" to "]", "(" to ")")

        /**
         * Creates an instance from the string representation.
         * Only opening brackets separated with spaces are accepted
         */
        fun parseFromString(str: String): BracketSequence? {
            val braces = str.split(" ")
            if (braces.all { allowedSymbols.contains(it) }) return BracketSequence(braces)
            return null
        }

        /**
         * Returns closing bracket by opening
         * or an empty string if there is no known pair
         */
        fun getPair(bracket: String): String {
            return pairs[bracket] ?: ""
        }
    }

    /**
     * Get next bracket in cyclic order
     */
    fun next(): String {
        index %= brackets.size
        return brackets[index++]
    }

    /**
     * Get a copy of this BracketSequence
     */
    fun clone(): BracketSequence {
        return BracketSequence(brackets.toList(), index)
    }

    /**
     * Set current bracket pointer to beginning
     */
    fun reset() {
        index = 0
    }
}
