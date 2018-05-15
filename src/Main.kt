

fun main(args : Array<String>) {
    val converter = IntToEnglish()
    for (number in 1..9999) {
        println(converter.convert(number))
    }
}