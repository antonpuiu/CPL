package cool.ast;

public class ASTPrinter implements ASTVisitor<Void> {
    int indent = 0;

    // program
    @Override
    public Void visit(ASTProgram p) {
        printIndent("program");

        indent++;
        for (var c : p.classes)
            c.accept(this);
        indent--;

        return null;
    }

    // class
    @Override
    public Void visit(ASTClass c) {
        printIndent("class");

        indent++;

        printIndent(c.name.getText());
        if (c.parent != null)
            printIndent(c.parent.getText());
        for (var feature : c.features)
            feature.accept(this);

        indent--;

        return null;
    }

    // feature
    @Override
    public Void visit(ASTMethod m) {
        printIndent("method");

        indent++;
        printIndent(m.name.getText());
        for (var formal : m.formals)
            formal.accept(this);
        printIndent(m.returnType.getText());
        m.content.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTAttribute a) {
        printIndent("attribute");

        indent++;
        a.var.accept(this);
        indent--;

        return null;
    }

    // formal
    @Override
    public Void visit(ASTFormal f) {
        printIndent("formal");

        indent++;
        printIndent(f.name.getText());
        printIndent(f.type.getText());
        indent--;

        return null;
    }

    // var
    @Override
    public Void visit(ASTVar v) {
        printIndent(v.name.getText());
        printIndent(v.type.getText());
        if (v.value != null)
            v.value.accept(this);

        return null;
    }

    // case
    @Override
    public Void visit(ASTCase c) {
        printIndent("case branch");

        indent++;
        printIndent(c.name.getText());
        printIndent(c.type.getText());
        c.value.accept(this);
        indent--;

        return null;
    }

    // expr
    @Override
    public Void visit(ASTAssign a) {
        printIndent("<-");

        indent++;
        printIndent(a.name.getText());
        a.value.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTDispatch d) {
        printIndent(".");

        indent++;
        d.object.accept(this);
        if (d.staticType != null)
            printIndent(d.staticType.getText());
        printIndent(d.methodName.getText());
        for (var arg : d.args)
            arg.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTMethodCall mc) {
        printIndent("implicit dispatch");

        indent++;
        printIndent(mc.methodName.getText());
        for (var arg : mc.args)
            arg.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTIf i) {
        printIndent("if");

        indent++;
        i.cond.accept(this);
        i.thenBranch.accept(this);
        i.elseBranch.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTWhile w) {
        printIndent("while");

        indent++;
        w.cond.accept(this);
        w.body.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTBlock b) {
        printIndent("block");

        indent++;
        for (var instruction : b.instructions)
            instruction.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(ASTLet l) {
        printIndent("let");

        indent++;
        for (var var : l.vars) {
            printIndent("local");

            indent++;
            var.accept(this);
            indent--;
        }
        l.context.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTSwitch s) {
        printIndent("case");

        indent++;
        s.object.accept(this);
        for (var c : s.cases)
            c.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTNew n) {
        printIndent("new");

        indent++;
        printIndent(n.type.getText());
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTIsVoid iv) {
        printIndent("isvoid");

        indent++;
        iv.value.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTArithmetic a) {
        printIndent(a.op.getText());

        indent++;
        a.left.accept(this);
        a.right.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTNegate n) {
        printIndent("~");

        indent++;
        n.value.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTNot n) {
        printIndent("not");

        indent++;
        n.value.accept(this);
        indent--;

        return null;
    }

    @Override
    public Void visit(ASTParen p) {
        p.value.accept(this);
        return null;
    }

    @Override
    public Void visit(ASTId i) {
        printIndent(i.name.getText());
        return null;
    }

    @Override
    public Void visit(ASTPrimitive p) {
        printIndent(p.value.getText());
        return null;
    }

    private void printIndent(String str) {
        for (int i = 0; i < indent; i++)
            System.out.print("  ");
        System.out.println(str);
    }
}
