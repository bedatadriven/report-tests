package com.bedatadriven;

import com.google.common.io.Resources;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class ArabicPdfTest {

    @BeforeEach
    public void loadFonts() throws IOException {
    }

    @Test
    public void test() throws IOException {
        Document document = new Document(PageSize.A4);
        FileOutputStream out = new FileOutputStream("arabic.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setStrictImageSequence(true);

        String arabicText = "مرحبا العالم العربي الجديد الشجاع.";

        BaseFont baseFont = loadFont("NotoSans-Regular.ttf");
        Font font = new Font(baseFont, 10, Font.NORMAL, Color.BLACK);

        Paragraph paragraph = new Paragraph(10, arabicText, font);

        document.open();
        document.add(paragraph);
        document.close();
        out.close();
    }


    private static BaseFont loadFont(String name) throws IOException {
        URL resource = Resources.getResource(name);
        byte[] bytes = Resources.toByteArray(resource);
        boolean cached = true;
        return BaseFont.createFont(name, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, cached, bytes, null);
    }

}
