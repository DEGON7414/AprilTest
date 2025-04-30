package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;

/**
 * ClassName: TextAppenderUtils
 * Description:
 *
 * @Author 許記源
 * @Create 2025/4/30 下午 02:26
 * @Version 1.0
 */
public class TextAppenderUtils {
    public static boolean addText(PDDocument document) {
        try {
            int numberOfPages = document.getNumberOfPages();
            for (int i = 0; i < numberOfPages; i += 2) {
                if (i + 1 < numberOfPages) {
                    PDPage page = document.getPage(i + 1);
                    PDRectangle mediaBox = page.getMediaBox();
                    float width = mediaBox.getWidth();
                    float height = mediaBox.getHeight();
                    float x = width / 2 - 100;
                    float y = height / 4;
                    //建立內容流 加入文字
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
                    ) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        //旋轉90度的矩陣 平移到XY再旋轉
                        Matrix rotateInstance = Matrix.getRotateInstance(Math.toRadians(90), x, y);
                        contentStream.setTextMatrix(rotateInstance);
                        contentStream.showText("Hello");
                        contentStream.endText();
                    }
                }
            }
            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
