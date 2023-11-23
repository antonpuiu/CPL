import org.antlr.v4.runtime.Token;
import java.util.*;

// Rădăcina ierarhiei de clase reprezentând nodurile arborelui de sintaxă
// abstractă (AST). Singura metodă permite primirea unui visitor.
public abstract class ASTNode {
    // Reținem un token descriptiv, pentru a putea afișa ulterior
    // informații legate de linia și coloana eventualelor erori semantice.
    Token token;

    ASTNode(Token token) {
        this.token = token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

// Orice expresie.
abstract class Expression extends ASTNode {
    Expression(Token token) {
        super(token);
    }
}

abstract class Definition extends ASTNode {
    Definition(Token token) {
        super(token);
    }
}

class Prog extends ASTNode {
    List<Definition> defs;
    List<Expression> exprs;

    Prog(List<Definition> defs, List<Expression> exprs, Token token) {
        super(token);

        this.defs = defs;
        this.exprs = exprs;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Formal extends ASTNode {
    Token type;
    Token name;

    Formal(Token type, Token name, Token token) {
        super(token);

        this.type = type;
        this.name = name;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// TODO 1: Definiți restul claselor de care ați avea nevoie. Asigurați-vă
// că moșteniți mereu nodul de bază ASTNode

class VarDef extends Definition {
    Token type;
    Token name;
    Expression init;

    VarDef(Token type, Token name, Expression init, Token token) {
        super(token);

        this.type = type;
        this.name = name;
        this.init = init;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class FuncDef extends Definition {
    Token type;
    Token name;
    List<Formal> formals;
    Expression body;

    FuncDef(Token type, Token name, List<Formal> formals, Expression body, Token token) {
        super(token);

        this.type = type;
        this.name = name;
        this.formals = formals;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Apel functie
class FuncCall extends Expression {
    Token name;
    List<Expression> args;

    FuncCall(Token name, List<Expression> args, Token token) {
        super(token);

        this.name = name;
        this.args = args;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Minus
class UnaryMinus extends Expression {
    Expression expr;

    UnaryMinus(Expression expr, Token start) {
        super(start);

        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Operation extends Expression {
    Expression left;
    Token op;
    Expression right;

    Operation(Expression left, Token op, Expression right, Token token) {
        super(token);

        this.left = left;
        this.op = op;
        this.right = right;
    }
}

class MultDiv extends Operation {
    MultDiv(Expression left, Token op, Expression right, Token token) {
        super(left, op, right, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class PlusMinus extends Operation {
    PlusMinus(Expression left, Token op, Expression right, Token token) {
        super(left, op, right, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Relational extends Operation {
    Relational(Expression left, Token op, Expression right, Token token) {
        super(left, op, right, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Construcția if.
class If extends Expression {
    // Sunt necesare trei câmpuri pentru cele trei componente ale expresiei.
    Expression cond;
    Expression thenBranch;
    Expression elseBranch;

    If(Expression cond,
            Expression thenBranch,
            Expression elseBranch,
            Token start) {
        super(start);
        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Assign extends Expression {
    Token name;
    Expression expr;

    Assign(Token name, Expression expr, Token token) {
        super(token);

        this.name = name;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Paren extends Expression {
    Expression expr;

    Paren(Expression expr, Token token) {
        super(token);

        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Identificatori
class Id extends Expression {
    Id(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Literali întregi
class Int extends Expression {
    Int(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Literali reali
class Float extends Expression {
    Float(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Literali booleeni
class Bool extends Expression {
    Bool(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
