import java.util.ArrayList;
import java.util.List;

public class TypeSymbol extends Symbol {
    public TypeSymbol(String name) {
        super(name);
    }
    
    // Symboluri aferente tipurilor, definite global
    public static final TypeSymbol INT   = new TypeSymbol("Int");
    public static final TypeSymbol FLOAT = new TypeSymbol("Float");
    public static final TypeSymbol BOOL  = new TypeSymbol("Bool");
    public static final List<TypeSymbol> types = new ArrayList<>();

    static {
        types.add(INT);
        types.add(FLOAT);
        types.add(BOOL);
    }
}
