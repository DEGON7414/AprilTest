package test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import utils.CreateReceipt;
import utils.OrderUtils;
import utils.PDFReaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * ClassName: Reader
 * Description:
 * 讀取PDF並提取出編號、金額
 * 將資料轉為收據PDF並匯出
 *
 * @Author 許記源
 * @Create 2025/4/28 下午 02:11
 * @Version 1.0
 */
public class ReaderTest {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\考題.pdf";
        String outputFilePath = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\export_combined.pdf";
        String imagePath = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\receipt_signature.bmp";

        try {
            //1.提取表格
            List<PDPage> pdPages = OrderUtils.extractReceiptPages(filePath);
            //確保是偶數
            if (pdPages.size() % 2 != 0) {
                System.out.println("警告：PDF頁數不是偶數，最後一頁可能無法正確處理");
            }

            // 使用try-with-resources來確保資源自動關閉
            try (PDDocument sourceDocument = PDDocument.load(new File(filePath));
                 PDDocument combinedDocument = new PDDocument()) {

                int processedCount = 0;

                // 遍歷每兩頁一組
                for (int i = 0; i < pdPages.size() - 1; i += 2) {
                    PDPage img = pdPages.get(i); // 這是圖片頁（假設為第一頁）
                    PDPage tablePage = pdPages.get(i + 1); // 這是表格頁（假設為第二頁）

                    //2. 從第二頁（表格頁）提取文本
                    String tableText = PDFReaderUtils.extractTextFromPdf(filePath, i + 2, i + 2); // 提取第二頁文本

                    //3. 提取條碼編號
                    String barcodeNumber = PDFReaderUtils.extractBarcodeNumber(tableText);
                    System.out.println("條碼編號是: " + barcodeNumber);

                    //4. 提取實付金額
                    String payment = PDFReaderUtils.paymentAmount(tableText);
                    System.out.println("實付金額是: " + payment);

                    // 添加這筆資料的收據到合併文檔
                    CreateReceipt.addReceiptToDocument(combinedDocument, barcodeNumber, "2025年4月28日", payment, imagePath);
                    processedCount++;

                    System.out.println("已處理 " + processedCount + " 筆資料");
                }

                // 保存合併後的文檔
                combinedDocument.save(outputFilePath);

                System.out.println("已成功創建包含所有 " + processedCount + " 筆收據的PDF文件：" + outputFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("處理PDF時發生錯誤: " + e.getMessage(), e);
        }
    }
}
