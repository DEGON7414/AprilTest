package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    /**
     * 將 PDF 檔案轉換為高品質圖片
     * @param pdfFile PDF 檔案
     * @param outputDir 輸出目錄
     * @return 生成的圖片路徑列表
     */
    public static List<String> convertPdfToImage(File pdfFile, String outputDir) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);

            // 設置高品質渲染選項
            RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            renderer.setRenderingHints(hints);

            // 一頁頁處理，旋轉後存入硬碟
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                // 使用更高的 DPI 值，例如 600 提高清晰度
                BufferedImage image = renderer.renderImageWithDPI(i, 300, ImageType.RGB);

                // 旋轉圖片 270 度
                BufferedImage rotatedImage = rotateImage(image);

                // 使用更好的圖片壓縮品質
                String imagePath = outputDir + "/page_" + (i + 1) + ".png";

                // 使用更高品質的寫入方式
                ImageOutputStream output = new FileImageOutputStream(new File(imagePath));
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
                if (writers.hasNext()) {
                    ImageWriter writer = writers.next();
                    ImageWriteParam param = writer.getDefaultWriteParam();

                    writer.setOutput(output);
                    writer.write(null, new IIOImage(rotatedImage, null, null), param);
                    writer.dispose();
                    output.close();
                } else {
                    // 如果找不到 PNG 寫入器，使用標準方法
                    ImageIO.write(rotatedImage, "PNG", new File(imagePath));
                }

                imagePaths.add(imagePath);
                image.flush();       // 釋放原始圖片記憶體
                rotatedImage.flush(); // 釋放旋轉後圖片記憶體
            }
        }
        return imagePaths;
    }

    /**
     * 旋轉圖片 270 度
     * @param image 原始圖片
     * @return 旋轉後的圖片
     */
    private static BufferedImage rotateImage(BufferedImage image) {
        int width = image.getHeight();
        int height = image.getWidth();

        BufferedImage rotatedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        // 設置高品質渲染
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 旋轉 270 度 (順時針)
        g2d.translate(0, height);
        g2d.rotate(-Math.PI / 2);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

}
