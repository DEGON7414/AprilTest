package test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * ClassName: ReceiptTest
 * Description:
 *
 * @Author 許記源
 * @Create 2025/5/2 上午 09:22
 * @Version 1.0
 */
public class ReceiptTest {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\考題.pdf";
        String outputFilePath = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\export_combined.pdf";
        String imagePath = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\receipt_signature.bmp";
        String date = "2025年5月2日";
        try {
            List<PDPage> pdPages = OrderUtils.extractReceiptPages(filePath);
            //確保是偶數
            if (pdPages.size() % 2 != 0) {
                System.out.println("警告：PDF頁數不是偶數，最後一頁可能無法正確處理");
            }
            // 使用try-with-resources來確保資源自動關閉
            try (PDDocument sourceDocument = PDDocument.load(new File(filePath));
            ) {

                int processedCount = 0;

                // 遍歷每兩頁一組
                for (int i = 0; i < pdPages.size() - 1; i += 2) {
                    //2. 從第二頁（表格頁）提取文本
                    String tableText = PDFReaderUtils.extractTextFromPdf(filePath, i + 2, i + 2); // 提取第二頁文本

                    //3. 提取條碼編號
                    String barcodeNumber = PDFReaderUtils.extractBarcodeNumber(tableText);
                    System.out.println("條碼編號是: " + barcodeNumber);

                    //4. 提取實付金額
                    String payment = PDFReaderUtils.paymentAmount(tableText);
                    System.out.println("實付金額是: " + payment);
                    //5.寫入
                    TextAppenderUtils.addText(sourceDocument, i + 1, date, barcodeNumber, payment);

                    processedCount++;

                    System.out.println("已處理 " + processedCount + " 筆資料");

                    File picFile = new File(imagePath);
                    double degree = 270;
                    File rotatedImage = PicInsert.rotateImage(picFile, degree);

                    // 接著插入圖片
                    PDPage targetPage = sourceDocument.getPage(i + 1);

                    PicInsert.insertPic(sourceDocument,targetPage,rotatedImage);
                }

                // 保存合併後的文檔
                sourceDocument.save(outputFilePath);

                System.out.println("已成功創建包含所有 " + processedCount + " 筆收據的PDF文件：" + outputFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("處理PDF時發生錯誤: " + e.getMessage(), e);

        }
    }
}