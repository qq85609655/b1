package com.gtafe.data.center.xml;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataStandardMain {
    static String xlsPath = "D:/table/";
    static String sqlPath = "D:/table/sql/";
    static DataFileVo[] files = new DataFileVo[] {
        new DataFileVo("1002", "JYT1002_教育管理基础信息.xlsx", "6", "gta_jc"),
        new DataFileVo("1004", "JYT1004_普通中小学校管理信息.xlsx", "6" ,"gta_xx"),
        new DataFileVo("1005", "JYT1005_中职学校管理信息.xlsx", "6","gta_zz"),
        new DataFileVo("1006", "JYT1006_高等学校管理信息.xlsx", "5","gta_gx"),
    };
    
    public static void main(String[] args) throws Exception{
        handleFile(files[0]);
        
        handleFile(files[3]);
    }
    
    
    static String regex_subset = "(\\.[0-9]+){1}\\s+.*$";
    static String regex_class = "(\\.[0-9]+){2}\\s+.*$";
    static String regex_subclass = "(\\.[0-9]+){3}\\s+.*$";
    public static void handleFile(DataFileVo vo )throws Exception{
        String[] regexs = new String[] {
            "^\\s*"+vo.getStart()+ regex_subset,
            "^\\s*"+vo.getStart()+ regex_class,
            "^\\s*"+vo.getStart()+ regex_subclass,
        }; 
        Workbook wb = new XSSFWorkbook(xlsPath + vo.getName());
        Sheet sheet = wb.getSheetAt(0);
        int length = sheet.getLastRowNum() + 1;
        boolean startFlag = false;
        
        
        List<DataCodeInfo> codeList = new ArrayList<DataCodeInfo>();
        DataCodeInfo rootCode = new DataCodeInfo();
        rootCode.setCode("1");
        rootCode.setNodeType(0);
        DataCodeInfo subset = null;
        DataCodeInfo clazz = null;
        DataCodeInfo subclass = null;
        for(int i=50;i<length;i++) {
            Cell cell = sheet.getRow(i).getCell(0);
            if(cell == null) {
                exit("第"+i+"行第一个cell为空！");
            }
            String value = getText(cell);
            if(!startFlag) {
                if(value.startsWith(vo.getStart()) && value.matches("^\\s*"+vo.getStart()+"+\\s+数据集\\s*$")) {
                    startFlag = true;
                    System.out.println("第"+i+"行进入数据集！");
                    continue;
                }
                continue;
            }
            if(value.replaceAll("\\s", "").equals("编号")) {
                //特殊出来无数据子类行的数据，但要求当前类下没有其他数据子类
                if(subclass == null && clazz!=null) {
                    
                    String name = clazz.getCodeName();
                    if(name.endsWith("类")) {
                        name = name.substring(0, name.length()-1) + "子类";
                    }
                    String code = clazz.getCode()+"01";
                    value = vo.getStart()+".1.1.1 "+code+" "+name;
                    i = i-1;
                    System.out.println("--------------------------------------------补充数据子类，"+code+"\t"+name);
                }else {
                    exit("第"+i+"行存在编号文字，但未归属于到任意子类！");
                }
            }
            if(value.matches(regexs[0])) {
                String[] values = splitBlank(value);
                if(values.length != 3) {
                    exit("第"+i+"行数据子集格式错误！");
                }
                DataCodeInfo codeInfo = createCodeInfo(i, values, 1, rootCode, null);
                subset = codeInfo;
                clazz = null;
                subclass = null;
                codeList.add(codeInfo);
                continue;
            }
            
            if(value.matches(regexs[1])) {
                String[] values = splitBlank(value);
                if(values.length != 3) {
                    exit("第"+i+"行数据类格式错误！");
                }
                DataCodeInfo codeInfo = createCodeInfo(i, values, 2, subset, null);
                clazz = codeInfo;
                subclass = null;
                codeList.add(codeInfo);
                continue;
            }
            
            if(value.matches(regexs[2])) {
                String[] values = splitBlank(value);
                if(values.length != 3) {
                    exit("第"+i+"行数据子类格式错误！");
                }
                DataCodeInfo codeInfo = createCodeInfo(i, values, 3, clazz, vo.getTablePrefix()+ "_"+subset.getCode());
                subclass = codeInfo;
                codeList.add(codeInfo);
                int newI = handleTableField(i, sheet, codeInfo, regexs);
                System.out.println("创建表["+ codeInfo.getTableName() +"]共"+codeInfo.getFields().size()+"个字段！"+codeInfo.getCodeName());
                i = newI;
                continue;
            }
            continue;
        }
        wb.close();
        if(codeList.isEmpty()) {
            exit(vo.getName()+" 没有有效的结果！");
        }else {
            int count = outputToSql(codeList, sqlPath + vo.id+".sql");
            System.out.println("共输出"+ count +"个有字段的表！"+vo.getId()+"\t"+vo.getName());
        }
    }
    
    static DataCodeInfo createCodeInfo(int i, String[] values, int nodeType, DataCodeInfo parent, String tablePrefix){
        if(parent == null) {
            exit("第"+i+"行数据节点类型和父节点类型不匹配！");
        }
        DataCodeInfo info = new DataCodeInfo();
        info.setCode(values[1]);
        info.setCodeName(values[2]);
        info.setParentCode(parent.getCode());
        info.setDescription("");
        info.setNodeType(nodeType);
        if(nodeType == 3) {
            String codeName= removeSpecialChar(info.getCodeName());
            if(!codeName.matches("^[\\u4e00-\\u9fa5]+$")) {
                exit("第"+i+"行数据节点名称仅支持中文(去除部分特殊字符后)！");
            }
            String pin =  PinYinUtil.getFirstSpell(codeName);
            info.setTableName(tablePrefix + "_" + pin);
            info.setTableName(info.getTableName().toUpperCase());
        }else {
            info.setTableName("");
        }
        return info;
    }
    
    static String chars = "()（）、";
    static String removeSpecialChar(String codeName){
        for(char c : chars.toCharArray()) {
            codeName = codeName.replace(c+"", "");
        }
        return codeName;
    }
    
    static int handleTableField(int ii, Sheet sheet, DataCodeInfo codeInfo,String[] regexs){
        if(codeInfo.getNodeType()!=3) return ii;
        codeInfo.setFields(new ArrayList<DataCodeFieldInfo>());
        
        int length = sheet.getLastRowNum() + 1;
        int i = ii+1;
        boolean fieldFlag = false;
        int[] indexs = null;
        for(;i<length;i++) {
            Cell cell = sheet.getRow(i).getCell(0);
            if(cell == null) {
                exit("第"+i+"行第一个cell为空！");
            }
            String value = cell.getStringCellValue().trim();
            if(value.replaceAll("\\s", "").equals("编号")) {
               indexs = checkNumRow(i, sheet.getRow(i));
               fieldFlag = true;
               continue;
            }
            //字段列表中存在空行，忽略空行
            if(value.isEmpty()) {
                System.out.println("----------------------------------------------第"+i+"行为空行，可能存在多行单元格合并问题！");
                continue;
            }
            if(value.matches("^\\s*[0-9a-zA-Z\\-\\_]+\\s*$")) {
                //有效的字段，取值 
                Row row = sheet.getRow(i);
                DataCodeFieldInfo field = new DataCodeFieldInfo();
                field.setItemCode(getText(row.getCell(indexs[0])).trim());
                field.setItemName(getText(row.getCell(indexs[1])).trim());
                field.setItemComment(getText(row.getCell(indexs[2])).trim());
                field.setDataType(getText(row.getCell(indexs[3])).trim());
                field.setDataLength(getText(row.getCell(indexs[4])).trim());
                field.setPrimarykey(0);
                field.setNullable(getText(row.getCell(indexs[5])).trim());
                field.setExplain(getText(row.getCell(indexs[7])).trim());
                field.setReferenced(getText(row.getCell(indexs[8])).trim());
                codeInfo.getFields().add(field);
                field.setItemName(field.getItemName().replaceAll("\\s", ""));
                continue;
            }
            if(value.matches(regexs[0]) || value.matches(regexs[1]) || value.matches(regexs[2])) {
                return i - 1;
            }
            if(fieldFlag == true) {
                //已经有字段添加后，出现非编号和字段行，则表示当前行表结束
                return i - 1;
            }
        }
        return i-1;
    }
    
    static String[] fieldTheads = new String[] {"编号","数据项名","中文简称","类型","长度","约束","值空间","解释/举例","引用编号"};
    static int[] checkNumRow(int index, Row row){
        int[] indexs = new int[fieldTheads.length];
        int cellIndex = 0;
        int length = row.getLastCellNum();
        for(int i=0;i<fieldTheads.length;i++) {
            
            boolean flag = false;
            for(int j=cellIndex;j<length;j++) {
                Cell cell = row.getCell(j);
                if(cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    continue;
                }
                String v = getText(cell).trim();
                v = v.replaceAll("\\s", "");
                if(v.equals(fieldTheads[i])) {
                    flag = true;
                    indexs[i] = j;
                    cellIndex = j+1;
                    break;
                }else {
                    exit("第"+index+"表头格式错误,"+v+"不匹配["+ fieldTheads[i] +"]!");
                }
            }
            if(flag) {
                continue;
            }else {
                exit("第"+index+"表头格式错误,["+ fieldTheads[i] +"]未匹配!");
            }
        }
        return indexs;
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
    
    
    static int outputToSql(List<DataCodeInfo> codeList,String fileName)throws Exception{
        String code = "INSERT INTO info_datastandard_org(code,parent_code,code_name,description" + 
            ",tablename,sourceid,node_type" + 
            ",creater,createtime,updater,updatetime) values ";
        String item = "INSERT INTO info_datastandard_item(" + 
            "subclass_code,item_code,item_name,item_comment," + 
            "data_type,data_length,data_primarykey,data_nullable," + 
            "data_value_source,data_explain,data_referenced,selectable," + 
            "creator,createtime,updater,updatetime," + 
            "sourceid ) values ";
        BufferedWriter bw = null;
        try {
            int count = 0;
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, false), "UTF-8"));
            bw.write("\r\n\r\n");
            StringBuffer sql = new StringBuffer();
            for(DataCodeInfo info : codeList) {
                sql.setLength(0);
                sql.append(" (");
                sql.append("'").append(info.getCode()).append("',");
                sql.append("'").append(info.getParentCode()).append("',");
                sql.append("'").append(info.getCodeName()).append("',");
                sql.append("'").append(info.getDescription()).append("',");
                sql.append("'").append(info.getTableName()).append("',");
                sql.append("'1',");
                sql.append("'").append(info.getNodeType()).append("',");
                sql.append("'1',now(),'1',now()");
                sql.append(");\r\n");
                bw.write(code);
                bw.write(sql.toString());
                if(info.getNodeType() == 3 && !info.getFields().isEmpty()) {
                    count ++;
                    sql.setLength(0);
                    sql.append("\r\n");
                    int i=0;
                    List<String> existsFields = new ArrayList<String>();
                    for(DataCodeFieldInfo field : info.getFields()) {
                        if(existsFields.contains(field.getItemName().toLowerCase())) {
                            //过滤重复的字段
                            continue;
                        }else {
                            existsFields.add(field.getItemName().toLowerCase());
                        }
                        if(i>0) {
                            sql.append(",\r\n");
                        }
                        sql.append("(");
                        sql.append("'").append(info.getCode()).append("',");
                        sql.append("'").append(field.getItemCode()).append("',");
                        sql.append("'").append(field.getItemName()).append("',");
                        sql.append("'").append(field.getItemComment()).append("',");
                        sql.append("'").append(field.getDataType()).append("',");
                        sql.append("'").append(field.getDataLength()).append("',");
                        sql.append("'").append(field.getPrimarykey()).append("',");
                        sql.append("'").append(field.getNullable()).append("',");
                        sql.append("'',");
                        sql.append("'").append(field.getExplain()).append("',");
                        sql.append("'").append(field.getReferenced()).append("',");
                        sql.append("'").append(field.getNullable() == 0 ? "M" : "O").append("',");
                        sql.append("'1',now(),'1',now(),1");
                        sql.append(")");
                        i++;
                    }
                    sql.append(";\r\n\r\n\r\n");
                    bw.write(item);
                    bw.write(sql.toString());
                }
                
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
    
    
    static String getText(Cell cell) {
        if(cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue() + "";
        }
        return cell.getStringCellValue();
    }
}
