package org.example

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

class Language : Grammar<Expr>() {
    val ws by regexToken("\\s+", ignore = true)
    
    val mul by literalToken("*")
    val div by literalToken("/")
    val plus by literalToken("+")
    val minus by literalToken("-")
    val lpar by literalToken("(")
    val rpar by literalToken(")")

    val digits by regexToken("\\d+")

    val num by digits use { text.toInt() }
    val term: Parser<Expr> by num use { Expr.Value(this) } or
            (skip(minus) and parser(::term) map { Expr.Neg(it) }) or
            (skip(lpar) and parser(::rootParser) and skip(rpar))
    val mulExpr by leftAssociative(term, mul or div use { type }) { acc, op, t ->
        if (op == mul) Expr.Mul(acc, t) else Expr.Div(acc, t)
    }
    val plusExpr by leftAssociative(mulExpr, plus or minus use { type }) { acc, op, t ->
        if (op == plus) Expr.Add(acc, t) else Expr.Min(acc, t)
    }

    override val rootParser: Parser<Expr> by plusExpr
}