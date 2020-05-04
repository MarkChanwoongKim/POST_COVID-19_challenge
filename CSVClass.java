import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

class NationData{
    private int count;

    public NationData(int count){
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public NationData addData(int count){
        this.count+=count;
        return this;
    }

}

class NationData_Final{
    private int incomeCount;
    private int[][] virusNewsCount;

    public NationData_Final(int incomeCount){
        this.incomeCount = incomeCount;
        this.virusNewsCount = new int[64][3];
        for(int i=0;i<64;i++){
            for(int j=0;j<3;j++){
                this.virusNewsCount[i][j]=0;
            }
        }
    }



    public NationData_Final addVirusNewsCount(int virusNum,int sentiment){
        this.virusNewsCount[virusNum][sentiment]++;
        return this;
    }
    public NationData_Final addIncomeCount(int count){
        this.incomeCount+=count;
        return this;
    }

    public int[][] getVirusNewsCount(){
        return this.virusNewsCount;
    }

    public int getIncomeCount(){
        return incomeCount;
    }
}

public class CSVClass {
    private static int atoi(String str) {return Integer.parseInt(str);}

    public static void main(String[] args) {
        BufferedReader br = null;
        BufferedWriter bufWriter = null;
        Map<Integer, Map<String, NationData>> data = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Map<String,String> nationNameMap = new TreeMap<>();
        Map<String,ArrayList<String>> nationNameAndNews = new TreeMap<>();
        Map<Integer,int[]> importedData =new TreeMap<>();
        String nationName = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\nation_code.csv";
        String targetCSV = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\2. Roaming_data.csv";

        String nationNameWithNews = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\news_sort.csv";
        String importedDir = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\importdata";
        String newsDir[] = {"C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\data\\0","C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\data\\1","C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\data\\2"};


        String line;
        try{
            br = Files.newBufferedReader(Paths.get(nationName));
             line= "";

            br.readLine();
            while((line = br.readLine()) != null){
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                tmpList = Arrays.asList(array);
                nationNameMap.put(tmpList.get(2),tmpList.get(1));

            }


            br = Files.newBufferedReader(Paths.get(targetCSV));
            line = "";

            br.readLine();
            while((line = br.readLine()) != null){
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                tmpList = Arrays.asList(array);
                String nationFullName = nationNameMap.get(tmpList.get(1));



                if(!data.containsKey(atoi(tmpList.get(0)))){
                    Map<String, NationData> tmpNationData = new HashMap<>();
                    tmpNationData.put(nationFullName,new NationData(atoi(tmpList.get(4))));
                    data.put(atoi(tmpList.get(0)),tmpNationData);
                }else{
                    if(data.get(atoi(tmpList.get(0))).containsKey(nationFullName)){
                        data.get(atoi(tmpList.get(0))).get(nationFullName).addData(atoi(tmpList.get(4)));
                    }else{
                        data.get(atoi(tmpList.get(0))).put(nationFullName,new NationData(atoi(tmpList.get(4))));
                    }

                }

            }

            br = Files.newBufferedReader(Paths.get(nationNameWithNews));
            line = "";

            br.readLine();
            while((line = br.readLine()) != null){
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                tmpList = Arrays.asList(array);

                int countryCount = tmpList.size()-2;
                String countryArr[];
                if(countryCount>0){
                    String tmpStr = tmpList.get(2).replaceAll("[\\[,\\],\",']", "");
                    if(tmpStr!=""||tmpStr.length()!=0){
                        countryArr = new String[countryCount];
                        for(int i =0;i<countryCount;i++){
                            countryArr[i]=tmpList.get(2+i).replaceAll("[\\[,\\],\",']", "");
                        }

                        for(int i = 0;i<countryArr.length;i++){
                            if(nationNameAndNews.containsKey(tmpList.get(1))){
                                nationNameAndNews.get(tmpList.get(1)).add(countryArr[i]);
                            }else{
                                ArrayList<String> tmpArr = new ArrayList<>();
                                tmpArr.add(countryArr[i]);
                                nationNameAndNews.put(tmpList.get(1),tmpArr);
                            }
                        }

                    }
                }

            }

            Map<String,Integer> newsSentimentMap = new HashMap<>();

            for(int i =0;i<3;i++){
                File dir = new File(newsDir[i]);
                File[] fileList = dir.listFiles();

                for(File file : fileList) {
                    newsSentimentMap.put(file.getName(),i-1);
                }

            }

            File dir = new File(importedDir);
            File[] fileList = dir.listFiles();

            for(File file : fileList) {
                if (file.isFile()) {
                    //System.out.println(file.getName());

                    FileReader filereader = new FileReader(file);
                    BufferedReader bufReader = new BufferedReader(filereader);
                    line = bufReader.readLine();

                    String array[] = line.split(",");
                    int[] tmpIntArr = new int[64];
                    for(int i=0;i<62;i++){
                        tmpIntArr[i]=atoi(array[i+1]);
                    }
                    importedData.put(atoi(file.getName().substring(0,8)),tmpIntArr);
                }
            }

            Map<String,String> newsWithVirus = new HashMap<>();
            String newsWithVirusCsv = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\뉴스분석.csv";

            FileReader filereader = new FileReader(newsWithVirusCsv);
            BufferedReader bufReader = new BufferedReader(filereader);
            line = bufReader.readLine();

            while((line = bufReader.readLine()) != null){
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                tmpList = Arrays.asList(array);
                if(tmpList.size()>1){
                    newsWithVirus.put(tmpList.get(0),tmpList.get(1));
                }

            }

            Map<String,Integer> virusIndex = new HashMap<>();
            String virusIndexCsv = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\병명.csv";

            filereader = new FileReader(virusIndexCsv);
            bufReader = new BufferedReader(filereader);
            line = bufReader.readLine();
            line = bufReader.readLine();


            List<String> tmpList = new ArrayList<String>();
            String array[] = line.split(",");
            tmpList = Arrays.asList(array);
            for(int i=0;i<tmpList.size();i++){
                virusIndex.put(tmpList.get(i),i);
            }


            String nationIndex[] = new String[nationNameMap.size()];

            int index = 0;
            for(String s : nationNameMap.keySet()){
                nationIndex[index++]=nationNameMap.get(s);
            }

            Map<Integer,Map<String,NationData_Final>> finalNationIncomingAndNewsData = new TreeMap<>();

            for(int date : data.keySet()){
                Map<String,NationData_Final> tmpMap = new HashMap<>();
                for(String nation : data.get(date).keySet()){
                    tmpMap.put(nation,new NationData_Final(data.get(date).get(nation).getCount()));
                }
                finalNationIncomingAndNewsData.put(date,tmpMap);
            }

            for(String fileName : newsSentimentMap.keySet()){
                if(nationNameAndNews.containsKey(fileName)){
                    for(int i=0;i<nationNameAndNews.get(fileName).size();i++){
                        if(finalNationIncomingAndNewsData.containsKey(atoi(fileName.substring(4,12)))){
                            //System.out.println(fileName.substring(4,12)+"란 데이터 존재");
                            if(finalNationIncomingAndNewsData.get(atoi(fileName.substring(4,12))).containsKey(nationNameAndNews.get(fileName).get(i))&&virusIndex.containsKey(newsWithVirus.get(fileName.substring(0,17)))){
                                //System.out.println(fileName+" - "+newsSentimentMap.get(fileName)+" 나라: "+nationNameAndNews.get(fileName).get(i));
                                finalNationIncomingAndNewsData.get(atoi(fileName.substring(4,12))).get(nationNameAndNews.get(fileName).get(i)).addVirusNewsCount(virusIndex.get(newsWithVirus.get(fileName.substring(0,17))),newsSentimentMap.get(fileName)+1);
                            }
                        }

                    }
                }
            }





            data.get(0);



            String savePoint = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\시간_나라_뉴스_인구분석.csv";
            bufWriter = Files.newBufferedWriter(Paths.get(savePoint));
            bufWriter.write("시간");
            bufWriter.write(",");
            bufWriter.write("나라명_정보");
            bufWriter.write(",");
            bufWriter.newLine();

            for(Integer date : finalNationIncomingAndNewsData.keySet()){
                bufWriter.write(date+"");
                bufWriter.write(",");
                bufWriter.write("[");
                for(String nation : finalNationIncomingAndNewsData.get(date).keySet()){
                    if(nation==""||nation==null){
                        continue;
                    }
                    bufWriter.write(nation+":"+finalNationIncomingAndNewsData.get(date).get(nation).getIncomeCount());
                    bufWriter.write("[");
                    int tmp[][] = finalNationIncomingAndNewsData.get(date).get(nation).getVirusNewsCount();
                    for(int i =0;i<64;i++){
                        bufWriter.write("[");
                        for(int j=0;j<3;j++){
                            bufWriter.write(tmp[i][j]+"");
                            if(j!=2){
                                bufWriter.write("$");
                            }
                        }
                        bufWriter.write("]");
                        if(i!=63){
                            bufWriter.write("$");
                        }
                    }
                    bufWriter.write("]");
                    bufWriter.write(",");
                }
                bufWriter.newLine();
            }

            String importedVirus = "C:\\Users\\근무지원대\\Desktop\\새 폴더 (3)\\corona_contest_data_0406\\바이러스유입.csv";
            bufWriter = Files.newBufferedWriter(Paths.get(importedVirus));
            bufWriter.write("시간");
            bufWriter.write(",");
            for(int i =0;i<array.length-1;i++){
                bufWriter.write(array[i]);
                if(i!=array.length-2){
                    bufWriter.write(",");
                }
            }
            bufWriter.newLine();
            for(int date : importedData.keySet()){
                bufWriter.write(date+"");
                bufWriter.write(",");
                for(int i =0;i<importedData.get(date).length-1;i++){
                    bufWriter.write(importedData.get(date)[i]+"");
                    if(i!=importedData.get(date).length-2){
                        bufWriter.write(",");
                    }
                }
                bufWriter.newLine();
            }

            bufWriter.close();


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
