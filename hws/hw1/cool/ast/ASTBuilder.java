package cool.ast;

import org.antlr.v4.runtime.Token;

import cool.parser.*;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends CoolParserBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
        List<ASTClass> classes = new ArrayList<ASTClass>();

        for (var cls : ctx.classes)
            classes.add((ASTClass) visit(cls));

        return new ASTProgram(classes, ctx.start);
    }

    @Override
    public ASTNode visitClass(CoolParser.ClassContext ctx) {
        List<ASTFeature> features = new ArrayList<ASTFeature>();

        for (var feature : ctx.features)
            features.add((ASTFeature) visit(feature));

        return new ASTClass(ctx.name, ctx.parent, features, ctx.start);
    }

    @Override
    public ASTNode visitMethod(CoolParser.MethodContext ctx) {
        List<ASTFormal> formals = new ArrayList<>();
        ASTExpression content = (ASTExpression) visit(ctx.content);

        for (var formal : ctx.formals)
            formals.add((ASTFormal) visit(formal));

        return new ASTMethod(ctx.name, formals, ctx.returnType, content, ctx.start);
    }

    @Override
    public ASTNode visitAttribute(CoolParser.AttributeContext ctx) {
        return new ASTAttribute((ASTVar)visit(ctx.variable), ctx.start);
    }

    @Override
    public ASTNode visitFormal(CoolParser.FormalContext ctx) {
        return new ASTFormal(ctx.name, ctx.type, ctx.start);
    }

    @Override
    public ASTNode visitVar(CoolParser.VarContext ctx) {
        ASTExpression value = null;

        if (ctx.value != null)
            value = (ASTExpression)visit(ctx.value);

        return new ASTVar(ctx.name, ctx.type, value, ctx.start);
    }

    @Override
    public ASTNode visitCase(CoolParser.CaseContext ctx) {
        return new ASTCase(ctx.name, ctx.type, (ASTExpression) visit(ctx.value), ctx.start);
    }

    @Override
    public ASTNode visitNew(CoolParser.NewContext ctx) {
        return new ASTNew(ctx.type, ctx.start);
    }

    @Override
    public ASTNode visitPlusMinus(CoolParser.PlusMinusContext ctx) {
        return new ASTArithmetic((ASTExpression) visit(ctx.left),
                                 ctx.op,
                                 (ASTExpression) visit(ctx.right),
                                 ctx.start);
    }

    @Override
    public ASTNode visitString(CoolParser.StringContext ctx) {
        return new ASTPrimitive(ctx.value, ctx.start);
    }

    @Override
    public ASTNode visitBool(CoolParser.BoolContext ctx) {
        return new ASTPrimitive(ctx.value, ctx.start);
    }

    @Override
    public ASTNode visitIsvoid(CoolParser.IsvoidContext ctx) {
        return new ASTIsVoid((ASTExpression) visit(ctx.value), ctx.start);
    }

    @Override
    public ASTNode visitWhile(CoolParser.WhileContext ctx) {
        return new ASTWhile((ASTExpression) visit(ctx.cond),
                            (ASTExpression) visit(ctx.body),
                            ctx.start);
    }

    @Override
    public ASTNode visitInt(CoolParser.IntContext ctx) {
        return new ASTPrimitive(ctx.value, ctx.start);
    }

    @Override
    public ASTNode visitSwitch(CoolParser.SwitchContext ctx) {
        List<ASTCase> cases = new ArrayList<>();

        for (var c : ctx.cases)
            cases.add((ASTCase) visit(c));

        return new ASTSwitch((ASTExpression) visit(ctx.object), cases, ctx.start);
    }

    @Override
    public ASTNode visitNot(CoolParser.NotContext ctx) {
        return new ASTNot((ASTExpression)visit(ctx.value), ctx.start);
    }

    @Override
    public ASTNode visitParen(CoolParser.ParenContext ctx) {
        return new ASTParen((ASTExpression)visit(ctx.value), ctx.start);
    }

    @Override
    public ASTNode visitMultDiv(CoolParser.MultDivContext ctx) {
        return new ASTArithmetic((ASTExpression) visit(ctx.left),
                                 ctx.op,
                                 (ASTExpression) visit(ctx.right),
                                 ctx.start);
    }

    @Override
    public ASTNode visitNegate(CoolParser.NegateContext ctx) {
        return new ASTNegate((ASTExpression)visit(ctx.value), ctx.start);
    }

    @Override
    public ASTNode visitDispatch(CoolParser.DispatchContext ctx) {
        List<ASTExpression> args = new ArrayList<>();

        for (var arg : ctx.args)
            args.add((ASTExpression) visit(arg));

        return new ASTDispatch((ASTExpression) visit(ctx.object),
                               ctx.staticType,
                               ctx.methodName,
                               args,
                               ctx.start);
    }

    @Override
    public ASTNode visitBlock(CoolParser.BlockContext ctx) {
        List<ASTExpression> instructions = new ArrayList<>();

        for (var instruction : ctx.instructions)
            instructions.add((ASTExpression) visit(instruction));

        return new ASTBlock(instructions, ctx.start);
    }

    @Override
    public ASTNode visitLet(CoolParser.LetContext ctx) {
        List<ASTVar> vars = new ArrayList<>();

        for (var var : ctx.vars)
            vars.add((ASTVar) visit(var));

        return new ASTLet(vars, (ASTExpression) visit(ctx.context), ctx.start);
    }

    @Override
    public ASTNode visitRelational(CoolParser.RelationalContext ctx) {
        return new ASTArithmetic((ASTExpression) visit(ctx.left),
                                 ctx.op,
                                 (ASTExpression) visit(ctx.right),
                                 ctx.start);
    }

    @Override
    public ASTNode visitId(CoolParser.IdContext ctx) {
        return new ASTId(ctx.name, ctx.start);
    }

    @Override
    public ASTNode visitIf(CoolParser.IfContext ctx) {
        return new ASTIf((ASTExpression) visit(ctx.cond),
                         (ASTExpression) visit(ctx.thenBranch),
                         (ASTExpression) visit(ctx.elseBranch),
                         ctx.start);
    }

    @Override
    public ASTNode visitAssign(CoolParser.AssignContext ctx) {
        return new ASTAssign(ctx.name, (ASTExpression) visit(ctx.value), ctx.start);
    }

    @Override
    public ASTNode visitMethodCall(CoolParser.MethodCallContext ctx) {
        List<ASTExpression> args = new ArrayList<>();

        for (var arg : ctx.args)
            args.add((ASTExpression) visit(arg));

        return new ASTMethodCall(ctx.methodName, args, ctx.start);
    }
}
