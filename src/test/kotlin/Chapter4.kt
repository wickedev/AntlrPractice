import com.winterbe.expekt.should
import org.antlr.v4.runtime.CommonTokenStream
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.StringBuilder

class Chapter4 : Spek({
    describe("ExprJoyRide") {
        it("should parse string tree") {
            val input = """
                193
                a = 5
                b = 6
                a+b*2
                (1+2)*3
                
            """.trimIndent()

            val lexer = ExprLexer(input.toCharStream())
            val tokens = CommonTokenStream(lexer)
            val parser = ExprParser(tokens)
            val tree = parser.prog()
            val result = tree.toStringTree(parser)
            result.should.be.equal("(prog (stat (expr 193) \\n) (stat a = (expr 5) \\n) (stat b = (expr 6) \\n) (stat (expr (expr a) + (expr (expr b) * (expr 2))) \\n) (stat (expr (expr ( (expr (expr 1) + (expr 2)) )) * (expr 3)) \\n))")
        }
    }

    describe("Calc") {
        it("should calculate correctly") {
            val input = """
                193
                a = 5
                b = 6
                a+b*2
                (1+2)*3
                
            """.trimIndent()
            val results = mutableListOf<Int>()
            val lexer = LabeledExprLexer(input.toCharStream())
            val tokens = CommonTokenStream(lexer)
            val parser = LabeledExprParser(tokens)
            val tree = parser.prog()
            val eval = EvalVisitor(results)
            eval.visit(tree)
            results.should.be.equal(listOf(193, 17, 9))
        }
    }
})