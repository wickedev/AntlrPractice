import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import java.nio.charset.StandardCharsets
import java.io.*

class TestUtils

fun String.toCharStream(): CharStream {
    val stream = ByteArrayInputStream(this.toByteArray())
    return CharStreams.fromStream(stream, StandardCharsets.UTF_8)
}

fun String.replaceSpace4(): String {
    return replace("\t", "    ")
}

fun readTestResourceAsString(filePath: String): String {
    return TestUtils::class.java.getResource(filePath).readText()
}