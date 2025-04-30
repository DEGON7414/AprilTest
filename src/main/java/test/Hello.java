package test;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import utils.InvoiceImageUtil;
import utils.PicInsert;
import utils.TextAppenderUtils;
import java.io.IOException;


/**
 * ClassName: Hello
 * Description:
 *
 * @Author 許記源
 * @Create 2025/4/30 下午 02:21
 * @Version 1.0
 */
public class Hello {
    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\考題.pdf";
        String outputFilePath = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\Hello.pdf"; // 最終檔案
        String imagePath = "C:\\Users\\cxhil\\Desktop\\kevin0427考題\\receipt_signature.bmp"; // 圖片路徑

        try (PDDocument document = PDDocument.load(new File(filePath))) {

            // 先插入文字
            boolean successText = TextAppenderUtils.addText(document);
            if (successText) {
                System.out.println("文字插入成功");
                // 旋轉圖片並保存旋轉後的圖片
                File picFile = new File(imagePath);
                double degree = 270;
                File rotatedImage = PicInsert.rotateImage(picFile, degree);

                // 接著插入圖片
                boolean successImage = PicInsert.insertPic(document,rotatedImage);

                if (successImage) {
                    System.out.println("圖片插入成功，全部完成");
                    // 最後保存到 outputFilePath
                    document.save(outputFilePath);
                } else {
                    System.out.println("圖片插入失敗");
                }

            } else {
                System.out.println("文字插入失敗");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


