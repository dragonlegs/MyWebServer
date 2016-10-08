package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Hemanth on 10/5/16.
 */
public class FileList {
    private String htmlCode="";
    private String currDir=System.getProperty("user.dir");

public void setCurrDir(String dir){
    currDir = currDir + dir;
    System.out.println(currDir);

}

public byte[] getFile(String fileName){
    try {


        Path path = Paths.get(currDir);
        System.out.println(path.toAbsolutePath().toString());
        byte[] data = Files.readAllBytes(path);
        return data;
    }catch (IOException e){
        System.out.println("Unable to get file");
        return "File not Found".getBytes();
    }

}
public String getHtmlCode(){
    setFiles();
    return htmlCode;
}

public void setFiles(){
    File f1 = new File(currDir);
    File[] strFileDirs = f1.listFiles();
    for (int i =0;i< strFileDirs.length;i++){
        if (strFileDirs[i].isDirectory()) {
            System.out.println("Directory: " + strFileDirs[i].toString());
            htmlCode = htmlCode + formatHtmlFile(strFileDirs[i], "DIR");
            System.out.println(htmlCode);
        }
        else if(strFileDirs[i].isFile()) {
            //System.out.println("File: " + strFileDirs[i].toString());
            //System.out.println(strFileDirs[i].getAbsolutePath());
            //getMIMEtype(strFileDirs[i].getName());
            htmlCode = htmlCode + formatHtmlFile(strFileDirs[i], getMIMEtype(strFileDirs[i].getName()));
            System.out.println(htmlCode);
        }

    }


}

    public String formatHtmlFile(File input, String mimeType){
        Date lastModified = new Date(input.lastModified());
        int spaceing =5;

        return "<img src=\"\" alt=\"["+mimeType.split("/")[0]+"]\">  <a href=\""+input.getName()+"\">"+input.getName()+"</a>"+"           "+lastModified.toString()+"     "
                  +input.length()+"\n";

    }
    public String formatHtmlHeader(){

        return "<h1>Index of " + currDir + "</h1>";

    }

    public String getCurrDir(){
        return this.currDir;
    }

//
//    public File sendFile(String fileName){
//        System.out.println("Received File:");
//        return new File();
//    }
    private String getSpace(String input,int space){
        int length = input.length();
        System.out.println("Length: " + length);
        String returnString ="";
        if (length < space) {
            for (int i =0;i <= space-length;i++){
                returnString = returnString + " ";
            }
        }
        //System.out.println("ReturnString is: " + returnString);
        return returnString;



    }
    public String getMIMEtype(String fileName){
//        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") !=0)
//            fileName.substring(fileName.lastIndexOf(".")+1);
//        System.out.println("File EXT: " + fileName);
        String[] ext = fileName.split("\\.");
        String extCheck = ext[ext.length-1];
        switch (extCheck){
            case "txt":
                return "text/plain";
            case "java":
                return "text/plain";
            case "mp3":
                return "audio/mpeg3";
            case "jpeg":
                return "image/jpeg";
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "html":
                return "text/html";
            default :
                return "text/plain";

        }

    }


}
