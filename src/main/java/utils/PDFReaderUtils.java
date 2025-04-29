package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.SystemColor.text;

/**
 * ClassName: PDFReader
 * Description:
 * 負責讀取PDF
 * 提取編號 金額
 *
 * @Author 許記源
 * @Create 2025/4/28 下午 01:25
 * @Version 1.0
 */
public class PDFReaderUtils {
    //讀取PDF文本
    public static String extractTextFromPdf(String filePath, int startPage, int endPage) throws IOException {
        File file = new File(filePath);
        try (PDDocument document = PDDocument.load(file);
        ) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setStartPage(startPage);
            pdfTextStripper.setEndPage(endPage);
            String text = pdfTextStripper.getText(document);
            return text;
        }

    }

    //提取條碼下的編號
    /*
     * @param text 從PDF提取出來全部的文本
     * @return 條碼編號
     * */
    public static String extractBarcodeNumber(String text) throws IOException {
        if (text == null || text.isEmpty()) {
            return null;
        }
        Pattern compile = Pattern.compile("\\d{2,}[A-Z0-9]{6,}");
        Matcher matches = compile.matcher(text);
        if (matches.find()) {
            return matches.group();
        }
        return null;
    }

    //提取實付金額
    public static String paymentAmount(String text) throws IOException {
        if (text == null || text.isEmpty()) {
            return null;
        }
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim().replaceAll("\\s+", " ");
            // 匹配全形和半形「金額」
            if (line.contains("實付金額") || line.contains("實付⾦額")) {
                String[] parts = line.split("：");
                if (parts.length > 1) {
                    return parts[1].split(" ")[0].trim(); // 提取金額，排除「總數」部分
                }
            }
        }
        return null;
    }
}