import java.io.IOException;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


public class Test {

    public static void main(String[] args) throws IOException {
        var input = CharStreams.fromFileName("manual.txt");

        var lexer = new CPLangLexer(input);
        var tokenStream = new CommonTokenStream(lexer);

        /*
        tokenStream.fill();
        List<Token> tokens = tokenStream.getTokens();
        for (var token : tokens) {
            var text = token.getText();
            var type = CPLangLexer.VOCABULARY.getSymbolicName(token.getType());

            System.out.println(text + " : " + type);
        }
        */

        var parser = new CPLangParser(tokenStream);
        var tree = parser.prog();
        System.out.println(tree.toStringTree(parser));

        // Visitor-ul de mai jos parcurge arborele de derivare și construiește
        // un arbore de sintaxă abstractă (AST).
        var astConstructionVisitor = new CPLangParserBaseVisitor<ASTNode>() {
            // TODO 3: Completati cu alte metode pentru a trece din arborele
            // generat automat in reprezentarea AST-ului dorit
            @Override
            public ASTNode visitProg(CPLangParser.ProgContext ctx) {
                List<Definition> defs = new ArrayList<>();
                List<Expression> exprs = new ArrayList<>();

                for (var def : ctx.definition())
                    defs.add((Definition)visit(def));

                for (var expr : ctx.expr())
                    exprs.add((Expression)visit(expr));

                return new Prog(defs, exprs, ctx.start);
            }

            @Override
            public ASTNode visitVarDef(CPLangParser.VarDefContext ctx) {
                Expression init = null;

                if (ctx.init != null)
                    init = (Expression)visit(ctx.init);

                return new VarDef(ctx.type, ctx.name, init, ctx.start);
            }

            @Override
            public ASTNode visitFuncDef(CPLangParser.FuncDefContext ctx) {
                ArrayList<Formal> formals = new ArrayList<>();

                for (var formal : ctx.formals)
                    formals.add((Formal)visit(formal));

                return new FuncDef(ctx.type, ctx.name, formals, (Expression)visit(ctx.body), ctx.start);
            }

            @Override
            public ASTNode visitFormal(CPLangParser.FormalContext ctx) {
                return new Formal(ctx.type, ctx.name, ctx.start);
            }


            @Override
            public ASTNode visitCall(CPLangParser.CallContext ctx) {
                ArrayList<Expression> args = new ArrayList<>();

                for (var arg : ctx.args)
                    args.add((Expression)visit(arg));

                return new FuncCall(ctx.name, args, ctx.start);
            }

            @Override
            public ASTNode visitUnaryMinus(CPLangParser.UnaryMinusContext ctx) {
                return new UnaryMinus((Expression)visit(ctx.e), ctx.start);
            }

            @Override
            public ASTNode visitMultDiv(CPLangParser.MultDivContext ctx) {
                return new MultDiv((Expression)visit(ctx.left), (Token)ctx.op, (Expression)visit(ctx.right), ctx.start);
            }

            @Override
            public ASTNode visitPlusMinus(CPLangParser.PlusMinusContext ctx) {
                return new PlusMinus((Expression)visit(ctx.left), (Token)ctx.op, (Expression)visit(ctx.right), ctx.start);
            }

            @Override
            public ASTNode visitRelational(CPLangParser.RelationalContext ctx) {
                return new Relational((Expression)visit(ctx.left), (Token)ctx.op, (Expression)visit(ctx.right), ctx.start);
            }

            @Override
            public ASTNode visitIf(CPLangParser.IfContext ctx) {
                return new If((Expression)visit(ctx.cond),
                              (Expression)visit(ctx.thenBranch),
                              (Expression)visit(ctx.elseBranch),
                              ctx.start);
            }

            @Override
            public ASTNode visitAssign(CPLangParser.AssignContext ctx) {
                return new Assign(ctx.name, (Expression)visit(ctx.e), ctx.start);
            }

            @Override
            public ASTNode visitParen(CPLangParser.ParenContext ctx) {
                return new Paren((Expression)visit(ctx.e), ctx.start);
            }

            @Override
            public ASTNode visitId(CPLangParser.IdContext ctx) {
                return new Id(ctx.ID().getSymbol());
            }

            @Override
            public ASTNode visitInt(CPLangParser.IntContext ctx) {
                return new Int(ctx.INT().getSymbol());
            }

            @Override
            public ASTNode visitFloat(CPLangParser.FloatContext ctx) {
                return new Float(ctx.FLOAT().getSymbol());
            }

            @Override
            public ASTNode visitBool(CPLangParser.BoolContext ctx) {
                return new Bool(ctx.BOOL().getSymbol());
            }
        };

        // ast este AST-ul proaspăt construit pe baza arborelui de derivare.
        var ast = astConstructionVisitor.visit(tree);

        // Acest visitor parcurge AST-ul generat mai sus.
        // ATENȚIE! Avem de-a face cu două categorii de visitori:
        // (1) Cei pentru arborele de derivare, care extind
        //     CPLangParserBaseVisitor<T> și
        // (2) Cei pentru AST, care extind ASTVisitor<T>.
        // Aveți grijă să nu îi confundați!
        var printVisitor = new ASTVisitor<Void>() {
            // TODO 4: Afisati fiecare nod astfel incat nivelul pe care acesta
            // se afla in AST sa fie reprezentat de numarul de tab-uri.
            // Folositi functia de mai jos 'printIndent' si incrementati /
            // decrementati corespunzator numarul de tab-uri
            int indent = 0;

            @Override
            public Void visit(Prog prog) {
                for (var def : prog.defs)
                    def.accept(this);

                for (var expr : prog.exprs)
                    expr.accept(this);

                return null;
            }

            @Override
            public Void visit(Formal formal) {
                printIndent("FORMAL");

                indent++;
                printIndent(formal.type.getText());
                printIndent(formal.name.getText());
                indent--;

                return null;
            }

            @Override
            public Void visit(VarDef varDef) {
                printIndent("VAR DEF");

                indent++;
                printIndent(varDef.type.getText());
                printIndent(varDef.name.getText());

                if (varDef.init != null)
                    varDef.init.accept(this);

                indent--;

                return null;
            }

            @Override
            public Void visit(FuncDef funcDef) {
                printIndent("FUNC DEF");

                indent++;
                printIndent(funcDef.type.getText());
                printIndent(funcDef.name.getText());

                for (Formal formal : funcDef.formals)
                    formal.accept(this);

                funcDef.body.accept(this);
                indent--;

                return null;
            }

            @Override
            public Void visit(FuncCall call) {
                printIndent("CALL");
                indent++;
                printIndent(call.name.getText());

                for (Expression arg : call.args)
                    arg.accept(this);

                indent--;

                return null;
            }

            @Override
            public Void visit(UnaryMinus minus) {
                printIndent("UNARY MINUS");

                indent++;
                minus.expr.accept(this);
                indent--;

                return null;
            }

            @Override
            public Void visit(MultDiv multDiv) {
                printIndent("MULT/DIV");

                indent++;
                multDiv.left.accept(this);
                printIndent(multDiv.op.getText());
                multDiv.right.accept(this);
                indent--;

                return null;
            }

            @Override
            public Void visit(PlusMinus plusMinus) {
                printIndent("PLUS/MINUS");

                indent++;
                plusMinus.left.accept(this);
                printIndent(plusMinus.op.getText());
                plusMinus.right.accept(this);
                indent--;

                return null;
            }

            @Override
            public Void visit(Relational rel) {
                printIndent("RELATIONAL");

                indent++;
                rel.left.accept(this);
                printIndent(rel.op.getText());
                rel.right.accept(this);
                indent--;

                return null;
            }

            @Override
            public Void visit(If iff) {
                printIndent("IF");

                indent++;
                iff.cond.accept(this);
                iff.thenBranch.accept(this);
                iff.elseBranch.accept(this);
                indent--;

                return null;
            }

            @Override
            public Void visit(Assign assign) {
                printIndent("ASSIGN");

                indent++;
                printIndent(assign.name.getText());
                assign.expr.accept(this);
                indent--;

                return null;
            }

            @Override
            public Void visit(Paren paren) {
                printIndent("PAREN");

                indent++;
                paren.expr.accept(this);
                indent--;

                return null;
            }

            @Override
            public Void visit(Id id) {
                printIndent("ID " + id.token.getText());
                return null;
            }

            @Override
            public Void visit(Int intt) {
                printIndent("INT " + intt.token.getText());
                return null;
            }

            @Override
            public Void visit(Float flt) {
                printIndent("FLOAT" + flt.token.getText());
                return null;
            }

                @Override
            public Void visit(Bool bl) {
                printIndent("BOOL" + bl.token.getText());
                return null;
            }

            void printIndent(String str) {
                for (int i = 0; i < indent; i++)
                    System.out.print("\t");
                System.out.println(str);
            }
        };

        // TODO 5: Creati un program CPLang care sa cuprinda cat mai multe
        // constructii definite in laboratorul de astazi. Scrieti codul CPLang
        // intr-un fisier txt si modificati fisierul de input de la inceputul
        // metodei 'main'

        System.out.println("The AST is");
        ast.accept(printVisitor);
    }


}
