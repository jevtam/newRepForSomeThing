package tasks.tasks

fun task4(args: Array<String>) {
    val input = args.joinToString(" ")
    val words = input.split("\\s+".toRegex()).filter { it.isNotBlank() }
    val counts = words.groupingBy { it }.eachCount()
    for (word in words) {
        val count = counts[word] ?: 0
        println("$word $count")
    }
}