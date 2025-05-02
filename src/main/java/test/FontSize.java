package test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.io.IOException;

/**
 * ClassName: FontSize
 * Description:
 *
 * @Author 許記源
 * @Create 2025/5/2 下午 01:16
 * @Version 1.0
 */
public class FontSize extends PDFTextStripper {
    public FontSize() throws IOException{
        super();
    }
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\cxhil\\Desktop\\kevin0427考題\\考題.pdf"); // 替換為你的PDF檔案路徑
        try (PDDocument document = PDDocument.load(file)) {
            FontSize stripper = new FontSize();
            stripper.setSortByPosition(true);
            stripper.getText(document);
        }
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        String character = text.getUnicode();
        float fontSize = text.getFontSizeInPt();
        System.out.printf("文字: %s, 字體大小: %.2f pt%n", character, fontSize);
    }
}
