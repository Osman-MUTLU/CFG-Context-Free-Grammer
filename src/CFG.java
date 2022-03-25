import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CFG {
    //Global variables.
    private ArrayList<Grammar> grammars;
    private String input;
    private Stack<Character> stack;
    private Stack<String> derivation;
    private ArrayList<String> tree;
    private File fl;
    private FileWriter fw;
    private BufferedWriter bw;
    /*
    *
    *  *---------------------------------------*
    *  | Grammars must be sorted by length!!!! |
    *  *---------------------------------------*
    *
    */

    //Constructor
    public CFG(ArrayList<Grammar> grammars, String input) throws IOException {
        fl = new File("output.txt");
        fw = new FileWriter(fl);
        bw = new BufferedWriter(fw);
        this.grammars = grammars;
        this.tree = new ArrayList();
        this.input = input+"$";
        this.stack = new Stack();
        if(isAccept()){

            System.out.println("\""+input.replace("$"," ")+"\" string is accepted.");
            System.out.println();
            bw.write("\""+input.replace("$"," ")+"\" string is accepted.");
            bw.newLine();
            bw.newLine();
            System.out.println("-----------< Derivation >-----------");
            System.out.println();
            bw.write("-----------< Derivation >-----------");
            bw.newLine();
            bw.newLine();
            printDerivation();
            bw.newLine();
            bw.newLine();
            bw.write("--------------< Tree >--------------");
            bw.newLine();
            bw.newLine();
            System.out.println();
            System.out.println();
            System.out.println("--------------< Tree >--------------");
            System.out.println();
            printTree();



        }
        else {
            bw.newLine();
            bw.write("The string is rejected.");
            System.out.println("The string is rejected.");
        }
        bw.close();
        fw.close();
    }
    // Returns whether the string is acceptable.
    // This process starts the whole derivation process.
    public boolean isAccept(){
        this.derivation = new Stack();
        // Puts the input characters sequentially into the stack.
        // After each operation, the stack is re-derived.
        for (int i = 0; i < input.length()-1; i++) {
            stack.push(input.charAt(i));
            derivationStack(input.charAt(i+1));
        }
        //After the derivation process ends, it checks the remaining characters in the stack.
        //If only one terminal is left, the input is acceptable.However, the input cannot be accepted.
        if(!stack.isEmpty()){
            if(stack.size()>1) return false;
            else {
                boolean flag = false;
                char lastChar = stack.pop();
                for(Grammar g: grammars){
                    if(lastChar == g.getVeriable().charAt(0)) flag = true;
                }
                return flag;
            }
        }
        else return false;

    }
    //Derives the stack content based on the next character and grammars.
    public void derivationStack(char nextChar){
        //Turns within the grammars.
        for(Grammar g : grammars){
            //If Stack size is bigger than grammar's rule length,
            if(stack.size()>=g.getRule().length()){
                //Flag is set to true by default.
                boolean flag=true;
                Stack<Character> temp = new Stack();
                for (int i = 0; i < g.getRule().length(); i++) {
                    temp.push(stack.pop());
                }
                for (int i = 0; i < g.getRule().length(); i++) {
                    char data = temp.peek();
                    stack.push(temp.pop());
                    if(data!=g.getRule().charAt(i)){
                        //If a character of the grammar's rule and the stack element do not match, the flag is false.
                        flag=false;
                    }
                }
                //If the flag is true, it means that the stack and the grammar are compatible.
                if(flag){
                    //maxConv determines the limit of derivation for the peek of Stack.
                    char maxConv = '$';
                    for(Grammar tempG: grammars){
                        boolean tempFlag = false;
                        for (int i = 0; i < tempG.getRule().length(); i++) {
                            if(tempG.getRule().charAt(i)==nextChar) tempFlag=true;
                        }
                        if(tempFlag){
                            //If nextChar is in a grammar, the first element of that grammar is maxConv.
                            maxConv = tempG.getRule().charAt(0);
                        }
                    }
                    //If maxConv is not equal to peek of stack,Stack is derived.
                    if(maxConv!=stack.peek()){
                        for (int i = 0; i < g.getRule().length(); i++) {
                            stack.pop();
                        }
                        derivation.push(g.getVeriable()+"-"+g.getRule());
                        stack.push(g.getVeriable().charAt(0));
                        derivationStack(nextChar);
                    }
                    break;
                }
            }
        }
    }
    //Regex replace with replacement at the last founded substring.
    public String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }

    //Prints the tree.
    private void printTree() throws IOException {
        Stack<String> temp = new Stack();
        String[] strTemp =  derivation.peek().split("-");

        temp.push(derivation.pop());
        tree.add(strTemp[0]);
        tree.add(strTemp[1]);
        String str = strTemp[1];

        while(!derivation.isEmpty()){
            strTemp = derivation.peek().split("-");
            temp.push(derivation.pop());
            str = replaceLast(str,strTemp[0],strTemp[1]);
            tree.add(str);
        }

        while(!temp.isEmpty())derivation.push(temp.pop());

        for(int i =0;i<tree.size();i++){

            for (int j = 0; j < tree.get(tree.size()-1).length()-tree.get(i).length(); j++) {
                System.out.print(" ");
            }
            for (int j = 0; j < tree.get(tree.size()-1).length()-tree.get(i).length(); j++) {
                bw.write(" ");
            }
            for (int j = 0; j < tree.get(i).length(); j++) {
                System.out.print(tree.get(i).charAt(j)+" ");
            }
            for (int j = 0; j < tree.get(i).length(); j++) {
                bw.write(tree.get(i).charAt(j)+" ");
            }
            bw.newLine();
            System.out.println();
        }
    }

    //Prints the derivation.
    private void printDerivation() throws IOException {
        Stack<String> temp = new Stack();
        String[] strTemp =  derivation.peek().split("-");
        temp.push(derivation.pop());
        bw.write((strTemp[0]+" -> "));
        bw.write(strTemp[1]);
        System.out.print(strTemp[0]+" -> ");
        System.out.print(strTemp[1]);
        String str = strTemp[1];
        int counter =0;
        while(!derivation.isEmpty()){
            counter++;
            strTemp = derivation.peek().split("-");
            temp.push(derivation.pop());
            str = replaceLast(str,strTemp[0],strTemp[1]);
            bw.write(" -> "+str);
            System.out.print(" -> "+str);
            if(counter%4 == 0){
                System.out.println();
                bw.newLine();
            }
        }
        while(!temp.isEmpty())derivation.push(temp.pop());
    }
}
