import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ForderToCSV {
    public static void main(String[] args) {
        BufferedWriter bufWriter = null;
        String path[] = {"C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\data\\0","C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\data\\1","C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\data\\2"};
        String targetCSV = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\감정.csv";
        Map<Integer,ArrayList<String>> data = new HashMap<>();
        try {
            for(int i=0;i<3;i++){
                File dir = new File(path[i]);
                File[] fileList = dir.listFiles();
                ArrayList<String> num = new ArrayList<>();
                data.put(i-1,num);
                for(File file : fileList){
                    if(file.isFile()) {
                        data.get(i-1).add(file.getName());
                    }
                }
            }

            data.get(1);


            bufWriter = Files.newBufferedWriter(Paths.get(targetCSV));
            bufWriter.write("filename");
            bufWriter.write(",");
            bufWriter.write("Sentiment");
            bufWriter.write(",");
            bufWriter.newLine();

            for(Integer key : data.keySet()){
                for(int i=0;i<data.get(key).size();i++){
                    bufWriter.write(data.get(key).get(i)+"");
                    bufWriter.write(",");
                    bufWriter.write(key+"");
                    bufWriter.write(",");
                    bufWriter.newLine();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(bufWriter != null){
                    bufWriter.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
