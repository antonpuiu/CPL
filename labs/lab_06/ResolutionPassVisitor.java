
public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {     
    @Override
    public TypeSymbol visit(Program prog) {
        for (var stmt: prog.stmts) {
            stmt.accept(this);
        }
        return null;
    }
    
    @Override
    public TypeSymbol visit(Id id) {
        // Verificăm dacă într-adevăr avem de-a face cu o variabilă
        // sau cu o funcție, al doilea caz constituind eroare.
        // Puteți folosi instanceof.
        var symbol = id.getScope().lookup(id.getToken().getText());

        if (symbol instanceof FunctionSymbol) {
            ASTVisitor.error(id.getToken(),
                  id.getToken().getText() + " is not a variable");
            return null;
        }

        // TODO 2: Întoarcem informația de tip salvată deja în simbol încă de la
        // definirea variabilei.
        return id.getSymbol().getType();
    }
    
    @Override
    public TypeSymbol visit(VarDef varDef) {
        if (varDef.initValue != null) {
            var varType  = varDef.id.getSymbol().getType();
            var initType = varDef.initValue.accept(this);
            
            // TODO 5: Verificăm dacă expresia de inițializare are tipul potrivit
            // cu cel declarat pentru variabilă.
            if (!varType.equals(initType) && initType != null) {
                ASTVisitor.error(varDef.getToken(), varDef.getToken().getText() + " has different type than " + initType.getName());
                return null;
            }
        }
        
        return varDef.id.getSymbol().getType();
    }
    
    @Override
    public TypeSymbol visit(FuncDef funcDef) {
        var returnType = funcDef.id.getSymbol().getType();
        var bodyType = funcDef.body.accept(this);
        
        // TODO 5: Verificăm dacă tipul de retur declarat este compatibil
        // cu cel al corpului.
        if (!returnType.equals(bodyType)) {
            ASTVisitor.error(funcDef.getToken(), "Return type " + returnType.getName() + " is different from body type " + bodyType.getName());
            return null;
        }
        
        return null;
    }

    @Override
    public TypeSymbol visit(Call call) {
        // Verificăm dacă funcția există în scope. Nu am putut face
        // asta în prima trecere din cauza a forward references.
        //
        // De asemenea, verificăm că Id-ul pe care se face apelul de funcție
        // este, într-adevăr, o funcție și nu o variabilă.
        //
        // Hint: pentru a obține scope-ul, putem folosi call.id.getScope(),
        // setat la trecerea anterioară.
        var id = call.id;
        var symbol = id.getScope().lookup(id.getToken().getText());

        if (symbol == null) {
            ASTVisitor.error(id.getToken(),
                  id.getToken().getText() + " function undefined");
            return null;
        }

        if (!(symbol instanceof FunctionSymbol)) {
            ASTVisitor.error(id.getToken(),
                  id.getToken().getText() + " is not a function");
            return null;
        }
        
        var functionSymbol = (FunctionSymbol)symbol;
        id.setSymbol(functionSymbol);
        
        // TODO 6: Verificați dacă numărul parametrilor actuali coincide
        // cu cel al parametrilor formali, și că tipurile sunt compatibile.
        var formals = functionSymbol.getFormals();

        return null;
    }   
    
    @Override
    public TypeSymbol visit(Assign assign) {
        var idType   = assign.id.accept(this);
        var exprType = assign.expr.accept(this);
        
        // TODO 5: Verificăm dacă expresia cu care se realizează atribuirea
        // are tipul potrivit cu cel declarat pentru variabilă.
        if (idType.equals(TypeSymbol.INT) || idType.equals(TypeSymbol.FLOAT)) {
            if (exprType.equals(TypeSymbol.BOOL)) {
                ASTVisitor.error(assign.getToken(), assign.id.getToken().getText() + " is numeric, while " + assign.expr.getToken().getText() + " is bool");
                return null;
            }
        }

        if (idType.equals(TypeSymbol.BOOL) && !exprType.equals(TypeSymbol.BOOL)) {
            ASTVisitor.error(assign.getToken(), assign.id.getToken().getText() + "is bool, while " + assign.expr.getToken().getText() + " is numeric");
            return null;
        }
        
        return idType;
    }

    @Override
    public TypeSymbol visit(If iff) {        
        var condType = iff.cond.accept(this);
        var thenType = iff.thenBranch.accept(this);
        var elseType = iff.elseBranch.accept(this);
        
        // TODO 4: Verificați tipurile celor 3 componente, afișați eroare
        // dacă este cazul, și precizați tipul expresiei.
        if (!condType.equals(TypeSymbol.BOOL)) {
            ASTVisitor.error(iff.cond.getToken(), iff.cond.getToken().getText() + " wrong type: " + condType.getName());

            return null;
        }

        if (!thenType.equals(TypeSymbol.BOOL)) {
            ASTVisitor.error(iff.thenBranch.getToken(), iff.thenBranch.getToken().getText() + " wrong type: " + condType.getName());

            return null;
        }

        if (!elseType.equals(TypeSymbol.BOOL)) {
            ASTVisitor.error(iff.elseBranch.getToken(), iff.elseBranch.getToken().getText() + " wrong type: " + condType.getName());

            return null;
        }

        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(Type type) {
        return null;
    }

    @Override
    public TypeSymbol visit(Formal formal) {
        return formal.id.getSymbol().getType();
    }

    // Operații aritmetice.
    @Override
    public TypeSymbol visit(UnaryMinus uMinus) {
        var exprType = uMinus.expr.accept(this);
        
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        for (var t : TypeSymbol.types) {
            if (exprType.equals(t))
                return t;
        }

        ASTVisitor.error(uMinus.expr.getToken(), uMinus.expr.getToken().getText() + " type not found");
        return null;
    }

    @Override
    public TypeSymbol visit(Div div) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        TypeSymbol t1 = div.left.accept(this);
        TypeSymbol t2 = div.right.accept(this);

        if (t1.equals(t2))
            return t1;

        ASTVisitor.error(div.getToken(), div.getToken().getText() + " left and right expr types differ");
        return null;
    }

    // 2 + (2/x)

    @Override
    public TypeSymbol visit(Mult mult) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        TypeSymbol t1 = mult.left.accept(this);
        TypeSymbol t2 = mult.right.accept(this);

        if (t1.equals(t2))
            return t1;

        ASTVisitor.error(mult.getToken(), mult.getToken().getText() + " left and right expr types differ");
        return null;
    }

    @Override
    public TypeSymbol visit(Plus plus) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        TypeSymbol t1 = plus.left.accept(this);
        TypeSymbol t2 = plus.right.accept(this);

        if (t1.equals(t2))
            return t1;

        ASTVisitor.error(plus.getToken(), plus.getToken().getText() + " left and right expr types differ");
        return null;
    }

    @Override
    public TypeSymbol visit(Minus minus) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        TypeSymbol t1 = minus.left.accept(this);
        TypeSymbol t2 = minus.right.accept(this);

        if (t1.equals(t2))
            return t1;

        ASTVisitor.error(minus.getToken(), minus.getToken().getText() + " left and right expr types differ");
        return null;
    }

    @Override
    public TypeSymbol visit(Relational relational) {
        // TODO 3: Verificăm tipurile operanzilor, afișăm eroare dacă e cazul,
        // și întoarcem tipul expresiei.
        // Puteți afla felul operației din relational.getToken().getType(),
        // pe care îl puteți compara cu CPLangLexer.EQUAL etc.
        TypeSymbol t1 = relational.left.accept(this);
        TypeSymbol t2 = relational.right.accept(this);

        if (t1.equals(t2))
            return TypeSymbol.BOOL;

        ASTVisitor.error(relational.getToken(), relational.getToken().getText() + " left and right expr types differ");
        return null;
    }

    // Tipurile de bază
    @Override
    public TypeSymbol visit(Int intt) {
        // TODO 2: Întoarcem tipul corect.
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Bool bool) {
        // TODO 2: Întoarcem tipul corect.
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(FloatNum floatNum) {
        // TODO 2: Întoarcem tipul corect.
        return TypeSymbol.FLOAT;
    }
    
};
