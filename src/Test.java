import java.io.*;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException {
        ArrayList<Grammar> grammars = new ArrayList();
        File file = new File("CFG.txt");
        FileReader fileReader = new FileReader(file);
        String line;
        BufferedReader br = new BufferedReader(fileReader);
        /*

         Context Free Grammar

        */
        while ((line = br.readLine()) != null) {
            String[] rule = line.split(">");
            if(rule[1].contains("|")){
                String[] tempRule = rule[1].split("\\|");
                for (int i = 0; i < tempRule.length; i++) {
                    Grammar g = new Grammar(rule[0],tempRule[i]);
                    grammars.add(g);
                }
            }
            else{
                Grammar g = new Grammar(rule[0],rule[1]);
                grammars.add(g);
            }
        }
        br.close();
        grammars = InsertionSort(grammars);
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the string : ");
        String userinput= sc.next();
        System.out.println();
        CFG cfg = new CFG(grammars,userinput);

    }
    static ArrayList<Grammar> InsertionSort(ArrayList<Grammar> array)
    {
        Grammar temp;
        int size = array.size();
        for(int i = 0; i < size-1; i++) // Starting from the 1st index of the directory, it proceeds to lastest  index.
        {
            if(array.get(i).getRule().length() > array.get(i+1).getRule().length())
            {
                temp = array.get(i);  // If the element of the array is less than the previous element, it becomes a swap.
                array.set((i),array.get(i+1))  ;  // In swap, 1 temp is used except two indexes.
                array.set((i+1),temp);
                i = 0;
            }
        }
        ArrayList<Grammar> temparray = new ArrayList();
        for (int i = array.size()-1; i >= 0; i--) {
            temparray.add(array.get(i));
        }
        return temparray;
    }

}
