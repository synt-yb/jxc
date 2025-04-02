package com.example.demo.config.iText5;

import com.example.demo.entity.Inventory;
import com.itextpdf.text.*;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfUtil {

    /**
     * 生成库存pdf
     * @param title 标题
     * @param inventoryList 库存表信息
     * @return 字节数组
     */
     public byte[] createInventoryPdf(String title, List<Inventory> inventoryList) {
         try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
             // 初始化文档核心
             Document doc = PdfComponentBuilder.initDocument(stream);
             // 添加标题段落
             doc.add(PdfComponentBuilder.buildTitle(title));
             // 添加表格
             doc.add(PdfTableBuilder.inventoryTable(inventoryList));
             doc.close();
             return stream.toByteArray();
         } catch (Exception e) {
             e.printStackTrace();
             System.out.println("生成pdf失败");
             return null;
         }
    }
}
