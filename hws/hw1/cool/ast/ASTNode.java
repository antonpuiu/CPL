package cool.ast;

import org.antlr.v4.runtime.Token;
import java.util.*;

public abstract class ASTNode {
    Token token;

    ASTNode(Token token) {
        this.token = token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

class ASTProgram extends ASTNode {
    List<ASTClass> classes;

    ASTProgram(List<ASTClass> classes, Token token) {
        super(token);

        this.classes = classes;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTClass extends ASTNode {
    Token name;
    Token parent;
    List<ASTFeature> features;

    ASTClass(Token name, Token parent, List<ASTFeature> features, Token token) {
        super(token);

        this.name = name;
        this.parent = parent;
        this.features = features;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

abstract class ASTFeature extends ASTNode {
    ASTFeature(Token token) {
        super(token);
    }
}

class ASTMethod extends ASTFeature {
    Token name;
    List<ASTFormal> formals;
    Token returnType;
    ASTExpression content;

    ASTMethod(Token name, List<ASTFormal> formals, Token returnType, ASTExpression content, Token token) {
        super(token);

        this.name = name;
        this.formals = formals;
        this.returnType = returnType;
        this.content = content;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTAttribute extends ASTFeature {
    ASTVar var;

    ASTAttribute(ASTVar var, Token token) {
        super(token);

        this.var = var;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTFormal extends ASTNode {
    Token name;
    Token type;

    ASTFormal(Token name, Token type, Token token) {
        super(token);

        this.name = name;
        this.type = type;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTVar extends ASTNode {
    Token name;
    Token type;
    ASTExpression value;

    ASTVar(Token name, Token type, ASTExpression value, Token token) {
        super(token);

        this.name = name;
        this.type = type;
        this.value = value;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTCase extends ASTNode {
    Token name;
    Token type;
    ASTExpression value;

    ASTCase(Token name, Token type, ASTExpression value, Token token) {
        super(token);

        this.name = name;
        this.type = type;
        this.value = value;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// expr
abstract class ASTExpression extends ASTNode {
    ASTExpression(Token token) {
        super(token);
    }
}

class ASTAssign extends ASTExpression {
    Token name;
    ASTExpression value;

    ASTAssign(Token name, ASTExpression value, Token token) {
        super(token);

        this.name = name;
        this.value = value;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTDispatch extends ASTExpression {
    ASTExpression object;
    Token staticType;
    Token methodName;
    List<ASTExpression> args;

    ASTDispatch(ASTExpression object,
                Token staticType,
                Token methodName,
                List<ASTExpression> args,
                Token token) {
        super(token);

        this.object = object;
        this.staticType = staticType;
        this.methodName = methodName;
        this.args = args;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTMethodCall extends ASTExpression {
    Token methodName;
    List<ASTExpression> args;

    ASTMethodCall(Token methodName, List<ASTExpression> args, Token token) {
        super(token);

        this.methodName = methodName;
        this.args = args;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTIf extends ASTExpression {
    ASTExpression cond, thenBranch, elseBranch;

    ASTIf(ASTExpression cond, ASTExpression thenBranch, ASTExpression elseBranch, Token token) {
        super(token);

        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTWhile extends ASTExpression {
    ASTExpression cond, body;

    ASTWhile(ASTExpression cond, ASTExpression body, Token token) {
        super(token);

        this.cond = cond;
        this.body = body;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}


class ASTBlock extends ASTExpression {
    List<ASTExpression> instructions;

    ASTBlock(List<ASTExpression> instructions, Token token) {
        super(token);

        this.instructions = instructions;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTLet extends ASTExpression {
    List<ASTVar> vars;
    ASTExpression context;

    ASTLet(List<ASTVar> vars, ASTExpression context, Token token) {
        super(token);

        this.vars = vars;
        this.context = context;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTSwitch extends ASTExpression {
    ASTExpression object;
    List<ASTCase> cases;

    ASTSwitch(ASTExpression object, List<ASTCase> cases, Token token) {
        super(token);

        this.object = object;
        this.cases = cases;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTNew extends ASTExpression {
    Token type;

    ASTNew(Token type, Token token) {
        super(token);

        this.type = type;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTIsVoid extends ASTExpression {
    ASTExpression value;

    ASTIsVoid(ASTExpression value, Token token) {
        super(token);

        this.value = value;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTArithmetic extends ASTExpression {
    ASTExpression left;
    Token op;
    ASTExpression right;

    ASTArithmetic(ASTExpression left, Token op, ASTExpression right, Token token) {
        super(token);

        this.left = left;
        this.op = op;
        this.right = right;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

abstract class ASTUnary extends ASTExpression {
    ASTExpression value;

    ASTUnary(ASTExpression value, Token token) {
        super(token);

        this.value = value;
    }
}

class ASTNegate extends ASTUnary {
    ASTNegate(ASTExpression value, Token token) {
        super(value, token);
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTNot extends ASTUnary {
    ASTNot(ASTExpression value, Token token) {
        super(value, token);
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTParen extends ASTExpression {
    ASTExpression value;

    ASTParen(ASTExpression value, Token token) {
        super(token);

        this.value = value;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTId extends ASTExpression {
    Token name;

    ASTId(Token name, Token start) {
        super(start);

        this.name = name;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ASTPrimitive extends ASTExpression {
    Token value;

    ASTPrimitive(Token value, Token token) {
        super(token);

        this.value = value;
    }

    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
