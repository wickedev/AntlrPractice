import com.winterbe.expekt.should
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.spekframework.spek2.Spek


class ShortToUnicodeString(private val sb: StringBuilder) : ArrayInitBaseListener() {
    override fun enterInit(ctx: ArrayInitParser.InitContext) {
        sb.append('"')
    }

    override fun exitInit(ctx: ArrayInitParser.InitContext) {
        sb.append('"')
    }

    override fun enterValue(ctx: ArrayInitParser.ValueContext) {
        val value = Integer.valueOf(ctx.INT().text)
        sb.append(String.format("\\u%04x", value))
    }
}


class TranslateTest : Spek({
    test("{1,2,3} should be translate") {
        val sb = StringBuilder()
        val input = "{1,2,3}".toCharStream()
        val lexer = ArrayInitLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ArrayInitParser(tokens)
        val tree = parser.init()
        val walker = ParseTreeWalker()
        walker.walk(ShortToUnicodeString(sb), tree)
        val result = sb.toString()
        result.should.be.equal("\"\\u0001\\u0002\\u0003\"")
    }
})
