import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.util.*
import org.antlr.v4.runtime.tree.ParseTreeWalker

object ShortToUnicodeString : ArrayInitBaseListener() {
    override fun enterInit(ctx: ArrayInitParser.InitContext) {
        print('"')
    }

    override fun exitInit(ctx: ArrayInitParser.InitContext) {
        print('"')
    }

    override fun enterValue(ctx: ArrayInitParser.ValueContext) {
        val value = Integer.valueOf(ctx.INT().text)
        System.out.printf("\\u%04x", value)
    }
}

fun main() {
    val stdIn = Scanner(System.`in`)
    val message = stdIn.nextLine()
    val stream = ByteArrayInputStream(message.toByteArray(StandardCharsets.UTF_8))
    val lexer = ArrayInitLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8))
    val tokens = CommonTokenStream(lexer)
    val parser = ArrayInitParser(tokens)
    val tree = parser.init()
    val walker = ParseTreeWalker()
    walker.walk(ShortToUnicodeString, tree)
    println()
}