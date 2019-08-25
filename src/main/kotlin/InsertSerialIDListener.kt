import org.antlr.v4.runtime.TokenStream
import org.antlr.v4.runtime.TokenStreamRewriter

class InsertSerialIDListener(tokens: TokenStream) : JavaBaseListener() {
    val reWriter: TokenStreamRewriter = TokenStreamRewriter(tokens)

    override fun enterClassBody(ctx: JavaParser.ClassBodyContext) {
        val field = "\n\tpublic static final long serialVersionUID = 1L;\n"
        reWriter.insertAfter(ctx.start, field)
    }
}