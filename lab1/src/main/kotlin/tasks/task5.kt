package tasks.tasks

fun task5(args: Array<String>) {
    val input = args.joinToString(" ")
    val words = input.split("\\s+".toRegex()).filter { it.isNotBlank() }
    val counts = words.groupingBy { it }.eachCount()
    counts.entries
        .sortedWith(
            compareByDescending<Map.Entry<String, Int>> { it.value }
                .thenBy { it.key }
        )
        .forEach { (w, c) -> println("$w $c") }
}