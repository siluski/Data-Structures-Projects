package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
	
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/                                  
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    		int i = 0;
    		expr = expr.replaceAll(" ","");
        	expr = expr.replaceAll("\t", "");
    		StringTokenizer s = new StringTokenizer(expr,"1234567890*/+-()]");
    		
    		while(s.hasMoreTokens())
    		{
    			String token = s.nextToken();
    			if(token.indexOf('[')!=-1)
    			{
    				i = 0;
    				String name = "";
    				while(i<token.length())
    				{
    					if(Character.isLetter(token.charAt(i)))
    					{
    						name = name + token.charAt(i);
    					}			
    					
    					else if(token.charAt(i)=='[')
    					{
    						Array a = new Array(name);
    						if(arrays.contains(a)==false)
    						{
    							arrays.add(a);
    						}
    						i++;
    						name = "";
    						continue;
    					}
    					if(i==token.length()-1)
    					{
    						Variable v = new Variable(name);
    						if(vars.contains(v)==false)
    						{
    							vars.add(v);
    						}
    						name = "";
    					}
    					i++;
    				}
    			}
    			else {
    			Variable v = new Variable(token);
   				if(vars.contains(v)==false)
   				{
   					vars.add(v);
   				}
    			}
    		}
    		
       }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	ArrayList<String> newExpr = new ArrayList<String>();
    	expr = expr.replaceAll(" ","");
    	expr = expr.replaceAll("\t", "");
    	StringTokenizer str = new StringTokenizer(expr,"+-/*()[]",true);
    	while(str.hasMoreTokens())
    	{
    		newExpr.add(str.nextToken());
    	}
    	return Evaluate(newExpr,vars,arrays);
    }
    public static float Evaluate(ArrayList<String>expr, ArrayList<Variable> vars, ArrayList<Array> arrays)
    {
    	Stack<String> ops = new Stack();
    	Stack<Float> values = new Stack();
    	Stack<Float> tempVal = new Stack();
    	Stack<String> tempOps = new Stack();
    	int openIndex = 0;
    	int closeIndex = 0;
    	int parentheses = 0;
    	float result = 0;
    	for(int i=0; i<expr.size(); i++)
    	{
    		if(isDigit(expr.get(i)))
    		{
    			values.push(Float.parseFloat(expr.get(i)));
    		}
    		if(isName(expr.get(i)))
    		{
    			Variable v = new Variable(expr.get(i));
    			if(vars.contains(v))
    			{
    				values.push(getVar(expr.get(i),vars));
    			}
    			
    		}
    		if(isOperation(expr.get(i)))
    		{
    			ops.push(expr.get(i));
    		}
    		if(expr.get(i).charAt(0)=='(')
    		{
    			openIndex = i;
    			parentheses++;
    			int j = i;
    			i++;
    			for(j = i; j<expr.size(); j++)
    			{
    				if(parentheses==1&&expr.get(j).equals(")"))
    				{
    					closeIndex = j;
    	    			i=j;
    					break;
    				}
    				if(expr.get(j).charAt(0)=='(')
    				{
    					parentheses++;
    				}
    				if(expr.get(j).charAt(0)==')')
    				{
    					parentheses--;
    				}
    			}
    			
    			ArrayList<String>subExpr = new ArrayList(expr.subList(openIndex+1, closeIndex));
    			values.push(Evaluate(subExpr,vars,arrays));
    			openIndex = 0;
    			closeIndex = 0;
    			parentheses = 0;
    		}
    		if(expr.get(i).equals("["))
    		{
    			openIndex = i;
    			String name = expr.get(i-1);
    			parentheses++;
    			int j = i;
    			i++;
    			for(j=i;j<expr.size();j++)
    			{
    				if(parentheses==1&&expr.get(j).equals("]"))
    				{
    					closeIndex = j;
    					i = j;
    					break;
    				}
    				if(expr.get(j).equals("["))
    				{
    					parentheses++;
    				}
    				if(expr.get(j).equals("]"))
    				{
    					parentheses--;
    				}
    			}
    			ArrayList<String>subExpr = new ArrayList(expr.subList(openIndex+1, closeIndex));
    			values.push(getArr(name,(int)Evaluate(subExpr,vars,arrays),arrays));
    			openIndex = 0;
    			closeIndex = 0;
    			parentheses = 0;
    		}
    		
    	}
    	Stack<Float>reverseNum = new Stack();
    	Stack<String>reverseOp = new Stack();
    	while(values.isEmpty()==false)
    	{
    		reverseNum.push(values.pop());
    	}
    	while(ops.isEmpty()==false)
		{
    		reverseOp.push(ops.pop());
		}
    	while(reverseOp.isEmpty()==false)
    	{
    		String currentOp = reverseOp.pop();
    		if(currentOp.equals("*")||currentOp.equals("/"))
    		{
    			float x = reverseNum.pop();
        		float y = reverseNum.pop();
        		reverseNum.push(calculate(x,y,currentOp));
    		}
    		if(currentOp.equals("+")||currentOp.equals("-"))
    		{
    			tempVal.push(reverseNum.pop());
    			tempOps.push(currentOp);
    		}
    	}
    	while(tempOps.isEmpty()==false)
    	{
    		reverseOp.push(tempOps.pop());
    	}
    	while(tempVal.isEmpty()==false)
    	{
    		reverseNum.push(tempVal.pop());
    	}
    	while(reverseOp.isEmpty()==false)
    	{
    		String currentOp = reverseOp.pop();
    		float x = reverseNum.pop();
    		float y = reverseNum.pop();
    		reverseNum.push(calculate(x,y,currentOp));
    			
    	}
    	return reverseNum.pop();
    }
    public static boolean isOperation(String str)
    {
    	String ops = "+-*/";
    	if(ops.indexOf(str)!=-1)
    	{
    		return true;
    	}
    	return false;
    }
    public static float calculate(float x, float y, String operation) {
    	if(operation.equals("+")) {return x+y;}
    	if(operation.equals("-")) {return x-y;}
    	if(operation.equals("*")) {return x*y;}
    	return x/y;
    	
    }
    public static boolean isName(String str)
    {
    	if(str.matches("[a-zA-z]+"))
    	{
    		return true;
    	}
    	return false;
    }
    public static float getVar(String name, ArrayList<Variable>vars)
    {
    	for(int i = 0; i<vars.size();i++)
    	{
    		if(vars.get(i).name.equals(name))
    		{
    			return vars.get(i).value;
    		}
    	}
    	return 0;
    }
    public static float getArr(String name, int index, ArrayList<Array>arrays)
    {
    	for(int i = 0; i<arrays.size(); i++)
    	{
    		if(arrays.get(i).name.equals(name))
    		{
    			return arrays.get(i).values[index];
    			
    		}
    	}
    	return 0;
    }
    public static boolean isDigit(String str)
    {
    	for(int i = 0; i<str.length();i++)
    	{
    		if(Character.isDigit(str.charAt(i)))
    		{
    			return true;
    		}
    	}
    	return false;
    }
}
