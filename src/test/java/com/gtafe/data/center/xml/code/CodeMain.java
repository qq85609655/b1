package com.gtafe.data.center.xml.code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gtafe.data.center.xml.DataFileVo;
import com.gtafe.framework.base.utils.DateUtil;

public class CodeMain {
    static String xlsPath = "D:/table/code/";
    static String sqlPath = "D:/table/code/sql/";
    static DataFileVo[] files = new DataFileVo[] {
        new DataFileVo("A", "A.xlsx", "", ""),
        new DataFileVo("B", "B.xlsx", "" ,""),
        new DataFileVo("B2", "B2.xls", "" ,""),
        
        new DataFileVo("C", "C.xlsx", "",""),
        new DataFileVo("D", "D.xlsx", "",""),
        new DataFileVo("B3", "B3.xlsx", "" ,"")
    };
    
    public static void main(String[] args) throws Exception{
        init();
        for(DataFileVo f : files) {
        	handleFile(f);
        }
        System.out.println("生产代码sql成功完成！共"+ totalCount +"个！");
    }
    
    static Map<String,String> nodeMap = new HashMap<String,String>();
    static void init() {
        InputStream in = CodeMain.class.getResourceAsStream("code.properties");
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(in, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String key : prop.stringPropertyNames()){
            nodeMap.put(prop.getProperty(key).trim(), key);
        }
        File file = new File(sqlPath + "code.sql");
        if(file.exists() && file.isFile()) {
        	file.delete();
        }
    }
    
    static int totalCount = 0;
    static boolean needDesc = true;
    public static void handleFile(DataFileVo vo )throws Exception{
        if(vo.getId().equalsIgnoreCase("A")) {
            needDesc = false;
        }else {
            needDesc = true;
        }
        long time = System.currentTimeMillis();
        System.out.println("开始打开文件"+vo.getName());
        Workbook wb = new XSSFWorkbook(xlsPath + vo.getName());
        
        System.out.println("完成打开文件"+vo.getName()+"，用时"+(System.currentTimeMillis() - time));
        Iterator<Sheet> it = wb.sheetIterator();
        while(it.hasNext()) {
            handleSheet(vo, it.next());
        }
        wb.close();
    }
   
    
    public static void handleSheet(DataFileVo vo, Sheet sheet) throws Exception{
        String sheetName = sheet.getSheetName().trim();
        boolean codeInnerTrim = false;
        if("统计用区划代码和城乡划分代码".equals(sheetName)) {
        	codeInnerTrim = true;
        }
        String nodeId = "";
        if(nodeMap.containsKey(sheetName)) {
            nodeId = nodeMap.get(sheetName);
        }else {
            error(vo, sheetName, 0, "代码类节点不存在！");
        }
        int lastRowNum = sheet.getLastRowNum();
        List<CodeTempInfo> codeList = new ArrayList<CodeTempInfo>();
        
        String code0 = getText(sheet.getRow(0).getCell(0));
        String name0 = getText(sheet.getRow(0).getCell(1));
        if(!code0.equals("代码") || !name0.equals("名称")) {
            error(vo, sheetName, 0, "格式错误，代码名称不完整！");
        }
        if("学科分类代码".equals(sheetName)) {
        	System.out.println(111);
        }
        
        for(int i=0;i<lastRowNum;i++) {
            Row row = sheet.getRow(i);
            if(row == null) continue;
            String code = getText(row.getCell(0));
            String name = getText(row.getCell(1));
            if(code.isEmpty() && name.isEmpty()) {
                continue;
            }
            if(code.equals("代码")) {
                if(name.equals("名称")) {
                    continue;
                }else {
                    error(vo, sheetName, i, "格式错误，代码名称不完整！");
                }
            }
            if(code.isEmpty()) {
                error(vo, sheetName, i, "代码列（第一列）不能为空！");
            }
            if(codeInnerTrim) {
            	code = code.replaceAll("\\s", "");
            }
            CodeTempInfo info = new CodeTempInfo();
            info.setCode(code);
            info.setName(name);
            info.setDescription("");
            if(needDesc) {
                Cell c2 = row.getCell(2);
                info.setDescription(c2==null ? "" : getText(c2));
            }
            info.setNodeId(nodeId);
            codeList.add(info);
        }
        int size = codeList.size();
        removeRepeat(codeList);
        log(vo, sheetName, "有效代码共"+ codeList.size() +"个，共重复了"+ (size-codeList.size()) +"个！");
        outputToSql(codeList);
        totalCount += codeList.size();
    }
    
    private static void removeRepeat(List<CodeTempInfo> codeList) {
    	Set<String> set = new HashSet<String>();
    	Iterator<CodeTempInfo> it = codeList.iterator();
    	while(it.hasNext()) {
    		CodeTempInfo info = it.next();
    		if(set.contains(info.getCode())) {
    			it.remove();
    		}else {
    			set.add(info.getCode());
    		}
    	}
    }
    
    
    
    static String[] splitBlank(String str){
        if(str == null) {
            return new String[0];
        }
        String[] strs = str.split("\\s");
        List<String> list=  new ArrayList<String>();
        for(String s : strs) {
            if(s.length()>0) {
                list.add(s);
            }
        }
        return list.toArray(new String[list.size()]);
    }
    
    public static void exit(String msg) {
        throw new RuntimeException(msg);
    }
    
    static int outputToSql(List<CodeTempInfo> codeList)throws Exception{
    	if(codeList==null || codeList.isEmpty()) {
    		return 0;
    	}
        String code = "INSERT INTO info_codestandard_code(node_id,code,name,description" + 
            ",sourceid" + 
            ",creater,createtime,updater,updatetime) values \r\n";
        BufferedWriter bw = null;
        try {
            int count = 0;
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sqlPath + "code.sql", true), "UTF-8"));
            bw.write("\r\n\r\n");
            StringBuffer sql = new StringBuffer();
            int codeSize = 0;
            for(CodeTempInfo info : codeList) {
                if(codeSize>0) {
                    sql.append(",\r\n");
                }
                sql.append(" (");
                sql.append("'").append(info.getNodeId()).append("',");
                sql.append("'").append(textEncode(info.getCode())).append("',");
                sql.append("'").append(textEncode(info.getName())).append("',");
                sql.append("'").append(textEncode(info.getDescription())).append("',");
                sql.append("'1',");
                sql.append("'1',now(),'1',now()");
                sql.append(")");
                codeSize++;
                count++;
                if(codeSize >= 50) {
                    sql.append(";\r\n\r\n");
                    bw.write(code);
                    bw.write(sql.toString());
                    sql.setLength(0);
                    codeSize = 0;
                }
            }
            if(codeSize > 0) {
                sql.append(";\r\n\r\n");
                bw.write(code);
                bw.write(sql.toString());
                sql.setLength(0);
                codeSize = 0;
            }
            bw.flush();
            bw.close();
            bw = null;
            return count;
        }finally {
            if(bw!=null) {
                bw.flush();
                bw.close();
            }
        }
    }
    
    public static String textEncode(String str){
    	return str.replace("'", "");
    }
    
    static String getText(Cell cell) {
        if(cell==null)return "";
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            String cellValue = "";
            String foramt = cell.getCellStyle().getDataFormatString();
            if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
                cellValue =    DateUtil.formatDateByFormat(cell.getDateCellValue(), foramt);
            } else {  //否
            	if(foramt.equalsIgnoreCase("General")) {
            		foramt = "";
            	}
                cellValue = new DecimalFormat(foramt).format(cell.getNumericCellValue()); 
            } 
            return cellValue.trim();
        }
        return cell.getStringCellValue().trim();
    }
    
    
    static void error(DataFileVo vo, String sheetName,int i, String msg){
        throw new RuntimeException("文件["+vo.getName()+"]sheet["+sheetName+"]第"+(i+1)+"行"+msg);
    }
    
    static void log(DataFileVo vo, String sheetName, String msg){
        System.out.println("文件["+vo.getName()+"]sheet["+sheetName+"]"+msg);
    }
}
