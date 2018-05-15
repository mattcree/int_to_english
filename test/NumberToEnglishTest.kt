import org.junit.Test
import kotlin.test.assertEquals

class NumberToEnglishTest {

    companion object {
        val converter = IntToEnglish();
    }

    // Testing Main API

    @Test
    fun testConvertZeroShouldReturnZero() {
        val expected = "zero"
        val actual = converter.convert(0);
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertOneShouldReturnOne() {
        val expected = "one"
        val actual = converter.convert(1);
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertLessThanTwentyShouldReturnNineteen() {
        val expected = "nineteen"
        val actual = converter.convert(19);
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertOverTwentyShouldReturnTwentyOne() {
        val expected = "twenty one"
        val actual = converter.convert(21);
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertLessThanOneHundredShouldReturnNinetyNine() {
        val expected = "ninety nine"
        val actual = converter.convert(99)
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertOneHundredShouldReturnOneHundred() {
        val expected = "one hundred"
        val actual = converter.convert(100)
        println(actual.trim())
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertOverOneHundredShouldReturnOneHundredAndOne() {
        val expected = "one hundred and one"
        val actual = converter.convert(101)
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertLessThanOneThousandShouldReturnNineHundredAndNinetyNine() {
        val expected = "nine hundred and ninety nine"
        val actual = converter.convert(999)
        println(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertOneThousandShouldReturnOneThousand() {
        val expected = "one thousand"
        val actual = converter.convert(1000)
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertOverOneThousandShouldReturnOneThousandAndOne() {
        val expected = "one thousand, and one"
        val actual = converter.convert(1001)
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertOneThousandOneHundredShouldReturnOneThousandOneHundred() {
        val expected = "one thousand, one hundred"
        val actual = converter.convert(1100)
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertOverOneThousandOneHundredShouldReturnOneThousandOneHundredAndTen() {
        val expected = "one thousand, one hundred and ten"
        val actual = converter.convert(1110)
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertOverOneThousandOneHundredShouldReturnOneThousandOneHundredAndEleven() {
        val expected = "one thousand, one hundred and eleven"
        val actual = converter.convert(1111)
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertUnderOneMillionShouldReturnNineHundredAndNinetyNineThousandNineHundredAndNinetyNine() {
        val expected = "nine hundred and ninety nine thousand, nine hundred and ninety nine"
        val actual = converter.convert(999999)
        assertEquals(expected, actual)
    }


    @Test
    fun testConvertWhenNumberIsMaxSize() {
        val expected = "two billion, one hundred and forty seven million, four hundred and eighty three thousand, six hundred and forty seven"
        val actual = converter.convert(2147483647)
        assertEquals(expected, actual)
    }

    @Test
    fun testConvertWhenNumberContainsManyZeros() {
        val expected = "fourteen million, three thousand, and forty three"
        val actual = converter.convert(14003043)
        assertEquals(expected, actual)
    }

    //Testing Internal Functions
    //Int to Clusters Tests
    @Test
    fun testGetClustersWithSixDigitNumberShouldReturnListOfPairs() {
        val first = Pair(9, 99)
        val second = Pair(8, 88)
        val expected = listOf(first, second)
        val actual = converter.getClusters(999888)
        assertEquals(expected, actual)
    }

    @Test
    fun testGetClustersWhenZeroShouldReturnZeroPair() {
        val first = Pair(0, 0)
        val expected = listOf(first)
        val actual = converter.getClusters(0)
        assertEquals(expected, actual)
    }

    @Test
    fun testIntToClustersShouldReturnNumberAsClusters() {
        val expected = listOf(99, 888)
        val actual = converter.intToClusters(99888)
        assertEquals(expected, actual)
    }

    //Testing 'to String' logic
    @Test
    fun testOneDigitNumberToStringShouldReturnNine() {
        val expected = "nine"
        val actual = converter.twoDigitNumberToString(9)
        assertEquals(expected, actual)
    }

    @Test
    fun testTwoDigitNumbertoStringWhenLessThanTwentyShouldReturnNineteen() {
        val expected = "nineteen"
        val actual = converter.twoDigitNumberToString(19)
        assertEquals(expected, actual)
    }

    @Test
    fun testTwoDigitNumbertoStringWhenGreaterThanTwentyShouldFortyThree() {
        val expected = "forty three"
        val actual = converter.twoDigitNumberToString(43)
        assertEquals(expected, actual)
    }

    @Test
    fun testFormattedStringReturnsStringWithCommas() {
        val expected = "1,234,567"
        val actual = converter.formattedIntString(1234567)
        assertEquals(expected, actual)
    }

    //Processing the Numbers
    @Test
    fun testProcessFirstNumberWhenZeroShouldReturnEmptyString() {
        val expected = ""
        val actual = converter.processFirstNumber(0)
        assertEquals(expected, actual)
    }

    @Test
    fun testProcessSecondNumberWhenZeroShouldReturnEmptyString() {
        val expected = ""
        val actual = converter.processSecondNumber(0)
        assertEquals(expected, actual)
    }

    @Test
    fun testProcessFirstNumberWhenGreaterThanZeroShouldReturnNumber() {
        val expected = "six"
        val actual = converter.processFirstNumber(6)
        assertEquals(expected, actual)
    }

    @Test
    fun testProcessSecondNumberWhenGreaterThanZeroShouldReturnNumber() {
        val expected = "eighty eight"
        val actual = converter.processSecondNumber(88)
        assertEquals(expected, actual)
    }
}

