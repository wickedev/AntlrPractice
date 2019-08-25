import java.lang.StringBuilder

class ExtractInterfaceListener(
    private val parser: JavaParser
) : JavaBaseListener() {

    private val sb: StringBuilder = StringBuilder()

    override fun enterImportDeclaration(ctx: JavaParser.ImportDeclarationContext) {
        sb.append("${parser.tokenStream.getText(ctx)}\n")
    }

    override fun exitImportDeclaration(ctx: JavaParser.ImportDeclarationContext?) {
        sb.append("\n\n")
    }

    override fun enterClassDeclaration(ctx: JavaParser.ClassDeclarationContext) {
        sb.append("interface I${ctx.Identifier()} {\n")
    }

    override fun exitClassDeclaration(ctx: JavaParser.ClassDeclarationContext?) {
        sb.append("}\n")
    }

    override fun enterMethodDeclaration(ctx: JavaParser.MethodDeclarationContext) {
        val tokens = parser.tokenStream
        val type = if (ctx.type() != null) tokens.getText(ctx.type()) else "void"

        val args = tokens.getText(ctx.formalParameters())
        sb.append("    $type ${ctx.Identifier()}$args;\n")
    }

    fun getInterfaceString(): String = sb.toString()
}