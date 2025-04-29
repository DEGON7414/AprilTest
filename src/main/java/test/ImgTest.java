package test;

import utils.InvoiceImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * ClassName: ImgTest
 * Description:
 *將PDF轉為圖片檔
 * @Author 許記源
 * @Create 2025/4/29 上午 10:49
 * @Version 1.0
 */
public class ImgTest {
    public static void main(String[] args) {
        try {
            // 準備測試資料
            File pdfFile = new File("C:\\Users\\cxhil\\Desktop\\kevin0427考題\\export_combined.pdf"); // 替換為你的 PDF 檔案路徑
            String outputDir = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\IMG"; // 替換為你的輸出目錄
           File imageFolder = new File(outputDir);
            if(!imageFolder.exists()) {
                imageFolder.mkdir();
            }
            // 檢查 PDF 檔案是否存在
            if (!pdfFile.exists()) {
                System.out.println("PDF 檔案不存在: " + pdfFile.getAbsolutePath());
                return;
            }

            // 執行轉換
            System.out.println("開始轉換 PDF 為圖片...");
            List<String> imagePaths = InvoiceImageUtil.convertPdfToImage(pdfFile, outputDir);

            // 檢查結果
            if (imagePaths.isEmpty()) {
                System.out.println("未生成任何圖片檔案");
                return;
            }

            // 列出生成的圖片路徑
            System.out.println("圖片已生成，請檢查以下路徑：");
            for (String imagePath : imagePaths) {
                File imageFile = new File(imagePath);
                if (imageFile.exists() && imageFile.length() > 0) {
                    System.out.println("圖片生成成功: " + imagePath);
                } else {
                    System.out.println("圖片生成失敗或為空: " + imagePath);
                }
            }

            System.out.println("請人工檢查圖片是否旋轉 270 度且無缺字。");
        } catch (Exception e) {
            System.out.println("轉換過程中發生錯誤: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
