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
    public static  String covertAmountToChinese(int amount) {
        String [] chinsesNumbers ={"零", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖"} ;
        String [] units = {"", "拾", "佰", "仟", "萬", "億", "兆"};
        if (amount < 0) {
            return null;
        }
        StringBuilder rs = new StringBuilder();
        int unitindex = 0;
        while (amount > 0) {
            int i = amount % 10;
            if(i !=0){
                rs.insert(0, chinsesNumbers[i ]+units[unitindex]);
            }else {
                if(rs.length()>0 && rs.charAt(0) !='零'){
                    rs.insert(0,chinsesNumbers[i ]);
                }
            }
            amount /= 10;
            unitindex++;
        }
        if(rs.charAt(0)=='零'){
            rs.deleteCharAt(0);
        }
        return rs.toString()+ "元";
    }
}
