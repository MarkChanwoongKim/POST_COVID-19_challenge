import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        String path = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\3-2. Contents";
        String targetPath = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\data";
        File dir = new File(path);
        File[] fileList = dir.listFiles();
        boolean read = false;
        String str;
        URL obj;
        HttpURLConnection con;
        DataOutputStream out;
        JsonParser Parser;
        JsonObject jsonObj;

        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        try {
            obj = new URL("https://language.googleapis.com/v1/documents:analyzeSentiment?key= "); // 호출할 url
            int fileSize = fileList.length;
            int count =1;
            System.out.println("총 파일 개수: "+fileSize);
            for(File file : fileList){
                if(file.isFile()){
                    //System.out.println(file.getName());

                    FileReader filereader = new FileReader(file);
                    BufferedReader bufReader = new BufferedReader(filereader);
                    String line = "";
                    read = false;
                    sb = new StringBuffer();
                    while((line = bufReader.readLine()) != null){
                        if(line.startsWith("Ariticle")){
                            read=true;
                        }
                        if(!read){
                            continue;
                        }
                        sb.append(line.replaceAll("[(,),\",“,•,—,-,®,[,],™,»]", ""));
                    }
                    bufReader.close();
                    //System.out.println(sb.toString());

                    con = (HttpURLConnection)obj.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    con.setRequestProperty("Content-Type","application/json");

                    str = "{\"document\":{\"type\":\"PLAIN_TEXT\",\"content\":\""+sb.toString()+"\"}}";

                    out = null;

                    out = new DataOutputStream(con.getOutputStream());
                    out.writeBytes(str);
                    out.flush();
                    out.close();

                    try{
                        in = new BufferedReader(new InputStreamReader((con.getInputStream())));
                        line="";
                        sb = new StringBuffer();
                        while ((line=in.readLine())!=null){
                            sb.append(line);

                            //System.out.println(line);
                        }
                        Parser = new JsonParser();
                        jsonObj = (JsonObject) Parser.parse(sb.toString());
                        jsonObj = (JsonObject) Parser.parse(jsonObj.get("documentSentiment").toString());
                        //System.out.println(Float.parseFloat(jsonObj.get("score").toString()));
                        float v = Float.parseFloat(jsonObj.get("score").toString());
                        if(v<-0.25){
                            moveFile("0",file.getName(),path,targetPath);
                        }else if(v>=-0.25&&v<=0.25){
                            moveFile("1",file.getName(),path,targetPath);
                        }else if(v>0.25) {
                            moveFile("2", file.getName(), path, targetPath);
                        }else{
                            moveFile("error", file.getName(), path, targetPath);
                        }
                    }catch (Exception e){
                        moveFile("error",file.getName(),path,targetPath);
                    }

                    System.out.println("분류중.."+count+"/"+fileSize);
                    count++;

                }
            }


        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(in != null)
                try {
                    in.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
        }

    }


    public static String moveFile(String folderName, String fileName, String beforeFilePath, String afterFilePath) {

        String target = afterFilePath+"/"+folderName+"/"+fileName;
        String filePath = beforeFilePath+"/"+fileName;

        try{

            File file =new File(filePath);

            if(file.renameTo(new File(target))){ //파일 이동
                return target; //성공시 성공 파일 경로 return
            }else{
                return null;
            }

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
