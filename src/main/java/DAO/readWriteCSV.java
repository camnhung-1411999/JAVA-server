package DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class readWriteCSV {
    public static List<user> read() {
        BufferedReader reader = null;
        List<user> list = new ArrayList<user>();

        try {
            String line = "";
            reader = new BufferedReader(new FileReader("src\\main\\java\\DAO\\user.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 0) {
                    user temp = new user();
                    temp.setUsername(fields[0]);
                    temp.setName(fields[1]);
                    temp.setPassword(fields[2]);
                    list.add(temp);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return list;
    }
    public static void write(List<user> list){
        FileWriter fw=null;
        try{
            fw=new FileWriter("src\\main\\java\\DAO\\user.csv");
            fw.append("username,name,password\n");

            for(int i=0;i<list.size();i++){
                fw.append(list.get(i).getUsername());
                fw.append(",");
                fw.append(list.get(i).getName());
                fw.append(",");
                fw.append(list.get(i).getPassword());
                fw.append("\n");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(fw!=null){
                try{
                    fw.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
