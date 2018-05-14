import java.text.NumberFormat
import java.util.*

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

        return numberToWords(clusters)
    }

// ----------------------------------------------------------------------------
// Internal Data and Functions/Methods
// ----------------------------------------------------------------------------
// Static Tables for Number -> Word Lookups
// ----------------------------------------------------------------------------

    companion object {
        val digitsAndTeensTable = hashMapOf(
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
        val tensTable = hashMapOf(
                20 to "twenty",
                30 to "thirty",
                40 to "forty",
                50 to "fifty",
                60 to "sixty",
                70 to "seventy",
                80 to "eighty",
                90 to "ninety"
        )
        val magnitudeTable = hashMapOf(
                1 to "",
                2 to " thousand, ",
                3 to " million, ",
                4 to " billion, "
        )
    }


// ----------------------------------------------------------------------------
// Number Pairs/Clusters
// ----------------------------------------------------------------------------

    fun toNumberPair(num: Int): Pair<Int, Int> {
        val tens = num % 100
        val hundreds = (num - tens) / 100
        return Pair(hundreds, tens)
    }

// ----------------------------------------------------------------------------
// Number Pairs/Clusters to String Representation
// ----------------------------------------------------------------------------

    fun numberToWords(clusterList: List<Pair<Int, Int>>): String {
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

    fun processNumberPair(numPair: Pair<Int, Int>, mode: String): String {
        if(isEmptyNumPair(numPair))
            return ""
        when(mode == "trailing") {
            true -> return processNumberPairTrailing(numPair)
            false -> return processNumberPairStandard(numPair)
        }
    }

    fun processNumberPairStandard(numPair: Pair<Int, Int>): String {
        val first = numPair.first
        val second = numPair.second
        if(first == 0)
            return processSecondNumber(second)
        if(second == 0)
            return processFirstNumber(first) + " hundred "
        return concatPair(numPair)
    }

    fun processNumberPairTrailing(numPair: Pair<Int, Int>): String {
        val first = numPair.first
        val second = numPair.second
        if (first == 0)
            return "and " + processSecondNumber(second)
        if (second == 0)
            return processFirstNumber(first) + " hundred "
        return concatPair(numPair)
    }



// ----------------------------------------------------------------------------
// One Digit Numbers to String
// ----------------------------------------------------------------------------

    // Converting single digit number to a String
    fun processFirstNumber(num: Int): String {
        if(num > 0)
            return oneDigitNumberToString(num)
        return ""
    }

    fun oneDigitNumberToString(num: Int): String {
        return digitsAndTeensTable[num]!!
    }

// ----------------------------------------------------------------------------
// Two Digit Numbers to String
// ----------------------------------------------------------------------------

    // Converting (up to) a two digit number to a String
    fun processSecondNumber(num: Int): String {
        if(num > 0)
            return twoDigitNumberToString(num)
        return ""
    }

    fun twoDigitNumberToString(num: Int): String {
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
    fun intToNumberPairs(num: Int): List<Pair<Int, Int>> {
        return intToClusters(num).map {toNumberPair(it)}
    }

    // Splits the Number String into a list of Int clusters < 999 i.e. '1,234,567' -> [1, 234, 567]
    fun intToClusters(num: Int): List<Int> {
        return formattedIntString(num).split(',').map {Integer.parseInt(it)}
    }

    // Converts the Integer to a String with comma delimiters i.e. 1234567 -> '1,234,567'
    fun formattedIntString(num: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(num)
    }

// ----------------------------------------------------------------------------
// Helpers
// ----------------------------------------------------------------------------

    fun concatPair(numPair: Pair<Int, Int>): String {
        return processFirstNumber(numPair.first) + " hundred and " + processSecondNumber(numPair.second)
    }

    fun trim(str: String): String {
        return str.trim {it <= ' ' || it <= ','}
    }

    fun isEmptyNumPair(numPair: Pair<Int, Int>): Boolean {
        return if(numPair.first == 0 && numPair.second == 0) true else false
    }
}