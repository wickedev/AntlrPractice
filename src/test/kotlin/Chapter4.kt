import com.winterbe.expekt.should
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

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

    describe("Demo") {
        it("interface parsing correctly") {
            val input = readTestResourceAsString("Demo.java")
            val expect = readTestResourceAsString("DemoExpect.java")

            val lexer = JavaLexer(input.toCharStream())
            val tokens = CommonTokenStream(lexer)
            val parser = JavaParser(tokens)
            val tree = parser.compilationUnit() // parse

            val walker = ParseTreeWalker()
            val extractor = ExtractInterfaceListener(parser)
            walker.walk(extractor, tree)
            val result = extractor.getInterfaceString().replaceTapToSpace4()
            result.should.be.contain(expect)
        }
    }

    describe("Insert Grammar") {
        val rows = "parrt\tTerence Parr\t101\ntombu\tTom Burns\t020\nbke\tKevin Edgar\t008\n"

        it("get col 1 data correctly") {
            val lexer = RowsLexer(rows.toCharStream())
            val tokens = CommonTokenStream(lexer)
            val parser = RowsParser(tokens, 1)
            parser.buildParseTree = false
            parser.file()
            parser.list.should.be.equal(listOf("parrt", "tombu", "bke"))
        }

        it("get col 2 data correctly") {
            val lexer = RowsLexer(rows.toCharStream())
            val tokens = CommonTokenStream(lexer)
            val parser = RowsParser(tokens, 2)
            parser.buildParseTree = false
            parser.file()
            parser.list.should.be.equal(listOf("Terence Parr", "Tom Burns", "Kevin Edgar"))
        }

        it("get col 3 data correctly") {
            val lexer = RowsLexer(rows.toCharStream())
            val tokens = CommonTokenStream(lexer)
            val parser = RowsParser(tokens, 3)
            parser.buildParseTree = false
            parser.file()
            parser.list.should.be.equal(listOf("101", "020", "008"))
        }
    }

    describe("Semantic Predicates") {
        it("data grouping sequentially") {
            val input = "2 9 10 3 1 2 3"
            val lexer = DataLexer(input.toCharStream())
            val tokens = CommonTokenStream(lexer)
            val parser = DataParser(tokens)

            val tree = parser.file()
            val result = tree.toStringTree(parser)
            println(result)
            result.should.be.equal("(file (group 2 (sequence 9 10)) (group 3 (sequence 1 2 3)))")
        }
    }

    describe("XML Lexer") {
        it("lexing tokens correctly") {
            val input = readTestResourceAsString("t.xml")
            val expect = readTestResourceAsString("t.expect")
            val lexer = XMLLexer(input.toCharStream())
            val tokens = CommonTokenStream(lexer)
            tokens.fill()
            val result = tokens.tokens.map { it.toString() }
                .reduce { acc, token -> "$acc\n" + token }
            result.should.be.contain(expect)
        }
    }

    describe("Rewrite Input Stream") {
        it("insert serialVersion field on class body") {
            val input = readTestResourceAsString("Demo.java")
            val expect = readTestResourceAsString("RewriteDemoExpect.java")

            val lexer = JavaLexer(input.toCharStream())
            val tokens = CommonTokenStream(lexer)
            val parser = JavaParser(tokens)
            val tree = parser.compilationUnit() // parse

            val walker = ParseTreeWalker()
            val extractor = InsertSerialIDListener(tokens)
            walker.walk(extractor, tree)
            val result = extractor.reWriter.text.replaceTapToSpace4()
            result.should.be.contain(expect)
        }
    }
})