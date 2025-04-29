package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
/**
 * ClassName: PDFReader
 * Description:
 * 圖片插入到PDF
 *
 *
 * @Author 許記源
 *
 */


public class InsertImgUtils {

    public static void insertImagesToSecondPages(File pdfFile, File imageFolder, File outputPdf) throws IOException {
        File[] imageFiles = imageFolder.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
        });

        if (imageFiles == null || imageFiles.length == 0) {
            throw new IOException("找不到圖片檔");
        }

        Arrays.sort(imageFiles);
        int imageIndex = 0;

        try (PDDocument document = PDDocument.load(pdfFile)) {
            for (int i = 1; i < document.getNumberOfPages(); i += 2) {
                if (imageIndex >= imageFiles.length) break;

                PDPage page = document.getPage(i);
                BufferedImage originalImage = ImageIO.read(imageFiles[imageIndex]);

                // 頁面尺寸
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();

                // 直接在這裡處理縮放 - 縮放至適合頁面寬度，但保持較大尺寸
                float scaleFactor = Math.min(pageWidth * 0.7f / originalImage.getWidth(), pageHeight * 0.3f / originalImage.getHeight());
                // 確保縮放比例不會太小
                scaleFactor = Math.max(scaleFactor, 0.35f);
                int scaledWidth = (int)(originalImage.getWidth() * scaleFactor);
                int scaledHeight = (int)(originalImage.getHeight() * scaleFactor);

                // 建立縮放後的圖片
                BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, originalImage.getType());
                Graphics2D g2d = scaledImage.createGraphics();

                // 修正圖片反轉問題 - 添加變換
                g2d.translate(0, scaledHeight);
                g2d.scale(1, -1);
                g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
                g2d.dispose();

                PDImageXObject pdImage = LosslessFactory.createFromImage(document, scaledImage);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                    // 圖片尺寸
                    float imgWidth = scaledImage.getWidth();
                    float imgHeight = scaledImage.getHeight();

                    // 計算水平置中位置
                    float x = 50;

                    // 注意：PDF座標系統原點在左下角，y值越小越靠下
                    // 設置圖片位置在頁面底部上方100點處
                    float y = 800;

                    // 輸出偵錯資訊
                    System.out.println("頁面寬度: " + pageWidth + ", 圖片寬度: " + imgWidth + ", X座標: " + x);
                    System.out.println("頁面高度: " + pageHeight + ", 圖片高度: " + imgHeight + ", Y座標: " + y);

                    // 畫圖
                    contentStream.drawImage(pdImage, x, y, imgWidth, imgHeight);
                }

                imageIndex++;
            }

            document.save(outputPdf);
        }
    }
}
