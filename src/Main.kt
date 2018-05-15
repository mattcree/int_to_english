
//The main loop
fun main(args : Array<String>) {
    // Get an instance of the converter
    val converter = IntToEnglish()

    // Print the numbers
    for (number in 1..9999) {
        println(converter.convert(number))
    }
}