package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;

/**
 * ClassName: TextAppenderUtils
 * Description:
 *
 * @Author 許記源
 * @Create 2025/4/30 下午 02:26
 * @Version 1.0
 */
public class TextAppenderUtils {
    public static boolean addText(PDDocument document, int pageIndex, String date, String number, String amount) {
        try {
            PDPage page = document.getPage(pageIndex);
            PDRectangle mediaBox = page.getMediaBox();
            float width = mediaBox.getWidth();
            float height = mediaBox.getHeight();
            int numberOfPages = document.getNumberOfPages();
            File file = new File("C:\\Users\\cxhil\\Downloads\\Fonts_Kai\\TW-Kai-98_1.ttf");
//            File file = new File("C:\\Windows\\Fonts\\kaiu.ttf");
            PDType0Font font = PDType0Font.load(document, file);

            int amountInt = Integer.parseInt(amount);
            String chineseAmount = AmountToChinese.covertAmountToChinese(amountInt);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {

                contentStream.setLineCapStyle(1); // 圓形線帽
                contentStream.setLineJoinStyle(1);

                // 標題（旋轉 90 度）
                contentStream.beginText();
                contentStream.setFont(font, 16);
                // 設定旋轉角度與起始位置：90 度順時針旋轉，並指定旋轉中心座標 (x, y)
                float rotateX = width - 245;  // 水平位置
                float rotateY = height / 2 - 160;  // 垂直位置
                Matrix rotate = Matrix.getRotateInstance(Math.toRadians(90), rotateX, rotateY);

                contentStream.setTextMatrix(rotate);
                contentStream.showText("國際電話卡收據");
                contentStream.endText();


                // 日期
                contentStream.beginText();
                contentStream.setFont(font, 9);
                float rotateDateX = width - 233;  // 水平位置
                float rotateDateY1 = height / 2 - 135;  // 垂直位置
                Matrix rotateDate = Matrix.getRotateInstance(Math.toRadians(90), rotateDateX, rotateDateY1);
                contentStream.setTextMatrix(rotateDate);
                contentStream.showText(date);
                contentStream.endText();
//
                // 收據編號
                contentStream.beginText();
                contentStream.setFont(font, 9);
                float rotateNumberX = width - 225;  // 水平位置
                float rotateNumberY = height / 2 - 180;  // 垂直位置
                Matrix rotateNumber = Matrix.getRotateInstance(Math.toRadians(90), rotateNumberX, rotateNumberY);
                contentStream.setTextMatrix(rotateNumber);
                contentStream.showText("收據編號：" + number);
                contentStream.endText();
//
                // 買受人
                contentStream.beginText();
                contentStream.setFont(font, 9);
                float rotateBuyerX = width - 215;  // 水平位置
                float rotateBuyerY = height / 2 - 180;  // 垂直位置
                Matrix rotateBuyer = Matrix.getRotateInstance(Math.toRadians(90), rotateBuyerX, rotateBuyerY);
                contentStream.setTextMatrix(rotateBuyer);
                contentStream.showText("買受人：");
                contentStream.endText();

                // 統一編號
                contentStream.beginText();
                contentStream.setFont(font, 9);
                float rotateTaxIdX = width - 205;  // 水平位置
                float rotateTaxIdY = height / 2 - 180;  // 垂直位置
                Matrix rotateTaxId = Matrix.getRotateInstance(Math.toRadians(90), rotateTaxIdX, rotateTaxIdY);
                contentStream.setTextMatrix(rotateTaxId);
                contentStream.showText("統一編號：");
                contentStream.endText();
                // 地址
                contentStream.beginText();
                contentStream.setFont(font, 9);
                float rotateAddressX = width - 195;  // 水平位置
                float rotateAddressY = height / 2 - 180;  // 垂直位置
                Matrix rotateAddress = Matrix.getRotateInstance(Math.toRadians(90), rotateAddressX, rotateAddressY);
                contentStream.setTextMatrix(rotateAddress);
                contentStream.showText("地址：");
                contentStream.endText();

                // 第一條分隔線
                contentStream.setLineWidth(1f);
                float lineX1 = width - 190;  // 水平位置
                float lineY1 = height / 2 - 180;  // 垂直位置
                float x2 = lineX1;  // 垂直線，X 不變
                float y2 = lineY1 + 145;  // 長度 100 的線段
                contentStream.moveTo(lineX1, lineY1);
                contentStream.lineTo(x2, y2);
                contentStream.stroke();

                // 品名
                contentStream.beginText();
                contentStream.setFont(font, 11);
                float productX = width - 180;  // 水平位置
                float productY = height / 2 - 180;  // 垂直位置
                Matrix product = Matrix.getRotateInstance(Math.toRadians(90), productX, productY);
                contentStream.setTextMatrix(product);
                contentStream.showText("品名");
                contentStream.endText();
                //金額
                contentStream.beginText();
                contentStream.setFont(font, 9);
                float moneyX = width - 180;  // 水平位置
                float moneyY = height / 2 - 55;  // 垂直位置
                Matrix money = Matrix.getRotateInstance(Math.toRadians(90), moneyX, moneyY);
                contentStream.setTextMatrix(money);
                contentStream.showText("金額");
                contentStream.endText();
                // 品名內容
                contentStream.beginText();
                contentStream.setFont(font, 9);
                float productNameX = width - 170;  // 水平位置
                float productNameY = height / 2 - 180;  // 垂直位置
                Matrix productName = Matrix.getRotateInstance(Math.toRadians(90), productNameX, productNameY);
                contentStream.setTextMatrix(productName);
                contentStream.showText("出國上網設備及通信費用");
                contentStream.endText();

                // 金額內容
                String amountText = amount;
                contentStream.beginText();
                contentStream.setFont(font, 9);
                float moneyNumberX = width - 170;  // 水平位置
                float moneyNumberY = height / 2 - 55;  // 垂直位置
                Matrix moneyNumber = Matrix.getRotateInstance(Math.toRadians(90), moneyNumberX, moneyNumberY);
                contentStream.setTextMatrix(moneyNumber);
                contentStream.showText(amountText);
                contentStream.endText();
                // 第二條分隔線
                contentStream.setLineWidth(1f);
                float lineX2 = width - 165;  // 水平位置
                float lineY2 = height / 2 - 180;  // 垂直位置
                float x3 = lineX2;  // 垂直線，X 不變
                float y3 = lineY2 + 145;  // 長度 100 的線段
                contentStream.moveTo(lineX2, lineY2);
                contentStream.lineTo(x3, y3);
                contentStream.stroke();

                // 中文大寫總額
                contentStream.beginText();
                contentStream.setFont(font, 9);
                float chNumberX = width - 155;  // 水平位置
                float chNumberY = height / 2 - 180;  // 垂直位置
                Matrix chNumber = Matrix.getRotateInstance(Math.toRadians(90), chNumberX, chNumberY);
                contentStream.setTextMatrix(chNumber);
                contentStream.showText("總計新台幣 " + chineseAmount);
                contentStream.endText();

                // 備註文字
                String remark1 = " 本收據根據財政部 88 年 9 月 14 日 台財稅第";
                String remark2 = "881943611號 函核准使用，由銷售人員自行印製，不另開";
                String remark3 = "立統一發票。因國際電話卡適用零稅率，本收據不得作";
                String remark4 = "為申報和抵銷項稅額之憑證。";
                //第一段
                contentStream.beginText();
                contentStream.setFont(font, 6);
                float noteX = width - 145;  // 水平位置
                float noteY = height / 2 - 180;  // 垂直位置
                Matrix note = Matrix.getRotateInstance(Math.toRadians(90), noteX, noteY);
                contentStream.setTextMatrix(note);
                contentStream.showText(remark1);
                contentStream.endText();
                //第二段
                contentStream.beginText();
                contentStream.setFont(font, 6);
                float noteX1 = width - 138;  // 水平位置
                float noteY1 = height / 2 - 180;  // 垂直位置
                Matrix note1 = Matrix.getRotateInstance(Math.toRadians(90), noteX1, noteY1);
                contentStream.setTextMatrix(note1);
                contentStream.showText(remark2);
                contentStream.endText();
                //第三段
                contentStream.beginText();
                contentStream.setFont(font, 6);
                float noteX2 = width - 131;  // 水平位置
                float noteY2 = height / 2 - 180;  // 垂直位置
                Matrix note2 = Matrix.getRotateInstance(Math.toRadians(90), noteX2, noteY2);
                contentStream.setTextMatrix(note2);
                contentStream.showText(remark3);
                contentStream.endText();
                //第四段
                contentStream.beginText();
                contentStream.setFont(font, 6);
                float noteX3 = width - 124;  // 水平位置
                float noteY3 = height / 2 - 180;  // 垂直位置
                Matrix note3 = Matrix.getRotateInstance(Math.toRadians(90), noteX3, noteY3);
                contentStream.setTextMatrix(note3);
                contentStream.showText(remark4);
                contentStream.endText();
                //框線1 收據上方
                contentStream.setLineWidth(1f);
                float frameX = width - 270;  // 水平位置
                float frameY = height / 2 - 185;  // 垂直位置
                float XX = frameX;  // 垂直線，X 不變
                float YY = frameY + 160;  // 長度 100 的線段
                contentStream.moveTo(frameX, frameY);
                contentStream.lineTo(XX, YY);
                contentStream.stroke();
                //框線2 印章底下
                contentStream.setLineWidth(1f);
                float frameX1 = width - 10;  // 水平位置
                float frameY1 = height / 2 - 185;  // 垂直位置
                float XX1 = frameX1;  // 垂直線，X 不變
                float YY1 = frameY1 + 160;  // 長度 100 的線段
                contentStream.moveTo(frameX1, frameY1);
                contentStream.lineTo(XX1, YY1);
                contentStream.stroke();
                //框線3 品名旁邊
                contentStream.setLineWidth(1f);
                contentStream.moveTo(XX, YY);       // 左上角起點
                contentStream.lineTo(XX1, YY1);     // 右上角終點
                contentStream.stroke();

                // 框線4 金額旁邊
                contentStream.setLineWidth(1f);
                contentStream.moveTo(frameX, frameY);     // 左下角起點
                contentStream.lineTo(frameX1, frameY1);   // 右下角終點
                contentStream.stroke();
            }


            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
