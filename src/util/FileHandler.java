package util;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class FileHandler {

    public static HashMap <String, Integer> userDataMap = new HashMap<String, Integer>();
    static File userDataFile = new File("userData");

    public static boolean searchForUserName (String userName){
        if(!userDataFile.exists()){
            makeFile();
        }

        try {
            Scanner dataReader = new Scanner(userDataFile);
            while (dataReader.hasNextLine()) {
                String data = dataReader.nextLine();
                String[] fields = data.split(",");
                if(fields[0].equals(userName)){
                    userDataMap.put(fields[0], Integer.valueOf(fields[1]));
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Reading error");
        }
        return false;
    }

    private static void makeFile() {
        System.out.println("No file");
        try {
            userDataFile.createNewFile();
            System.out.println("File Made");
        } catch (IOException ioException) {
            System.out.println("make file error");
        }
    }

    public static void writeInFile(String userName, int score){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                    new FileWriter("userData", true));
            writer.write( userName + "," + score + "\n");
            writer.close();
            userDataMap.put(userName, 0);
        } catch (IOException e) {
            System.out.println("File Writing error");
        }
    }

    public static void updateRecord(String userName, String newRecord) {
        try {
            writeInTempFile(userName, newRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            changeTempToUserData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void changeTempToUserData() throws FileNotFoundException {
        PrintWriter writer1 = new PrintWriter("userData");
        //clear Patient file
        writer1.print("");
        File tempFile = new File("temp");
        Scanner tempReader = new Scanner(tempFile);
        //write all the data in temp file into Patient file
        while (tempReader.hasNextLine()) {
            String data = tempReader.nextLine();
            String[] fields = data.split(",");
            writer1.print(fields[0] + "," + fields[1] + "\n");
        }
        tempReader.close();
        writer1.close();
        //delete temp file
        tempFile.delete();
    }

    private static void writeInTempFile(String userName, String newRecord) throws IOException {

        Scanner dataReader = new Scanner(userDataFile);
        //write to a temp file
        BufferedWriter writer = new BufferedWriter(
                new FileWriter("temp", true));
        while (dataReader.hasNextLine()) {
            String data = dataReader.nextLine();
            String[] fields = data.split(",");
            //write data from userData file to temp file
            //if usernames were the same write the new record in temp file
            if (userName.equals(fields[0])) {
                writer.write(fields[0] + "," + newRecord + "\n");
                userDataMap.clear();
                userDataMap.put(fields[0], Integer.valueOf(newRecord));
            }
            else
                writer.write(fields[0] + "," + fields[1] + "\n");
        }
        writer.close();
        dataReader.close();
    }
}



