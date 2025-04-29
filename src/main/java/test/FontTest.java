package test;

import java.awt.*;

/**
 * ClassName: FontTest
 * Description:
 *
 * @Author 許記源
 * @Create 2025/4/29 下午 04:14
 * @Version 1.0
 */
public class FontTest {
    public static void main(String[] args) {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String fontName : fontNames) {
            System.out.println(fontName);
    }
}}
