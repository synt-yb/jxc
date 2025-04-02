package com.example.demo.config.iText5;

import com.example.demo.entity.Inventory;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfTableBuilder {

    // 字体: STSong-Light 允许显示中文
    public static final BaseFont baseFont;

    static {
        try {
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PdfPCell> addInventoryTable(List<String> cells, int rowNum) {
        // 设置数据样式
        Font dataFont = new Font(baseFont, 12);
        // 创建单元格
        List<PdfPCell> cellList = new ArrayList<>();
        for (String data : cells){
            PdfPCell cell = new PdfPCell(new Phrase(data, dataFont));
            cell.setPadding(8f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellList.add(cell);

        }
        // 前三项合并
        for (int i = 0; i <= 2; i++) {
            cellList.get(i).setRowspan(rowNum);
        }
        return cellList;
    }

    public static PdfPTable inventoryTable(List<Inventory> inventoryList) {

        // 创建表格
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100); // 表格宽度占满页面
        table.setSpacingBefore(20f); // 表格距离上方的距离

        // 设置表头样式
        Font headerFont = new Font(baseFont, 12, Font.BOLD, BaseColor.WHITE);// 表头字体样式：大小为12，粗体，白色
        BaseColor headerBgColor = new BaseColor(79, 129, 189); // 表头背景色（深蓝色）

        // 添加表头单元格
        String[] headers = {"物料名称", "类型", "规格", "购入价/元", "数量/件", "生产日期", "保质期/天"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER); // 居中对齐
            cell.setBackgroundColor(headerBgColor); // 设置背景色
            cell.setPadding(8f); // 单元格内边距
            table.addCell(cell);
        }

        // 处理表格数据
        int rowNum=1;
        List<PdfPCell> cellList = new ArrayList<>();// 存储单元格对象的列表
        String beforeMaterial_id=null; //记录上一条记录的物料id
        List<String> rowDataTemp=null; // 存储上一条记录的数据
        for (Inventory item : inventoryList){
            // 存储临时数据行
            if (beforeMaterial_id==null){//初次进入
                beforeMaterial_id=item.getMaterial_id();
                rowDataTemp=item.getAllInventoryInfo();
            }else{
                if (beforeMaterial_id.equals(item.getMaterial_id())){//如果和之前的数据id相同,则合并
                    rowNum++;
                    rowDataTemp.addAll(item.getPartInventoryInfo());
                }else{
                    cellList.addAll(addInventoryTable(rowDataTemp,rowNum));
                    rowNum=1;
                    beforeMaterial_id=item.getMaterial_id();
                    rowDataTemp=item.getAllInventoryInfo();
                }
            }
        }
        if (rowDataTemp!=null){
            cellList.addAll(addInventoryTable(rowDataTemp,rowNum));
        }else{
            PdfPCell emptyCell = new PdfPCell(new Phrase("无库存数据", new Font(baseFont, 12)));
            emptyCell.setColspan(7);
            emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(emptyCell);
            return table;
        }

        // 添加数据行
        cellList.forEach(table::addCell);

        // 返回表格
        return table;
    }
}
