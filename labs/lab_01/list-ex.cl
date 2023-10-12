(*
    Laborator COOL.
*)

(*
    Exercițiul 1.

    Implementați funcția fibonacci, utilizând atât varianta recursivă,
    cât și cea iterativă.
*)
class Fibo {
    fibo_rec(n : Int) : Int {
        if n <= 1 then 1 else fibo_rec(n - 1) + fibo_rec(n - 2) fi
    };

    fibo_iter(n : Int) : Int {
        let a : Int <- 0,
            b : Int <- 1,
            c : Int <- a
        in
            {
                while (0 <= n)
                loop
                    {
                        c <- a + b;
                        a <- b;
                        b <- c;
                        n <- n - 1;
                    } pool;
                a;
            }
    };
};
    
(*
    Exercițiul 2.

    Pornind de la ierarhia de clase implementată la curs, aferentă listelor
    (găsiți clasele List și Cons mai jos), implementați următoarele funcții
    și testați-le. Este necesară definirea lor în clasa List și supradefinirea
    în clasa Cons.

    * append: întoarce o nouă listă rezultată prin concatenarea listei curente
        (self) cu lista dată ca parametru;
    * reverse: întoarce o nouă listă cu elementele în ordine inversă.

    Observați cerințele de întoarcere a unei *noi* liste. Operațiile
    se realizează nedistructiv, fără a altera listele existente. Amintiți-vă
    cum ați scrie axiomele pentru append și reverse într-un limbaj funcțional.
*)

(*
    Listă omogenă cu elemente de tip Int. Clasa List constituie rădăcina
    ierarhiei de clase reprezentând liste. O variabilă cu tipul static List
    poate desemna atât liste vide, cât și nevide. O instanță cu tipul dinamic
    List desemnează o listă vidă. O instanță cu tipul dinamic Cons denotă
    o listă nevidă.

    Adaptare după arhiva oficială de exemple a limbajului COOL.
*)
class List inherits IO {
    isEmpty() : Bool { true };

    -- 0, deși cod mort, este necesar pentru verificarea tipurilor
    hd() : Int { { abort(); 0; } };

    -- Similar pentru self
    tl() : List { { abort(); self; } };

    cons(h : Int) : Cons {
        new Cons.init(h, self)
    };

    append(list : List) : List { list };

    reverse() : List { self };

    map(function : Map) : List { self };

    apply(filter : Filter) : List { self };

    print() : IO { out_string("\n") };
};

(*
    În privința vizibilității, atributele sunt implicit protejate, iar metodele,
    publice.

    Atributele și metodele utilizează spații de nume diferite, motiv pentru care
    hd și tl reprezintă nume atât de atribute, cât și de metode.
*)
class Cons inherits List {
    hd : Int;
    tl : List;

    init(h : Int, t : List) : Cons {
        {
            hd <- h;
            tl <- t;
            self;
        }
    };

    -- Supradefinirea funcțiilor din clasa List
    isEmpty() : Bool { false };

    hd() : Int { hd };

    tl() : List { tl };

    append(list : List) : List { tl.append(list).cons(hd) };

    reverse() : List { tl.reverse().append(new List.cons(hd)) };

    map(function : Map) : List { function.apply(self) };

    apply(filter : Filter) : List { filter.apply(self) };

    print() : IO {
        {
            out_int(hd);
            out_string(" ");
            -- Mecanismul de dynamic dispatch asigură alegerea implementării
            -- corecte a metodei print.
            tl.print();
        }
    };
};

(*
    Exercițiul 3.

    Scopul este implementarea unor mecanisme similare funcționalelor
    map și filter din limbajele funcționale. map aplică o funcție pe fiecare
    element, iar filter reține doar elementele care satisfac o anumită condiție.
    Ambele întorc o nouă listă.

    Definiți clasele schelet Map, respectiv Filter, care vor include unica
    metodă apply, având tipul potrivit în fiecare clasă, și implementare
    de formă.

    Pentru a defini o funcție utilă, care adună 1 la fiecare element al listei,
    definiți o subclasă a lui Map, cu implementarea corectă a metodei apply.

    În final, definiți în cadrul ierarhiei List-Cons o metodă map, care primește
    un parametru de tipul Map.

    Definiți o subclasă a subclasei lui Map de mai sus, care, pe lângă
    funcționalitatea existentă, de incrementare cu 1 a fiecărui element,
    contorizează intern și numărul de elemente prelucrate. Utilizați static
    dispatch pentru apelarea metodei de incrementare, deja definită.

    Repetați pentru clasa Filter, cu o implementare la alegere a metodei apply.
*)

class Map inherits IO {
    func(x : Int) : Int { { abort(); 0; } };

    apply(list : List) : List {
        case list of
            c : Cons => new Cons.init(func(c.hd()), apply(c.tl()));
            l : List => new List;
        esac
    };
};

class MapAddOne inherits Map {
    count : Int <- 0;

    func(x: Int) : Int { { count <- count + 1; x + 1; } };

    print() : IO {
        {
            out_string(" ");
            out_int(count);
            out_string("\n");
        }
    };
};

class Filter {
    condition(x : Int) : Bool { { abort(); false; } };

    apply(list: List) : List {
        case list of
            c : Cons => {
                if condition(c.hd())
                then
                    new Cons.init(c.hd(), apply(c.tl()))
                else
                    apply(c.tl())
                fi;
            };
            l : List => new List;
        esac
    };
};

class FilterGETwo inherits Filter {
    condition(x : Int) : Bool { 2 <= x };
};

class FilterEqTwo inherits Filter {
    condition(x : Int) : Bool { 2 = x };
};

-- Testați în main.
class Main inherits IO {
    main() : Object {
        let list : List <- new List.cons(1).cons(2).cons(3),
            temp : List <- list,
            fibo : Fibo <- new Fibo,
            map  : MapAddOne <- new MapAddOne
        in
            {
                out_string("Fibo testing:\n");

                out_string("fibo_rec(5) = ");
                out_int(fibo.fibo_rec(5));
                out_string("\n");
                out_string("fibo_iter(5) = ");
                out_int(fibo.fibo_iter(5));
                out_string("\n");

                -- Afișare utilizând o buclă while. Mecanismul de dynamic
                -- dispatch asigură alegerea implementării corecte a metodei
                -- isEmpty, din clasele List, respectiv Cons.
                while (not temp.isEmpty()) loop
                    {
                        out_int(temp.hd());
                        out_string(" ");
                        temp <- temp.tl();
                    }
                pool;

                out_string("\n");

                -- Afișare utilizând metoda din clasele pe liste.
                list.print();

                out_string("Testing append: ");
                list.append(list).print();

                out_string("Testing reverse: ");
                list.reverse().print();


                out_string("Testing add one: ");
                map.apply(list).print();

                out_string("Printing the count:");
                map.print();

                out_string("Testing filter >= 2: ");
                new FilterGETwo.apply(list).print();

                out_string("Testing filter == 2: ");
                new FilterEqTwo.apply(list).print();
            }
    };
};
