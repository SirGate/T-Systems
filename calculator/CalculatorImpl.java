/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.math.BigDecimal;


/**
 *
 * @author Sergei Tulupov
 */
public class Calculator {

    public static String str;
    char[] mass;
    double[] operands;
    int[][] operations;
    int isempty, quantity;
    boolean isNull = false;

  
    public static void main(String[] args) {
  
        
    }
   public String evaluate(String statement) {
        int j, i;
        isempty = -1;
        quantity = 0;
        j = (int) statement.length();
        mass = new char[j];
        statement.getChars(0, j, mass, 0);
        if (!verifyStr(mass)) { 
            return null;
        }
        if (quantity == 0) {    
            return statement;
        }
        if ((quantity == 1) & (mass[0] == '-')) {
            return statement;
        }
        transformStr(mass, mass.length + 1, quantity);
        
        double res = getResult(quantity);           
        if (isNull) {                             
            return null;
        }
        statement = statement.valueOf(
                new BigDecimal(res).setScale(4, BigDecimal.ROUND_UP));
        return statement;
    }

    
    private boolean verifyStr(char[] mass) {
        int i, summ;
        summ = 0;
        for (i = 0; i < mass.length; i++) {
            if (mass[i] == '(') {
                summ++;
            }
            if (mass[i] == ')') {
                summ--;
            }
            if (summ < 0) {
                return false;
            }
            if (i < mass.length - 1) {

                if (mass[i] == '(') {
                    if (!((mass[i + 1] == '0') | (mass[i + 1] == '1')
                            | (mass[i + 1] == '2') | (mass[i + 1] == '3') | (mass[i + 1] == '4')
                            | (mass[i + 1] == '5') | (mass[i + 1] == '6')
                            | (mass[i + 1] == '7') | (mass[i + 1] == '8')
                            | (mass[i + 1] == '9') | (mass[i + 1] == '(') | (mass[i + 1] == '-'))) {
                        return false;
                    }
                }

                if (mass[i] == ')') {
                    if (!((mass[i + 1] == '*') | (mass[i + 1] == '/')
                            | (mass[i + 1] == '+') | (mass[i + 1] == '-') | (mass[i + 1] == ')'))) {
                        return false;
                    }
                }

                if (mass[i] == '.') {
                    if (!((mass[i + 1] == '0') | (mass[i + 1] == '1')
                            | (mass[i + 1] == '2') | (mass[i + 1] == '3') | (mass[i + 1] == '4') | (mass[i + 1] == '5') | (mass[i + 1] == '6')
                            | (mass[i + 1] == '7') | (mass[i + 1] == '8') | (mass[i + 1] == '9'))) {
                        return false;
                    }
                }

                if ((mass[i] == '*') | (mass[i] == '/') | (mass[i] == '-') | (mass[i] == '+')) {
                    if ((mass[i + 1] == '*') | (mass[i + 1] == '/')
                            | (mass[i + 1] == '+') | (mass[i + 1] == '-') | (mass[i + 1] == ')') | (mass[i + 1] == '.')) {
                        return false;
                    }
                }

                if (((mass[i + 1] == '0') | (mass[i + 1] == '1')
                        | (mass[i + 1] == '2') | (mass[i + 1] == '3') | (mass[i + 1] == '4') | (mass[i + 1] == '5') | (mass[i + 1] == '6')
                        | (mass[i + 1] == '7') | (mass[i + 1] == '8') | (mass[i + 1] == '9'))) {
                    if (mass[i + 1] == '(') {
                        return false;
                    }
                }
            };

            if ((mass[0] == '*') | (mass[0] == '/') | (mass[0] == '+')) {
                return false;
            }

            if ((mass[mass.length - 1] == '*') | (mass[mass.length - 1] == '/')
                    | (mass[mass.length - 1] == '+') | (mass[mass.length - 1] == '-')) {
                return false;
            }

            if ((mass[i] == '*') | (mass[i] == '/') | (mass[i] == '-')
                    | (mass[i] == '+') | (mass[i] == '(') | (mass[i] == ')')) {
                quantity++;
            }
        }
        if (summ > 0) {
            return false;
        }
        return true;
    }


    
    private void transformStr(char[] mass, int size1, int size2) {
        operands = new double[size1];
        operations = new int[size2][3];
        int i = 0;
        int j = 0;
        int number = 0;
        int number1 = 0;
        String str1;
        for (number = 0; number < size2; number++) {
            if ((i == 0) & (mass[0] == '(')) {
                operations[number][0] = -1;   
                operations[number][1] = -1; 
                i = 1;
                j = i;
                number++;
            }
            if ((i == 0) & (mass[0] == '-')) {
                operations[number][0] = -3;   
                operations[number][1] = number1;
                operands[number1] = -1;
                number1++;
                i = 1;
                j = i;
                number++;
            }
            while ((!(mass[i] == '(')) & (!(mass[i] == ')'))
                    & (!(mass[i] == '*')) & (!(mass[i] == '/'))
                    & (!(mass[i] == '+')) & (!(mass[i] == '-')) & (i < mass.length)) {
                i++;
            }
            if (i == j) {
                operations[number][1] = -1;
                operations[number - 1][2] = -1;
                if ((mass[i] == '-') & (mass[i - 1] == '(')) {
                    mass[i] = '*';
                    operands[number1] = -1;
                    operations[number][1] = number1;
                    operations[number - 1][2] = number1;
                    number1++;
                }
            } else {
                str1 = new String(mass, j, i - j);
                Double dbl = new Double(str1); 
                operands[number1] = dbl;
                operations[number][1] = number1;
                if (number > 0) {
                    operations[number - 1][2] = number1;
                }
                number1++;
            }

            switch (mass[i]) {     
                case '(':
                    operations[number][0] = -1;
                    break;
                case ')':
                    operations[number][0] = -2;
                    break;
                case '*':
                    operations[number][0] = -3;
                    break;
                case '/':
                    operations[number][0] = -4;
                    break;
                case '+':
                    operations[number][0] = -5;
                    break;
                case '-':
                    operations[number][0] = -6;
                    break;
            }
            i++;
            j = i;
        }
        if (operations[number - 1][0] == -2) {
            operations[number - 1][2] = -1; 
        } else {
            str1 = new String(mass, j, mass.length - j);
            Double dbl = new Double(str1);
            operands[size1 - 1] = dbl;
            operations[number - 1][2] = size1 - 1;
        }
    }

   
    private double calculate(int begin, int end) {
        int i, j;
        int max;
        int prevmax;// 
        double result = 0;
        boolean iscalculated = false;

        while (!iscalculated) { 
            max = -1;
            prevmax = -1;
            for (i = begin; i < end; i++) {
                if ((operations[i][0] == -3) | (operations[i][0] == -4)) {
                    max = 2;
                    prevmax = i;
                }
                if ((operations[i][0] == -5) | (operations[i][0] == -6)) {
                    if (max == -1) {
                        max = 1;
                        prevmax = i;
                    }
                } 
            }
            if (prevmax > -1) { 
                switch (operations[prevmax][0]) { 
                    case -3:
                        result = operands[operations[prevmax][1]] 
                                * operands[operations[prevmax][2]];
                        operations[prevmax][0] = -10;  
                        isempty = operations[prevmax][2];
                        operands[operations[prevmax][1]] = result;
                        for (j = prevmax; j > 0; j--) {
                            if (j > 0) {
                                if (operations[j - 1][0] > -9) {
                                    operations[j - 1][2] = operations[prevmax][1];
                                 
                                    break;
                                }
                            }
                        }
                        for (j = prevmax; j < end; j++) {
                            if (j < end - 1) {
                                if (operations[j + 1][0] > -9) {
                                    operations[j + 1][1] = operations[prevmax][1];
                                  
                                    break;
                                }
                            }
                        }
                        break;
                    //   
                    case -4:
                        if (operands[operations[prevmax][2]] == 0) {
                            isNull = true; // 
                        }
                        result = operands[operations[prevmax][1]]
                                / operands[operations[prevmax][2]];
                        operations[prevmax][0] = -10;
                        isempty = operations[prevmax][2];
                        operands[operations[prevmax][1]] = result;
                        for (j = prevmax; j > begin; j--) {
                            if (j > 0) {
                                if (operations[j - 1][0] > -9) {
                                    operations[j - 1][2] = operations[prevmax][1];
                                    break;
                                }
                            }
                        }
                        for (j = prevmax; j < end; j++) {
                            if (j < end - 1) {
                                if (operations[j + 1][0] > -9) {
                                    operations[j + 1][1] = operations[prevmax][1];
                                    break;
                                }
                            }
                        }
                        break;
                    case -5:
                        result = operands[operations[prevmax][1]]
                                + operands[operations[prevmax][2]];
                        operations[prevmax][0] = -10;
                        isempty = operations[prevmax][2];
                        operands[operations[prevmax][1]] = result;
                        for (j = prevmax; j > begin; j--) {
                            if (j > begin) {
                                if (operations[j - 1][0] > -9) {
                                    operations[j - 1][2] = operations[prevmax][1];
                                    break;
                                }
                            }
                        }
                        for (j = prevmax; j < end; j++) {
                            if (j < end - 1) {
                                if (operations[j + 1][0] > -9) {
                                    operations[j + 1][1] = operations[prevmax][1];
                                    break;
                                }
                            }
                        }
                        break;
                    case -6:
                        result = operands[operations[prevmax][1]]
                                - operands[operations[prevmax][2]];
                        operations[prevmax][0] = -10;
                        isempty = operations[prevmax][2];
                        operands[operations[prevmax][1]] = result;
                        for (j = prevmax; j > 0; j--) {
                            if (j > 0) {
                                if (operations[j - 1][0] > -9) {
                                    operations[j - 1][2] = operations[prevmax][1];
                                    break;
                                }
                            }
                        }
                        for (j = prevmax; j < end; j++) {
                            if (j < end - 1) {
                                if (operations[j + 1][0] > -9) {
                                    operations[j + 1][1] = operations[prevmax][1];
                                    break;
                                }
                            }
                        }
                        break;
                }
            }
            if (prevmax == -1) {
                iscalculated = true;
            }
        }
        return result;
    }
   
    private double getResult(int size) {
        int i, j, k, d;
        int prevmax, nextmax;
        double result = 0;
        boolean iscalculated = false;
        boolean b;
        while (!iscalculated) {
            prevmax = -10;
            nextmax = -10;
            for (i = 0; i < size; i++) {
                if (operations[i][0] == -1) { 
                    prevmax = i;
                }
            }
            if (!(prevmax == -10)) {
                operations[prevmax][0] = -10;
            }
            if (prevmax > -1) {
                j = prevmax;
                while ((nextmax == -10)) {
                    j++;
                    if ((operations[j][0] == -2)) {
                        operations[j][0] = -10;
                        nextmax = j;
                    }
                }
       result = calculate(prevmax + 1, nextmax);
       operands[isempty] = result;
                k = prevmax;
                b = false;
                while (!b) {
                    if (k > 0) {
                        if (operations[k - 1][0] > -9) {
                            operations[k - 1][2] = isempty;
                            b = true;
                        }
                    }
                    if (k == 0) {
                        b = true;
                    }
                    k--;
                }
                k = nextmax;
                b = false;
                while (!b) {
                    if (k < size - 1) {
                        if (operations[k + 1][0] > -9) {
                            operations[k + 1][1] = isempty;
                            b = true;
                        }
                    }
                    if (k == size) {
                        b = true;
                    }
                    k++;
                }
            }
            if (prevmax == -10) {
                iscalculated = true;
            }
        }
        for (i = 0; i < size; i++) {
            if (operations[i][0] > -9) {
                result = calculate(0, size);
            }
        }
        return result;
    }
}
