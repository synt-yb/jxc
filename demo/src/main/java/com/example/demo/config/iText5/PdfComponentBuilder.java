package com.example.demo.config.iText5;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfComponentBuilder {

    public static final BaseFont baseFont;

    static {
        try {
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 初始化文档配置（字体/边距）
    public static Document initDocument(ByteArrayOutputStream stream)
            throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(doc, stream);
        doc.open();
        return doc;
    }

    // 创建固定格式标题（中文字体+居中）
    public static Paragraph buildTitle(String text) throws IOException {
        Font font =new Font(baseFont, 18, Font.BOLD, BaseColor.BLACK);
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }

    // 创建右上角时间标签（可选功能）
    public static Paragraph buildTimeLabel(String time) throws IOException {
        Font font = new Font(baseFont, 18, Font.BOLD, BaseColor.BLACK);
        Paragraph p = new Paragraph("生成时间：" + time, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        return p;
    }
}