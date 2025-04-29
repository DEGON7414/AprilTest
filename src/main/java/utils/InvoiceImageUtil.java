package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: InvoiceImageUtil
 * Description:
 * 處理圖片
 * 1.旋轉
 * 2.轉成圖片
 *
 * @Author 許記源
 * @Create 2025/4/29 上午 10:34
 * @Version 1.0
 */
public class InvoiceImageUtil {
    // 將 PDF 檔案每一頁轉成圖片，旋轉 270 度後存入硬碟
    public static List<String> convertPdfToImage(File pdfFile, String outputDir) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            // 設置抗鋸齒渲染
            renderer.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC));
            renderer.setRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY));
            renderer.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));

            // 加載標楷體字體
            String fontPath = "C:\\Windows\\Fonts\\kaiu.ttf";
            PDType0Font.load(document, new File(fontPath));

            // 一頁頁處理，旋轉後存入硬碟
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 300);
                // 旋轉圖片 270 度
                BufferedImage rotatedImage = rotateImage(image);
                String imagePath = outputDir + "/page_" + (i + 1) + ".png";
                javax.imageio.ImageIO.write(rotatedImage, "PNG", new File(imagePath));
                imagePaths.add(imagePath);
                image.flush();       // 釋放原始圖片記憶體
                rotatedImage.flush(); // 釋放旋轉後圖片記憶體
            }
        }
        return imagePaths; // 返回圖片路徑列表
    }

    //2.將圖片旋轉270度
    public static BufferedImage rotateImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage bufferedImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        graphics.translate(0, width);
        graphics.rotate(Math.toRadians(-90));

        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return bufferedImage;

    }




}
