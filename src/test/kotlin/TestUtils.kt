import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

fun String.toCharStream(): CharStream {
    val stream = ByteArrayInputStream(this.trimMargin().toByteArray())
    return CharStreams.fromStream(stream, StandardCharsets.UTF_8)
}