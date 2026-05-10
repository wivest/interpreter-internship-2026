package org.example

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

class Language : Grammar<Statement>() {
    val ws by regexToken("\\s+", ignore = true)

    val mul by literalToken("*")
    val div by literalToken("/")
    val plus by literalToken("+")
    val minus by literalToken("-")
    val lpar by literalToken("(")
    val rpar by literalToken(")")
    val comma by literalToken(",")

    val trueToken by literalToken("true")
    val falseToken by literalToken("false")
    val lessEq by literalToken("<=")
    val greaterEq by literalToken(">=")
    val less by literalToken("<")
    val greater by literalToken(">")
    val equal by literalToken("==")
    val notEqual by literalToken("!=")

    val ifToken by literalToken("if")
    val thenToken by literalToken("then")
    val elseToken by literalToken("else")

    val whileToken by literalToken("while")
    val doToken by literalToken("do")

    val funToken by literalToken("fun")
    val lscope by literalToken("{")
    val rscope by literalToken("}")
    val ret by literalToken("return")

    val digits by regexToken("\\d+")

    val identifier by regexToken("\\w+")
    val assign by literalToken("=")

    val num by digits use { text.toInt() }
    val term: Parser<Expr> by num use { Expr.Value(this) } or
            (identifier and skip(lpar) and
                    separatedTerms(parser(::expr), comma, true) and
                    skip(rpar) map {
                Expr.Call(it.t1.text, it.t2)
            }) or
            (identifier use { Expr.Var(text) }) or
            (skip(minus) and parser(::term) map { Expr.Neg(it) }) or
            (skip(lpar) and parser(::plusExpr) and skip(rpar))
    val mulExpr by leftAssociative(term, mul or div use { type }) { acc, op, t ->
        if (op == mul) Expr.Mul(acc, t) else Expr.Div(acc, t)
    }
    val plusExpr by leftAssociative(mulExpr, plus or minus use { type }) { acc, op, t ->
        if (op == plus) Expr.Add(acc, t) else Expr.Min(acc, t)
    }
    val expr by plusExpr

    val assignment by identifier use { text } and skip(assign) and expr map { Statement.Assignment(it.t1, it.t2) }

    val cond by trueToken use { Cond.Value(true) } or
            (falseToken use { Cond.Value(false) }) or
            (expr and skip(less) and expr map { Cond.Less(it.t1, it.t2) }) or
            (expr and skip(greater) and expr map { Cond.Greater(it.t1, it.t2) }) or
            (expr and skip(lessEq) and expr map { Cond.LessEq(it.t1, it.t2) }) or
            (expr and skip(greaterEq) and expr map { Cond.GreaterEq(it.t1, it.t2) }) or
            (expr and skip(equal) and expr map { Cond.Eq(it.t1, it.t2) }) or
            (expr and skip(notEqual) and expr map { Cond.Neq(it.t1, it.t2) })

    val ifStatement by skip(ifToken) and cond and skip(thenToken) and
            separatedTerms(parser(::rootParser), comma) and skip(elseToken) and
            separatedTerms(parser(::rootParser), comma) map {
        Statement.If(it.t1, it.t2, it.t3)
    }

    val whileStatement by skip(whileToken) and cond and skip(doToken) and
            separatedTerms(parser(::rootParser), comma) map {
        Statement.While(it.t1, it.t2)
    }

    val funcDecl by skip(funToken) and identifier and skip(lpar) and
            separatedTerms(identifier use { text }, comma, true) and
            skip(rpar) and skip(lscope) and
            separatedTerms(parser(::rootParser), comma) and
            skip(rscope) map {
        Statement.Func(it.t1.text, it.t2, it.t3)
    }

    val retStmt by skip(ret) and expr map { Statement.Ret(it) }

    override val rootParser: Parser<Statement> by assignment or
            ifStatement or
            whileStatement or
            funcDecl or
            retStmt
}