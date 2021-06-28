package com.barchartfromexcel.ExcelBarChart;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojo.Model;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class Main {
	 static List<Model> modelobj = new ArrayList<Model>();


    public static void main(String[] args) throws IOException {
        excel();
        System.out.println(modelobj.get(0).getCusip());
        ArrayList<Model> aggreagtenow = aggreagtenow(modelobj);
        System.out.println(aggreagtenow);
        writeFileUsingPOI(aggreagtenow);

    }

    private static ArrayList<Model> aggreagtenow(List<Model> modelobj) {

            Map<String, Model> grpMap = new HashMap<String, Model>();

            for (Model model : modelobj) {
                int count=1;
                String key =  model.getIcccode()+ model.getCusip();
                if (grpMap.containsKey(key)) {
                    Model grpdPerson = grpMap.get(key);
                    grpdPerson.setReceiveramount(grpdPerson.getReceiveramount() + model.getReceiveramount());
                    grpdPerson.setAggreagtcount(count+1);
                    //grpdPerson.setMark2(grpdPerson.getMark2() + model.getMark2());
                    //grpdPerson.setMark3(grpdPerson.getMark3() + model.getMark3());
                } else {
                    grpMap.put(key, model);
                }
            }
            return new ArrayList<Model>(grpMap.values());
        }

    public static <E> void excel() throws IOException {
        {
            /* Read the bar chart data from the excel file */
            FileInputStream chart_file_input = new FileInputStream(new File("barChart.xlsx"));
                /* HSSFWorkbook object reads the full Excel document. We will manipulate this object and
                write it back to the disk with the chart */
            XSSFWorkbook my_workbook = new XSSFWorkbook(chart_file_input);
            /* Read chart data worksheet */
            XSSFSheet my_sheet = my_workbook.getSheetAt(0);
            /* Create Dataset that will take the chart data */

            /* We have to load bar chart data now */
            /* Begin by iterating over the worksheet*/
            /* Create an Iterator object */

           
            Iterator<Row> rowIterator = my_sheet.iterator();
            /* Loop through worksheet data and populate bar chart dataset */
            String chart_label="a";
            Number chart_data=0;
            boolean flag=false;
            while(rowIterator.hasNext())
            {
                if(flag == true) {
                    Model model = new Model();

                    Row row = rowIterator.next();
                    model.assignEmployee(row);
                    modelobj.add(model);

                }
                else
                {
                    flag=true;
                    Row row = rowIterator.next();
                }
            }

            /* Write changes to the workbook */
            FileOutputStream out = new FileOutputStream(new File("barChart.xls"));
            my_workbook.write(out);
            out.close();
            my_workbook.close();

        }
    }


    public static void writeFileUsingPOI(ArrayList<Model> aggreagtenow) throws IOException
    {
        //create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Country");



        //Iterate over data and write to sheet
        int rownum = 0;
        for (Model model : aggreagtenow)
        {
            Row row = sheet.createRow(rownum++);

            int cellnum = 0;
            for (int i=1;i<=6;i++)
            {
                Cell cell = row.createCell(cellnum++);
                if(model.getIcccode() instanceof String && i==1)
                    cell.setCellValue((String) model.getIcccode());
                else if((Integer)model.getReceiveramount() instanceof Integer && i==2)
                    cell.setCellValue((Integer) model.getReceiveramount());
                else if((Integer)model.getSenderamount() instanceof Integer && i==3)
                    cell.setCellValue((Integer) model.getSenderamount());
                else if(model.getCusip() instanceof String && i==4)
                    cell.setCellValue((String) model.getCusip());
                else if(model.getFxtrade() instanceof String && i==5)
                    cell.setCellValue((String) model.getFxtrade());
                else if((Integer)model.getAggreagtcount() instanceof Integer && i==6)
                    cell.setCellValue((Integer) model.getAggreagtcount());
            }
        }
        try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("CountriesDetails.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("CountriesDetails.xlsx has been created successfully");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            workbook.close();
        }
    }

}



