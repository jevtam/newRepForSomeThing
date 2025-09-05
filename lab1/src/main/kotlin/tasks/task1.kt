package tasks

fun task1(args: Array<String>) {
    val input = args.joinToString(" ")
    val words = input.split("\\s+".toRegex()).filter { it.isNotBlank() }
    for (word in words) {
        println(word)
    }
}