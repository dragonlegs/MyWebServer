package com.company;

import java.io.File;
import java.io.IOException;

/**
 * Created by Hemanth on 10/5/16.
 */
public class FileList {
    private String htmlCode="";
    private String currDir=System.getProperty("user.dir");

public String getHtmlCode(){
    setFiles();
    return htmlCode;
}

public void setFiles(){
    File f1 = new File(currDir);
    File[] strFileDirs = f1.listFiles();
    for (int i =0;i< strFileDirs.length;i++){
        if (strFileDirs[i].isDirectory())
        System.out.println("Directory: " + strFileDirs[i].toString());
        else if(strFileDirs[i].isFile())
            //System.out.println("File: " + strFileDirs[i].toString());

            htmlCode = htmlCode + formatHtmlFile(strFileDirs[i].getName());
            System.out.println(htmlCode);

    }

}

    public String formatHtmlFile(String input){

        return "<img src=\"/icons/text.gif\" alt=\"[TXT]\"> <a href=\""+input+"\">"+input+"</a>\n";

    }




}
