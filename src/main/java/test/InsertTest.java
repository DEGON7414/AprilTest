package test;

import utils.InsertImgUtils;

import java.io.File;
import java.io.IOException;

/**
 * ClassName: InsertTest
 * Description:
 *將圖片資料夾插入到PDF中
 * @Author 許記源
 * @Create 2025/4/29 下午 01:18
 * @Version 1.0
 */
public class InsertTest {
    public static void main(String[] args) {

        File inputPDF = new File("C:\\Users\\cxhil\\Desktop\\kevin0427考題\\考題.pdf");
        File imageFolder = new File("C:\\Users\\cxhil\\Desktop\\kevin0427考題\\IMG");   // 圖片資料夾
        File outputPdf = new File("C:\\Users\\cxhil\\Desktop\\kevin0427考題\\output_with_images.pdf"); // 產生的新 PDF

        try {
            InsertImgUtils.insertImagesToSecondPages(inputPDF, imageFolder, outputPdf);
            System.out.println("圖片成功插入，新的 PDF 存在：" + outputPdf.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("插入失敗！");
        }
    }
}

