package tasks.tasks

fun task2(args: Array<String>) {
    val input = args.joinToString(" ")
    val words = input.split("\\s+".toRegex()).filter { it.isNotBlank() }
    val sorted = words.sorted()
    sorted.forEach(::println)
}
