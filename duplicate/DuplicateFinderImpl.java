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

        if (load(sourceFile) == false) {   //Loading original file
            return false;
        };
        finalStr = sort(strings);     //sorting  strings
        if (save(targetFile) == false) {// saving sorted strings to the final file
		return false;
        };
        return true;//if is'nt errors- return true ,else false
    }

    // This method opens file and  load data from it and transforms data to String array
    
    private boolean load(File filename) {    
        int i, j;
        j = 0;
        File f1 = filename;
        j = (int) f1.length(); // measure file length
        char[] mass = new char[j];// create an array of that length 
        j = 0;

        try {
            source = new FileInputStream(f1);  //open inputstream
        } catch (FileNotFoundException e) {  
            return false;
        }
        try {
            do {
                i = source.read();// read file by byte
                if (i != -1) { // while  isn't end 
                    mass[j] = (char) i;// and save to the char array
					
                }
                j += 1;
            } while (i != -1);
        } catch (IOException e) {
            return false;
        }
        try {
            source.close();//close inputstream
        } catch (IOException e) {
            return false;
        }

        str = new String(mass);//transform read array to String
        strings = str.trim().split("\n");//split String by the "\n" on substrings 
        return true;
    }

    // This method sorts the substrings
    
    private String[][] sort(String[] str) {
        int i, j;
        int summ, summ2;
        String stroka = "";

        summ2 = str.length;
        String[][] str1 = new String[2][str.length];//create String array by two dimensions:first - counter of repetitions of substring   
        for (i = 0; i < str.length; i++) {          // in the file, second  - substring themselves
            str1[0][i] = "1";                       
            str1[1][i] = str[i];                  //reading input array to our new two-dimensions array;  
        }                                        //counter of repetitions everywere make 1
        for (i = 0; i < str.length; i++) {    //finding number of repetitions for every substrings
            summ = 1;
            if (!(str1[0][i].trim().equals("-1"))) {
                for (j = i + 1; j < str.length; j++) {
                    if (!(str1[0][j].trim().equals("-1"))) {
                        if (str1[1][j].trim().equals(str1[1][i].trim())) {//compare the substrings; ,
                            summ++;                // and for the copies set counter to -1 ,
                            summ2--;               //and the origin substring counter - summ increase on 1; and summ2 - 
                            str1[0][j] = "-1";    //it's common number of non-reapeted substrings decrease on 1
                        }
                    }
                }
                stroka = stroka.valueOf(summ);//transform the final number of repetitions of substring  
                str1[0][i] = stroka;         //in to String and save it to array  
            }
        }
        String[][] strings1 = new String[2][summ2];//create new array , in which  number of elements the same as numuber of  
               j=0;                                // non-reapeted substrings - summ2
			   

        for (i = 0; i < str.length; i++) {        //fill it 
            if (!(str1[0][i].equals("-1"))) {
                strings1[0][j] = str1[0][i];
                strings1[1][j] = str1[1][i];
                j++;
            }
        }
        stroka = "";                    
        for (j = 0; j < summ2 - 1; j++) {    //sort the strings by  modified method of buble
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

   // This method opens final file. If it doesn't exist,it will be creates, if it exist - it will be opened 
   // and data will be added to the end  
    
    private boolean save(File filename) {
        int i;
        String str = "";
        File f1 = filename;

        try {
            dest = new FileOutputStream(f1, true);  
        } catch (FileNotFoundException e) {
            return false;
        }

        for (i = 0; i < finalStr[1].length; i++) {//creating output strings by concatenate of two elements of  
            // array - substring and its counter of repetitions; all substrings concatenate in one big string 
            // str - for easier way to save in the file
            str = str.concat(finalStr[1][i].trim() + "[" + finalStr[0][i].trim() + "]" + "\r\n");
        }
        strOut = str.getBytes();//transform output big string in the byte array
        try {
            dest.write(strOut);//write array to file
        } catch (IOException ex) {
            return false;
        }
        try {
            dest.close();
        } catch (IOException e) {
            return false;
        }
        return true;// if there is no errors - return true
    }

}
