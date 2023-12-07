package cool.ast;

public interface ASTVisitor<T> {
    // program
    T visit(ASTProgram p);

    // class
    T visit(ASTClass c);

    // feature
    T visit(ASTMethod m);
    T visit(ASTAttribute a);

    // formal
    T visit(ASTFormal f);

    // var
    T visit(ASTVar v);

    // case
    T visit(ASTCase c);

    // expr
    T visit(ASTDispatch d);
    T visit(ASTMethodCall mc);
    T visit(ASTIf i);
    T visit(ASTWhile w);
    T visit(ASTBlock b);
    T visit(ASTLet l);
    T visit(ASTSwitch s);
    T visit(ASTNew n);
    T visit(ASTIsVoid iv);
    T visit(ASTAssign a);
    T visit(ASTArithmetic a);
    T visit(ASTNegate n);
    T visit(ASTNot n);
    T visit(ASTParen p);
    T visit(ASTId i);
    T visit(ASTPrimitive p);
}
