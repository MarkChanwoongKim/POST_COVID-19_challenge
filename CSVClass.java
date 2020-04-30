import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

class NationData{
    private ArrayList<Integer> count;
    private ArrayList<Integer> dateCount;

    public NationData(int count,int dateCount){
        this.count = new ArrayList();
        this.dateCount = new ArrayList<>();
        this.count.add(count);
        this.dateCount.add(dateCount);
    }

    public ArrayList getCount() {
        return count;
    }

    public ArrayList getDateCount() {
        return dateCount;
    }


    public NationData addData(int count,int dateCount){
        int select=0-1;
        for(int i =0;i<this.dateCount.size();i++){
            if(this.dateCount.get(i)==dateCount){
                select = i;
                break;
            }
        }
        if(select>=0){
            this.count.set(select,this.count.get(select)+count);
        }else{
            this.count.add(count);
            this.dateCount.add(dateCount);
        }
        return this;
    }

}

public class CSVClass {
    private static int atoi(String str) {return Integer.parseInt(str);}

    public static void main(String[] args) {
        BufferedReader br = null;
        BufferedWriter bufWriter = null;
        Map<Integer, Map<String, NationData>> data = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String targetCSV = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\2. Roaming_data.csv";
        String savePoint = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\data.csv";


        try{
            br = Files.newBufferedReader(Paths.get(targetCSV));
            String line = "";

            br.readLine();
            while((line = br.readLine()) != null){
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                tmpList = Arrays.asList(array);

                Date startDate = sdf.parse(tmpList.get(3));
                Date endDate = sdf.parse(tmpList.get(2));

                long diffDay = (startDate.getTime() - endDate.getTime()) / (24*60*60*1000);
                diffDay++;
                if(!data.containsKey(atoi(tmpList.get(0)))){
                    Map<String, NationData> tmpNationData = new HashMap<>();
                    tmpNationData.put(tmpList.get(1),new NationData(atoi(tmpList.get(4)),(int)diffDay));
                    data.put(atoi(tmpList.get(0)),tmpNationData);
                }else{
                    if(data.get(atoi(tmpList.get(0))).containsKey(tmpList.get(1))){
                        data.get(atoi(tmpList.get(0))).get(tmpList.get(1)).addData(atoi(tmpList.get(4)),(int)diffDay);
                    }else{
                        data.get(atoi(tmpList.get(0))).put(tmpList.get(1),new NationData(atoi(tmpList.get(4)),((int)diffDay)));
                    }

                }

            }

            bufWriter = Files.newBufferedWriter(Paths.get(savePoint));
            bufWriter.write("return");
            bufWriter.write(",");
            bufWriter.write("from");
            bufWriter.write(",");
            bufWriter.write("count");
            bufWriter.write(",");
            bufWriter.write("dayCount");
            bufWriter.write(",");
            bufWriter.newLine();

            for(Integer key : data.keySet()){
                for(String nation : data.get(key).keySet()){
                    for(int i=0;i<data.get(key).get(nation).getCount().size();i++){
                        bufWriter.write(key+"");
                        bufWriter.write(",");
                        bufWriter.write(nation);
                        bufWriter.write(",");
                        bufWriter.write(data.get(key).get(nation).getCount().get(i)+"");
                        bufWriter.write(",");
                        bufWriter.write(data.get(key).get(nation).getDateCount().get(i)+"");
                        bufWriter.write(",");
                        bufWriter.newLine();
                    }

                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(br != null){
                    br.close();
                }
                if(bufWriter != null){
                    bufWriter.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }
}
