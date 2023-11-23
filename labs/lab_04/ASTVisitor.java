public interface ASTVisitor<T> {
    // TODO2: Adăugați metode pentru fiecare clasă definită anterior
    T visit(Prog prog);

    T visit(Formal formal);

    T visit(VarDef varDef);

    T visit(FuncDef funcDef);

    T visit(FuncCall fc);

    T visit(UnaryMinus minus);

    T visit(MultDiv md);

    T visit(PlusMinus pm);

    T visit(Relational rel);

    T visit(If iff);

    T visit(Assign asg);

    T visit(Paren pr);

    T visit(Id id);

    T visit(Int intt);

    T visit(Float flt);

    T visit(Bool bl);
}
