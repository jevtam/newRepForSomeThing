package tasks.tasks

fun task3(args: Array<String>) {
    val input = args.joinToString(" ")
    val words = input.split("\\s+".toRegex()).filter { it.isNotBlank() }
    val uniqueSorted = words.sorted().distinct()
    uniqueSorted.forEach(::println)
}