import java.util.HashMap

class EvalVisitor(private val results: MutableList<Int>) : LabeledExprBaseVisitor<Int>() {
    /** "memory" for our calculator; variable/value pairs go here  */
    private var memory: MutableMap<String, Int> = HashMap()

    /** ID '=' expr NEWLINE  */
    override fun visitAssign(ctx: LabeledExprParser.AssignContext): Int? {
        val id = ctx.ID().text  // id is left-hand side of '='
        val value = visit(ctx.expr())   // compute value of expression on right
        memory[id] = value           // store it in our memory
        return value
    }

    /** expr NEWLINE  */
    override fun visitPrintExpr(ctx: LabeledExprParser.PrintExprContext): Int? {
        val value = visit(ctx.expr()) // evaluate the expr child
        results.add(value)         // print the result
        return 0                          // return dummy value
    }

    /** INT  */
    override fun visitInt(ctx: LabeledExprParser.IntContext): Int? {
        return Integer.valueOf(ctx.INT().text)
    }

    /** ID  */
    override fun visitId(ctx: LabeledExprParser.IdContext): Int? {
        val id = ctx.ID().text
        return if (memory.containsKey(id)) memory[id] else 0
    }

    /** expr op=('*'|'/') expr  */
    override fun visitMulDiv(ctx: LabeledExprParser.MulDivContext): Int? {
        val left = visit(ctx.expr(0))  // get value of left subexpression
        val right = visit(ctx.expr(1)) // get value of right subexpression
        return if (ctx.op.type == LabeledExprParser.MUL) left * right else left / right
        // must be DIV
    }

    /** expr op=('+'|'-') expr  */
    override fun visitAddSub(ctx: LabeledExprParser.AddSubContext): Int? {
        val left = visit(ctx.expr(0))  // get value of left subexpression
        val right = visit(ctx.expr(1)) // get value of right subexpression
        return if (ctx.op.type == LabeledExprParser.ADD) left + right else left - right
        // must be SUB
    }

    /** '(' expr ')'  */
    override fun visitParens(ctx: LabeledExprParser.ParensContext): Int? {
        return visit(ctx.expr()) // return child expr's value
    }
}
