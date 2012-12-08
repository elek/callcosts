package net.anzix.callcost.rulefile;

import net.anzix.callcost.option.Option;
import net.anzix.callcost.option.AndOption;
import net.anzix.callcost.option.OrOption;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author elekma
 */
public class OptionExpressionParser {

    List<String> elements = new ArrayList<String>();

    public Option parse(String str, Map<String, Map<String, String>> optionParameters, PojoCreator creator) {
        try {
            str = str.replaceAll("\\[", "(");
            str = str.replaceAll(",", " ");
            str = str.replaceAll("\\]", ")");
            str = str.replaceAll("\\)", " ) ");
            str = str.replaceAll("\\(", " ( ");
            parseToReversePolish(optionParameters, str);
            return createOptions(optionParameters, creator);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Can't parse expression " + str + " ", ex);
        }
    }

    private Option createOptions(Map<String, Map<String, String>> optionParameters, PojoCreator creator) {
        Stack s = new Stack();
        for (String element : elements) {
            if (element.equals(",")) {
                continue;
            }
            if (isFunction(optionParameters, element) || isOperator(element)) {
                if (element.equals("OR")) {
                    Option o = new OrOption((Option) s.pop(), (Option) s.pop());
                    s.push(o);
                    continue;
                } else if (element.equals("AND")) {
                    Option o = new AndOption((Option) s.pop(), (Option) s.pop());
                    s.push(o);
                    continue;
                }
                Option option = createOption(s, creator, optionParameters, element);
                s.push(option);


            } else if (isOperator(element)) {
                // operator
                s.pop();
            } else {
                s.push(element);
            }
        }
        return (Option) s.pop();
    }

    private Option createOption(Stack s, PojoCreator creator, Map<String, Map<String, String>> optionParameters, String element) {
        if (optionParameters.containsKey(element)) {

            Map<String, String> params = new LinkedHashMap<java.lang.String, java.lang.String>(optionParameters.get(element));
            String type = (String) params.get("type");
            if (type == null) {
                throw new IllegalArgumentException("Type parameter is missing for option " + element);
            }
            params.remove("type");
            String clazzName = "net.anzix.callcost.option." + type;
            setParameters(params, s);
            try {
                Class<? extends Option> optionType = (Class<? extends Option>) Class.forName(clazzName);
                return creator.create(optionType, params);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);

            }
        } else {
            try {
                Class<? extends Option> type = (Class<? extends Option>) Class.forName(element);
                try {
                    Constructor<? extends Option> constructor = getConstructorWithMoreParam(type);
                    Object[] param = new Object[constructor.getParameterTypes().length];
                    int i = 0;
                    for (Class conParam : constructor.getParameterTypes()) {
                        param[i++] = creator.convert(type, "CONSTRUCTOR", conParam, s.pop());
                    }
                    return constructor.newInstance(param);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Can't create the Option " + type.getCanonicalName(), e);
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("No such option " + element);
            }
        }

    }

    private Constructor<? extends Option> getConstructorWithMoreParam(Class<? extends Option> type) {
        Constructor<? extends Option> selected = null;
        for (Constructor c : type.getDeclaredConstructors()) {
            if (selected == null || c.getParameterTypes().length > selected.getParameterTypes().length) {
                selected = c;
            }
        }
        return selected;
    }

    private void setParameters(Map<String, String> params, Stack s) {
        for (String key : new ArrayList<String>(params.keySet())) {
            if (params.get(key).equals("{}")) {
                params.put(key, (String) s.pop());
            }
        }
    }

    protected void parseToReversePolish(Map<String, Map<String, String>> optionParameters, String str) {
        Stack<String> stack = new Stack<String>();
        for (String token : str.split(" ")) {
            if (token.trim().length() == 0) {
                continue;
            }
            //If the token is a number, then add it to the output queue.
            //If the token is a function token, then push it onto the stack.
            //If the token is a function argument separator (e.g., a comma):
            if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    elements.add(stack.pop());
                }
                // )
                stack.pop();
                if (stack.size() > 0 && isFunction(optionParameters, stack.peek())) {
                    elements.add(stack.pop());
                }

            } else if (isOperator(token)) {
                while (stack.size() > 0 && isOperator(stack.peek())) {
                    elements.add(stack.pop());
                }
                stack.push(token);
            } else if (isFunction(optionParameters, token)) {
                stack.push(token);
            } else {
                //number or any other parameter
                if (!token.matches("[0-9\\.]+") && !token.startsWith("\"")) {
                    throw new IllegalArgumentException("Wrong parameter format: " + token);
                }
                if (token.startsWith("\"")) {
                    stack.push(token.substring(1, token.length() - 1));
                } else {
                    stack.push(token);
                }
            }

        }
        while (stack.size() > 0) {
            elements.add(stack.pop());
        }

    }

    private boolean isOperator(String token) {
        return token.equals("OR") || token.equals("AND");
    }

    private boolean isFunction(Map<String, Map<String, String>> optionParameters, String peek) {
        if (optionParameters.containsKey(peek)) {
            return true;
        }
        try {
            Class.forName(peek);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static class Expression {

    }

    public static class Function extends Expression {

    }

    public static class Operator extends Expression {

    }

    public static class Parameter extends Expression {

    }

}
