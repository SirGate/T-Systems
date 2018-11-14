/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsystems.javaschool.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Tulupov Sergei
 */
public class DuplicateFinderImpl implements DuplicateFinder {

    String[] strings;
    String[][] finalStr;
    byte[] strOut;
    int quantity;
    private FileInputStream source;
    private FileOutputStream dest;

    String str;

    public DuplicateFinderImpl() {
    }

    public static void main(String[] args) {
        DuplicateFinder d = new DuplicateFinderImpl();
       System.out.println(d.process(new File("a.txt"), new File("b.txt")));

    }

    @Override
    public boolean process(File sourceFile, File targetFile) {

        if (load(sourceFile) == false) {   //Загружаем исходный файл
            return false;
        };
        finalStr = sort(strings);     //сортируем строки
        if (save(targetFile) == false) {// сохраняем отсортированные строки в конечный файл
            return false;
        };
        return true;//Если не было ошибок возвращаем true ,иначе false
    }

    // Этот метод открывает файл и считывет из него данные преобразуя их в массив строк
    
    private boolean load(File filename) {    
        int i, j;
        j = 0;
        File f1 = filename;
        j = (int) f1.length(); // определяем размер файла
        char[] mass = new char[j];// создаем массив такого размера
        j = 0;

        try {
            source = new FileInputStream(f1);  //открываем входящий поток
        } catch (FileNotFoundException e) {  
            return false;
        }
        try {
            do {
                i = source.read();// читаем по байту файл
                if (i != -1) { // пока не конец
                    mass[j] = (char) i;// и сохраняем в массив символов

                }
                j += 1;
            } while (i != -1);
        } catch (IOException e) {
            return false;
        }
        try {
            source.close();//закрываем входящий поток
        } catch (IOException e) {
            return false;
        }

        str = new String(mass);//преобразуем считанный из файла массив символов в строку
        strings = str.trim().split("\n");//разбиваем строку на подстроки - в качестве разделителя перевод строки 
        return true;
    }

    // Этот метод сортирует строки
    
    private String[][] sort(String[] str) {
        int i, j;
        int summ, summ2;
        String stroka = "";

        summ2 = str.length;
        String[][] str1 = new String[2][str.length];//создаем двумерный массив: первый элемент отвечает  
        for (i = 0; i < str.length; i++) {          // за кол-во повторений строки в файле, второй - собственно
            str1[0][i] = "1";                       //сама строка
            str1[1][i] = str[i];                  //Cчитываем входящий массив в наш двумерный;  
        }                                        //число повторений пока везде ставим 1
        for (i = 0; i < str.length; i++) {    //Ищем число повторений каждой строки и суммируем их
            summ = 1;
            if (!(str1[0][i].trim().equals("-1"))) {
                for (j = i + 1; j < str.length; j++) {
                    if (!(str1[0][j].trim().equals("-1"))) {
                        if (str1[1][j].trim().equals(str1[1][i].trim())) {//сравниваем строки;если они одинаковы ,
                            summ++;                // то у второй ставим чисдо повторений -1 - чтобы больше не анализировать,
                            summ2--;               //а у исходной увеличиваем число повторений summ на 1; при этом summ2 - 
                            str1[0][j] = "-1";    //это общее число неповторяющихся строк уменьшается на одну
                        }
                    }
                }
                stroka = stroka.valueOf(summ);//преобразуем окончательное число повторений строки в строку и сохраняем в массиве
                str1[0][i] = stroka;
            }
        }
        String[][] strings1 = new String[2][summ2];//создаем новый массив , в которм число элементов равно числу 
               j=0;                                // неповторяющихся строк
        

        for (i = 0; i < str.length; i++) {        //заполняем его
            if (!(str1[0][i].equals("-1"))) {
                strings1[0][j] = str1[0][i];
                strings1[1][j] = str1[1][i];
                j++;
            }
        }
        stroka = "";                    
        for (j = 0; j < summ2 - 1; j++) {    //сортируем строки улучшенным методом пузырька
            for (i = 0; i < summ2 - j - 1; i++) {
                if (strings1[1][i].compareTo(strings1[1][i + 1]) > 0) {
                    str1[0][0] = strings1[0][i];
                    str1[1][0] = strings1[1][i];
                    strings1[0][i] = strings1[0][i + 1];
                    strings1[1][i] = strings1[1][i + 1];
                    strings1[0][i + 1] = str1[0][0];
                    strings1[1][i + 1] = str1[1][0];
                }
            }
        }
        return strings1;
    }

   // Этот метод открывает конечный файл. Если его нет, то он его создает, если есть - открывает 
   // и дописывает исходные данные в конец 
    
    private boolean save(File filename) {
        int i;
        String str = "";
        File f1 = filename;

        try {
            dest = new FileOutputStream(f1, true);// открываем выходной поток с дозаписью данных 
        } catch (FileNotFoundException e) {
            return false;
        }

        for (i = 0; i < finalStr[1].length; i++) {//создаем выходные строки путем соединенеия двух 
            // элементов массива - собственно строки и ее повторений в скобочках; все строки соединяем в одну большую строку
            // str - для удобства вывода в файл
            str = str.concat(finalStr[1][i].trim() + "[" + finalStr[0][i].trim() + "]" + "\r\n");
        }
        strOut = str.getBytes();//преобразуем выходную строку в массив байтов
        try {
            dest.write(strOut);//записываем его в файл
        } catch (IOException ex) {
            return false;
        }
        try {
            dest.close();
        } catch (IOException e) {
            return false;
        }
        return true;// если не было ошибок - возвращаем true
    }

}
