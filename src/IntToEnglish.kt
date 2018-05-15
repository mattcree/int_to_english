import java.text.NumberFormat
import java.util.*
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf

class IntToEnglish : IntToWords {

// ----------------------------------------------------------------------------
// Main API
// ----------------------------------------------------------------------------

    /**
     * A method for converting an integer a written English representation
     * of that integer.
     *
     * For example:
     *     14,003,043 -> fourteen million three thousand and fourty three
     *
     * @param num The number to convert
     * @return The number as English words
     */
    override fun convert(num: Int): String {
        // If it's zero, no work needed
        if(num == 0)
            return "zero"

        // Split the number into a list of pairs
        val clusters = intToNumberPairs(num)

        // This step ensures numbers less than 1000 are processed without special rules
        // required for numbers with lots of zeros and whether to add an 'and' when
        // there is no *first* in our number pair
        // i.e. 14011 -> 'fourteen thousand and eleven', not 'fourteen thousand eleven'
        if(clusters.size == 1)
            return trim(processNumberPair(clusters.first(), "standard"))

        return processClusters(clusters)
    }

// ----------------------------------------------------------------------------
// Internal Data and Functions/Methods
// ----------------------------------------------------------------------------
// Static Tables for Number -> Word Lookups
// ----------------------------------------------------------------------------

    companion object {
        // The tables used to do the mapping between integers
        // and their English counterparts
        private val digitsAndTeensTable = hashMapOf(
                0 to "",
                1 to "one",
                2 to "two",
                3 to "three",
                4 to "four",
                5 to "five",
                6 to "six",
                7 to "seven",
                8 to "eight",
                9 to "nine",
                10 to "ten",
                11 to "eleven",
                12 to "twelve",
                13 to "thirteen",
                14 to "fourteen",
                15 to "fifteen",
                16 to "sixteen",
                17 to "seventeen",
                18 to "eighteen",
                19 to "nineteen"
        )
        private val tensTable = hashMapOf(
                20 to "twenty",
                30 to "thirty",
                40 to "forty",
                50 to "fifty",
                60 to "sixty",
                70 to "seventy",
                80 to "eighty",
                90 to "ninety"
        )
        // TODO: find a better way to do this rather than hardcoding commas and formatting
        // details
        private val magnitudeTable = hashMapOf(
                1 to "",
                2 to " thousand, ",
                3 to " million, ",
                4 to " billion, "
        )
    }


// ----------------------------------------------------------------------------
// Number Pairs/Clusters
// ----------------------------------------------------------------------------

    private fun toNumberPair(num: Int): Pair<Int, Int> {
        val tens = num % 100
        val hundreds = (num - tens) / 100
        return Pair(hundreds, tens)
    }

// ----------------------------------------------------------------------------
// Number Pairs/Clusters to String Representation
// ----------------------------------------------------------------------------

    private fun processClusters(clusterList: List<Pair<Int, Int>>): String {
        // The String Builder which will be the return value
        val stringBuilder = StringBuilder(150)
        var magnitudes = clusterList.size
        clusterList.forEach { pair ->
            if(magnitudes == 1) {
                stringBuilder.append(processNumberPair(pair, "trailing"))
            } else {
                stringBuilder.append(processNumberPair(pair, "standard"))
            }
            stringBuilder.append(magnitudeTable[magnitudes]!!)
            magnitudes--
        }
        return trim(stringBuilder.toString())
    }

    private fun processNumberPair(numPair: Pair<Int, Int>, mode: String): String {
        if(isEmptyNumPair(numPair))
            return ""
        when(mode == "trailing") {
            true -> return processNumberPairTrailing(numPair)
            false -> return processNumberPairStandard(numPair)
        }
    }

    private fun processNumberPairStandard(numPair: Pair<Int, Int>): String {
        if(numPair.first == 0)
            return secondNumber(numPair)
        if(numPair.second == 0)
            return firstNumber(numPair)
        return bothNumbers(numPair)
    }

    private fun processNumberPairTrailing(numPair: Pair<Int, Int>): String {
        if (numPair.first == 0)
            return secondNumberTrailing(numPair)
        if (numPair.second == 0)
            return firstNumber(numPair)
        return bothNumbers(numPair)
    }

// ----------------------------------------------------------------------------
// Processing Numbers
// ----------------------------------------------------------------------------

    // Converting single digit number to a String
    private fun processFirstNumber(num: Int): String {
        if(num > 0)
            return oneDigitNumberToString(num)
        return ""
    }

    // Converting (up to) a two digit number to a String
    private fun processSecondNumber(num: Int): String {
        if(num > 0)
            return twoDigitNumberToString(num)
        return ""
    }

// ----------------------------------------------------------------------------
// Formatting of Numbers with Special Cases
// ----------------------------------------------------------------------------

    private fun firstNumber(numPair: Pair<Int, Int>): String {
        return processFirstNumber(numPair.first) + " hundred "
    }

    private fun secondNumber(numPair: Pair<Int, Int>): String {
        return processSecondNumber(numPair.second)
    }

    private fun secondNumberTrailing(numPair: Pair<Int, Int>): String {
        return "and " + processSecondNumber(numPair.second)
    }

    private fun bothNumbers(numPair: Pair<Int, Int>): String {
        return processFirstNumber(numPair.first) + " hundred and " + processSecondNumber(numPair.second)
    }

// ----------------------------------------------------------------------------
// Two Digit Numbers to String
// ----------------------------------------------------------------------------

    private fun oneDigitNumberToString(num: Int): String {
        return digitsAndTeensTable[num]!!
    }

    private fun twoDigitNumberToString(num: Int): String {
        if(num < 20) return digitsAndTeensTable[num]!!
        val leastSignificantDigit = num % 10
        val mostSignificantDigit = num - leastSignificantDigit
        return tensTable[mostSignificantDigit]!! + " " + digitsAndTeensTable[leastSignificantDigit]!!
    }

// ----------------------------------------------------------------------------
// Int -> Cluster List
// ----------------------------------------------------------------------------

    // Produces a list of Pairs of the form Pair(<Hundred Value>, <Rest>) i.e. 199 -> (1, 99)
    // This data structure allows for easy conversion to more natural English patterns
    // for example, (1, 99) -> one hundred -- and -- ninety nine
    private fun intToNumberPairs(num: Int): List<Pair<Int, Int>> {
        return intToClusters(num).map {toNumberPair(it)}
    }

    // Splits the Number String into a list of Int clusters < 999 i.e. '1,234,567' -> [1, 234, 567]
    private fun intToClusters(num: Int): List<Int> {
        return formattedIntString(num).split(',').map {Integer.parseInt(it)}
    }

    // Converts the Integer to a String with comma delimiters i.e. 1234567 -> '1,234,567'
    private fun formattedIntString(num: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(num)
    }

// ----------------------------------------------------------------------------
// Helpers
// ----------------------------------------------------------------------------


    private fun trim(str: String): String {
        return str.trim {it <= ' ' || it <= ','}
    }

    private fun isEmptyNumPair(numPair: Pair<Int, Int>): Boolean {
        return if(numPair.first == 0 && numPair.second == 0) true else false
    }
}