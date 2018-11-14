/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.javaschool.tasks;

import java.math.BigDecimal;

/**
 *
 * @author тошиба
 */
public class CalculatorImpl implements Calculator {

    public static String str;
    char[] mass;
    double[] operands;
    int[][] operations;
    int isempty, quantity;
    boolean isNull = false;

   public  CalculatorImpl() {
    }

    public static void main(String[] args) {
        Calculator c = new CalculatorImpl();
        System.out.println(c.evaluate("(-2)*((3-4)*(-1+6)+1)/4"));
    }

    @Override
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
        
        double res = getResult(quantity);  �ние           
        if (isNull) {                             
            return null;
        }
        statement = statement.valueOf(
                new BigDecimal(res).setScale(4, BigDecimal.ROUND_UP));// округляем и преобразуем его в строку
        return statement;
    }

    // Этот метод проверяет строку на наличие различных ошибок, проверяет равенство открытых и закрытых
    // скобок;если выражение верно возвращает true иначе false
    
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

    // этот метод преобразует исходную строку в два массива - операций типа int и операндов типа double
    
    private void transformStr(char[] mass, int size1, int size2) {
        operands = new double[size1];
        operations = new int[size2][3];
        int i = 0;
        int j = 0;
        int number = 0;//номер операции
        int number1 = 0;//номер операнда
        String str1;
        for (number = 0; number < size2; number++) {
            if ((i == 0) & (mass[0] == '(')) { // если первой идет открывающая скобочка
                operations[number][0] = -1;     // -1 это '('; -2 это ')'; -3 это "*"; -4 это '/';-5 это '+'; -6 это '-';
                operations[number][1] = -1; // -1 это значит нет первого операнда
                i = 1;
                j = i;
                number++;
            }
            if ((i == 0) & (mass[0] == '-')) { //если первым идет "-" 
                operations[number][0] = -3;     // -1 это '('; -2 это ')'; -3 это "*"; -4 это '/';-5 это '+'; -6 это '-';
                operations[number][1] = number1; // первый операнд = -1;
                operands[number1] = -1;
                number1++;
                i = 1;
                j = i;
                number++;
            }
            while ((!(mass[i] == '(')) & (!(mass[i] == ')'))// ищем следующую операцию
                    & (!(mass[i] == '*')) & (!(mass[i] == '/'))
                    & (!(mass[i] == '+')) & (!(mass[i] == '-')) & (i < mass.length)) {
                i++;
            }
            if (i == j) {// если она идет следом за предыдущей, то есть у этих операций нет правого и левого операндов
                operations[number][1] = -1;
                operations[number - 1][2] = -1;// -1 - значит второго операнда нет
                if ((mass[i] == '-') & (mass[i - 1] == '(')) {//если за "("идет "-", то заменяем его на умножение на -1
                    mass[i] = '*';
                    operands[number1] = -1;
                    operations[number][1] = number1;
                    operations[number - 1][2] = number1;
                    number1++;
                }
            } else { //следующая операция идет не сразу, т.е. между ними есть операнд
                str1 = new String(mass, j, i - j);//выделяем  этот операнд 
                Double dbl = new Double(str1); // преобразуем его в тип double
                operands[number1] = dbl;// сохраняем его в массиве операндов
                operations[number][1] = number1;// cохраняем индекс первого операнда у текущей операции
                if (number > 0) {
                    operations[number - 1][2] = number1;// cохраняем его же индекс как индекс второго операнда
                    //у предыдущей операции
                }
                number1++;
            }

            switch (mass[i]) {                    //сохраняем тип текущей операции
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
        if (operations[number - 1][0] == -2) {//если последняя операция это ")"
            operations[number - 1][2] = -1; // -1 это значит нет последнего операнда
        } else {
            str1 = new String(mass, j, mass.length - j);// если нет операции - то последним идет операнд 
            Double dbl = new Double(str1);// получаем и сохраняем его
            operands[size1 - 1] = dbl;
            operations[number - 1][2] = size1 - 1;
        }
    }

    //этот метод вычисляет простое выражение (в котором нет скобочек)
    // begin - номер индекса входящей операции end - выходящей-1
   
    private double calculate(int begin, int end) {
        int i, j;
        int max;
        int prevmax;// 
        double result = 0;
        boolean iscalculated = false;

        while (!iscalculated) { //пока не посчитали результат
            max = -1;
            prevmax = -1;// номер предыдущей операции с максимальным приоритетом ; -1 - нет такой операции
            for (i = begin; i < end; i++) {// находим номер операции с максимальным приоритетом и сохраняем его
                if ((operations[i][0] == -3) | (operations[i][0] == -4)) {
                    max = 2;//2 - максимальный приоритет у "*" и "/"
                    prevmax = i;// номер операции с максимальным приоритетом
                }
                if ((operations[i][0] == -5) | (operations[i][0] == -6)) {
                    if (max == -1) {
                        max = 1;// приоритет 1 у "-" "+"
                        prevmax = i;
                    }
                } 
            }
            if (prevmax > -1) {      //если нашли операцию с максимальным пироритетом то выпоняем ее
                switch (operations[prevmax][0]) {   //определяем какая это операция
                    case -3:
                        result = operands[operations[prevmax][1]]   //вычисляем результат операции
                                * operands[operations[prevmax][2]];
                        operations[prevmax][0] = -10;   // "обнуляем эту операцию"
                        isempty = operations[prevmax][2];// запоминаем индекс освободившегося операнда для использования
                        operands[operations[prevmax][1]] = result;//сохраняем результат вместо первого операнда
                        for (j = prevmax; j > 0; j--) {//ищем предыдущую операцию
                            if (j > 0) {
                                if (operations[j - 1][0] > -9) {
                                    operations[j - 1][2] = operations[prevmax][1];// и результат этой операции 
                                    //сохраняем как второй операнд у предыдущей
                                    break;
                                }
                            }
                        }
                        for (j = prevmax; j < end; j++) {//ищем следующую операцию 
                            if (j < end - 1) {
                                if (operations[j + 1][0] > -9) {
                                    operations[j + 1][1] = operations[prevmax][1];//и результат этой операции
                                   //сохраняем как первый операнд у предыдущей  
                                    break;
                                }
                            }
                        }
                        break;
                    //    тоже самое делаем при других типах операций
                    case -4:
                        if (operands[operations[prevmax][2]] == 0) {
                            isNull = true; // на ноль делить нельзя
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
            if (prevmax == -1) {//если мы не нашли никакую операцию
                iscalculated = true;//значит результат уже посчитан
            }
        }
        return result;
    }

   // Этоn метод вычисляет все выражения, при этом для вычислений внутри  скобочек
   // он вызывает метод calculate(int begin, int end).
    
    private double getResult(int size) {
        int i, j, k, d;
        int prevmax, nextmax;
        double result = 0;
        boolean iscalculated = false;
        boolean b;
        while (!iscalculated) {//пока не посчитан результат
            prevmax = -10;//индекс предыдущей открывающей скобочки в массиве операций
            nextmax = -10;//индекс последующей закрывающей скобочки в массиве операций
            for (i = 0; i < size; i++) {
                if (operations[i][0] == -1) { // ищем индекс последней открывающей скобочки и запоминаем его
                    prevmax = i;
                }
            }
            if (!(prevmax == -10)) {
                operations[prevmax][0] = -10;//"обнуляем" ее
            }
            if (prevmax > -1) {
                j = prevmax;
                while ((nextmax == -10)) {// ищем следующую закрывающую скобочку
                    j++;
                    if ((operations[j][0] == -2)) {
                        operations[j][0] = -10;//"обнуляем" ее
                        nextmax = j;// и запоминаем
                    }
                }
       result = calculate(prevmax + 1, nextmax);// вызываем метод calculate и считаем выражение внутри скобочек
       operands[isempty] = result;//результат запоминаем в свободном операнде,индекс которого запомнили в isempty 
                k = prevmax;
                b = false;
                while (!b) {
                    if (k > 0) {
                        if (operations[k - 1][0] > -9) {
                            operations[k - 1][2] = isempty;//сохраняем результат вычислений как второй операнд 
                            //у предыдущей  операции
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
                            operations[k + 1][1] = isempty;//сохраняем результат как первый 
                            //операнд у следующей операции
                            b = true;
                        }
                    }
                    if (k == size) {
                        b = true;
                    }
                    k++;
                }
            }
            if (prevmax == -10) {// если скобочек больше нет , то результат почти посчитан
                iscalculated = true;
            }
        }
        for (i = 0; i < size; i++) {//если остались какие-либо не "обнуленные" операции , то
            // досчитываем выражение с помощью метода calculate
            if (operations[i][0] > -9) {
                result = calculate(0, size);
            }
        }
        return result;
    }
}
