package tasks.tasks

fun task6(args: Array<String>) {
    val input = if (args.isNotEmpty()) {
        args.joinToString(" ")
    } else {
        generateSequence(::readLine).joinToString(" ")
    }

    val words = input.split("\\s+".toRegex())
        .filter { it.isNotBlank() }
        .map { it.lowercase() }

    val counts = words.groupingBy { it }.eachCount()

    counts.entries
        .sortedWith(
            compareByDescending<Map.Entry<String, Int>> { it.value }
                .thenBy { it.key }
        )
        .forEach { (w, c) -> println("$w $c") }
}