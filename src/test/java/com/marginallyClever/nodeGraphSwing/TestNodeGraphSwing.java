package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.*;
import com.marginallyClever.nodeGraphCore.builtInNodes.LoadNumber;
import com.marginallyClever.nodeGraphCore.builtInNodes.PrintToStdOut;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.Add;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.Multiply;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.Subtract;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class TestNodeGraphSwing {
    private static NodeGraph model;

    @BeforeAll
    static void beforeAll() {
        model = new NodeGraph();
        NodeFactory.registerBuiltInNodes();
    }

    @BeforeEach
    public void beforeEach() {
        model.clear();
    }

    @Test
    public void testNodeVariablesToJSONAndBack() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Turtle t = new Turtle();
        t.jumpTo(10,20);
        t.moveTo(30,40);
        testNodeVariableToJSONAndBack(Turtle.class, t,new Turtle());
    }
}
