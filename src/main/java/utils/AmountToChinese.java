package utils;

/**
 * ClassName: AmountToChinese
 * Description:
 *數字轉換中文數字
 * @Author 許記源
 * @Create 2025/4/28 下午 03:33
 * @Version 1.0
 */
public class AmountToChinese {
    public static String covertAmountToChinese(int amount) {
        String[] chineseNumbers = {"零", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖"};
        String[] units = {"仟萬", "佰萬", "拾萬", "萬", "仟", "佰", "拾", ""}; // 固定8位

        if (amount < 0 || amount > 99999999) {
            return "金額超出範圍";
        }

        String amountStr = String.format("%08d", amount); // 補滿8位數
        StringBuilder result = new StringBuilder();
        boolean started = false; // 是否開始輸出（略過萬位之前的0）

        for (int i = 0; i < 8; i++) {
            int digit = amountStr.charAt(i) - '0';
            String unit = units[i];

            // 只要萬以下，強制開始輸出
            if (i >= 3) {
                started = true;
            }

            // 如果該位不是 0 或已經開始輸出，就印出該位
            if (digit != 0 || started) {
                result.append(chineseNumbers[digit]).append(unit);
                started = true;
            }
        }

        return result.toString() + "元";
    }
}