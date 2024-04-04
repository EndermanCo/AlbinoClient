package ir.albino.client.features.ui.html.modules;

import ir.albino.client.features.ui.html.annotations.HTMLAttributes;
import ir.albino.client.features.ui.html.serialize.HTMLSerializable;
import ir.albino.client.features.ui.html.annotations.ConstructorInjection;
import net.minecraft.client.gui.GuiButton;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Map;


public class HTMLButton extends GuiButton implements HTMLSerializable {

    public Expression heightExp;
    public Expression widthExp;
    public Expression yExp;
    public Expression xExp;


    @ConstructorInjection(attrs = {"buttonId", "x", "y"})
    public HTMLButton(int buttonId, String x, String y, String buttonText) {
        this(buttonId, 0, 0, buttonText);
        this.xExp = new ExpressionBuilder(x).variables("width", "height").build();
        this.yExp = new ExpressionBuilder(y).variables("width", "height").build();
    }

    public HTMLButton initButton(Map<String, Double> map) {
        if (xExp != null)
            xPosition = (int) xExp.setVariables(map).evaluate();
        if (yExp != null)
            this.yPosition = Double.valueOf(yExp.setVariables(map).evaluate()).intValue();
        if (widthExp != null)
            this.setWidth(Double.valueOf(widthExp.setVariables(map).evaluate()).intValue());
        if (heightExp != null)
            this.setHeight(Double.valueOf(heightExp.setVariables(map).evaluate()).intValue());
        return this;
    }

    public HTMLButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }


    public HTMLButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    @ConstructorInjection(attrs = {"id", "x", "y", "width", "height", "buttonText"})
    public HTMLButton(int id, String x, String y, String width, String height, String text) {
        this(id, x, y, text);
        this.widthExp = new ExpressionBuilder(width).variables("width", "height").build();
        this.heightExp = new ExpressionBuilder(height).variables("width", "height").build();

    }

}