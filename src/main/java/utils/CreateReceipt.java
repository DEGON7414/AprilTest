package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * ClassName: PDFReader
 * Description:
 * 將提取到的資料 轉為收據PDF 並匯出
 *
 * @Author 許記源
 */
public class CreateReceipt {
    // 新增方法：將收據頁面添加到現有文檔
    public static void addReceiptToDocument(PDDocument pdDocument, String receiptNumber, String date, String paymentAmount, String imagePath) throws IOException {
        PDPage pdPage = new PDPage(PDRectangle.A4);
        pdDocument.addPage(pdPage);

        try (PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, pdPage)) {
            File fontFile = new File("C:\\Users\\cxhil\\Downloads\\Fonts_Kai\\TW-Kai-98_1.ttf");
            if (!fontFile.exists()) {
                System.out.println("字型文件不存在！");
                return;
            }
            PDType0Font chineseFont = PDType0Font.load(pdDocument, fontFile);

            // 設置標題和日期的起始位置
            float currentY = 750;
            float lineSpacing = 30; // 調整行間距

            // 計算置中的 X 座標
            String title = "國際電話卡收據";
            float titleWidth = chineseFont.getStringWidth(title) / 1000 * 36; // 字型寬度
            float titleX = (PDRectangle.A4.getWidth() - titleWidth) / 2;

            // 設置標題
            pdPageContentStream.setFont(chineseFont, 36);
            pdPageContentStream.beginText();
            pdPageContentStream.newLineAtOffset(titleX, currentY);
            pdPageContentStream.showText(title);
            pdPageContentStream.endText();

            // 計算日期的起始位置
            String dateText = date;
            float dateWidth = chineseFont.getStringWidth(dateText) / 1000 * 12; // 字型寬度
            float dateX = (PDRectangle.A4.getWidth() - dateWidth) / 2;

            // 設置日期
            currentY -= lineSpacing;
            pdPageContentStream.setFont(chineseFont, 22);
            pdPageContentStream.beginText();
            pdPageContentStream.newLineAtOffset(dateX, currentY);
            pdPageContentStream.showText(dateText);
            pdPageContentStream.endText();
            currentY -= lineSpacing;

            // 設置收據編號
            String receiptText = "收據編號: " + receiptNumber;
            float receiptX = 50; // 靠左顯示
            pdPageContentStream.setFont(chineseFont, 22);
            pdPageContentStream.beginText();
            pdPageContentStream.newLineAtOffset(receiptX, currentY);
            pdPageContentStream.showText(receiptText);
            pdPageContentStream.endText();
            currentY -= lineSpacing;

            // 買受人、統一編號、地址（靠左顯示）
            String buyerText = "買受人: [買受人姓名]";
            String taxIdText = "統一編號: [統一編號]";
            String addressText = "地址: [地址]";

            String[] buyerInfo = {buyerText, taxIdText, addressText};
            for (String text : buyerInfo) {
                pdPageContentStream.setFont(chineseFont, 22);
                pdPageContentStream.beginText();
                pdPageContentStream.newLineAtOffset(receiptX, currentY);
                pdPageContentStream.showText(text);
                pdPageContentStream.endText();
                currentY -= lineSpacing;
            }

            // 分隔線 1
            currentY -= 10; // 分隔線與文字間距
            pdPageContentStream.moveTo(50, currentY);
            pdPageContentStream.lineTo(550, currentY);
            pdPageContentStream.stroke();
            currentY -= 20; // 分隔線下方間距

            // 取得 PDF 頁面寬度
            float pageWidth = pdPage.getMediaBox().getWidth();

            // 品名
            pdPageContentStream.beginText();
            pdPageContentStream.setFont(chineseFont, 22);
            pdPageContentStream.newLineAtOffset(50, currentY);  // 品名的位置，靠左
            pdPageContentStream.showText("品名:");

            // 計算金額的位置，讓金額直接靠右
            float amountX = pageWidth - 50 - chineseFont.getStringWidth("金額: " + paymentAmount) / 1000 * 22;  // 金額的右邊位置

            // 金額
            pdPageContentStream.newLineAtOffset(amountX - 10, 0);  // 設置金額的起始位置，直接靠右
            pdPageContentStream.showText("金額:");
            pdPageContentStream.endText();

            // 更新 Y 位置，讓下一行顯示品名和金額
            currentY -= lineSpacing;  // 調整到下一行

            // 顯示品名的具體內容
            pdPageContentStream.beginText();
            pdPageContentStream.setFont(chineseFont, 22);
            pdPageContentStream.newLineAtOffset(50, currentY);  // 品名的位置，靠左
            pdPageContentStream.showText("出國上網設備及通信費用");

            // 顯示金額的具體內容
            float amountXNextLine = pageWidth - 50 - chineseFont.getStringWidth(paymentAmount) / 1000 * 22;  // 計算金額的具體內容寬度
            pdPageContentStream.newLineAtOffset(amountXNextLine - 50, 0);  // 金額右對齊
            pdPageContentStream.showText(paymentAmount);
            pdPageContentStream.endText();

            // 分隔線 2
            currentY -= 10;
            pdPageContentStream.moveTo(50, currentY);
            pdPageContentStream.lineTo(550, currentY);
            pdPageContentStream.stroke();
            currentY -= 20;

            // 總計
            String totalChinese = AmountToChinese.covertAmountToChinese(Integer.parseInt(paymentAmount));
            pdPageContentStream.setFont(chineseFont, 22);

            pdPageContentStream.beginText();
            pdPageContentStream.newLineAtOffset(50, currentY);
            pdPageContentStream.showText("總計新台幣: " + totalChinese);
            pdPageContentStream.endText();
            currentY -= lineSpacing;

            // 加上額外文字並分行
            String extraText = "本收據根據財政部88年9月14日 台財稅地881943611號含核准使用，由銷售人員自行印製不另開立統一發票，因國際電話卡適用零稅率，本收據不得作為申報和抵銷項稅額之憑證";
            String[] extraTextLines = extraText.split("，");
            for (String line : extraTextLines) {
                pdPageContentStream.setFont(chineseFont, 12);

                float extraTextX = 50;  // 設定為固定的左邊位置
                pdPageContentStream.beginText();
                pdPageContentStream.newLineAtOffset(extraTextX, currentY);
                pdPageContentStream.showText(line);
                pdPageContentStream.endText();
                currentY -= lineSpacing;
            }

            // 圖片（置中，並放置於框線內）
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.err.println("圖片檔案不存在: " + imagePath);
                return;
            }
            try (FileInputStream fis = new FileInputStream(imageFile)) {
                PDImageXObject pdImage = PDImageXObject.createFromFileByContent(imageFile, pdDocument);
                float imageWidth = 200;
                float imageHeight = 200;
                // 計算框線位置，圖片放入框線中
                float imageX = (PDRectangle.A4.getWidth() - imageWidth) / 2;
                float imageY = currentY - imageHeight;
                pdPageContentStream.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);
                currentY -= imageHeight + 20; // 調整圖片底部位置
            }

            // 繪製外框線，調整框線大小以包含圖片
            pdPageContentStream.setLineWidth(2f); // 增加框線粗細
            pdPageContentStream.setStrokingColor(0, 0, 0); // 黑色
            pdPageContentStream.moveTo(50, 800);
            pdPageContentStream.lineTo(550, 800);
            pdPageContentStream.lineTo(550, currentY);
            pdPageContentStream.lineTo(50, currentY);
            pdPageContentStream.closePath();
            pdPageContentStream.stroke();
        }
    }
}

