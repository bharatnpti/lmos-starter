//import org.eclipse.lmos.starter.iaplatformcli.LmosAgentGeneratorService
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//
//class ArgumentParserTest {
//
//    @Test
//    fun `should parse single named argument with single value`() {
//        val args = arrayOf("--name", "Alice")
//        val expected = mapOf("--name" to listOf("Alice"))
//        assertEquals(expected, parseNamedArgumentsWithArray(args))
//    }
//
//    private fun parseNamedArgumentsWithArray(args: Array<String>): Any? {
//        val parseNamedArgumentsWithArray2 = LmosAgentGeneratorService().parseNamedArguments(args)
//        return parseNamedArgumentsWithArray2
//    }
//
//    @Test
//    fun `should parse single named argument with multiple values`() {
//        val args = arrayOf("--name", "Alice", "Bob")
//        val expected = mapOf("--name" to listOf("Alice", "Bob"))
//        assertEquals(expected, parseNamedArgumentsWithArray(args))
//    }
//
//    @Test
//    fun `should parse multiple named arguments with values`() {
//        val args = arrayOf("--name", "Alice", "--age", "25")
//        val expected = mapOf(
//            "--name" to listOf("Alice"),
//            "--age" to listOf("25")
//        )
//        assertEquals(expected, parseNamedArgumentsWithArray(args))
//    }
//
//    @Test
//    fun `should parse multiple named arguments with multiple values`() {
//        val args = arrayOf("--name", "Alice", "Bob", "--age", "25", "30")
//        val expected = mapOf(
//            "--name" to listOf("Alice", "Bob"),
//            "--age" to listOf("25", "30")
//        )
//        assertEquals(expected, parseNamedArgumentsWithArray(args))
//    }
//
//    @Test
//    fun `should handle arguments with no values`() {
//        val args = arrayOf("--flag")
//        val expected = mapOf("--flag" to emptyList<String>())
//        assertEquals(expected, parseNamedArgumentsWithArray(args))
//    }
//
//    @Test
//    fun `should handle mixed arguments correctly`() {
//        val args = arrayOf("--name", "Alice", "--flag", "--age", "25")
//        val expected = mapOf(
//            "--name" to listOf("Alice"),
//            "--flag" to emptyList(),
//            "--age" to listOf("25")
//        )
//        assertEquals(expected, parseNamedArgumentsWithArray(args))
//    }
//
//    @Test
//    fun `should return empty map when no arguments are given`() {
//        val args = emptyArray<String>()
//        val expected = emptyMap<String, List<String>>()
//        assertEquals(expected, parseNamedArgumentsWithArray(args))
//    }
//}
