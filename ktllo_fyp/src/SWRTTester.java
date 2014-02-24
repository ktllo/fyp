/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.leolo.fyp.util.StopwordRemoverThread;
/**
 *
 * @author leolo
 */
public class SWRTTester {
    public static void main(String [] args){
        String testString="好耐冇放過星期六，陪老婆去上環做完緊要野後就去附近搵野食。行過永和街見到La Lune呢間新店，見Lunch Menu Appetizer 同 Main Dish 各可以4選1, 見有Masala Lamb pie/ Duck Confit 幾吸引就走入去試。\n" +
"老婆揀Green Pea Soup做Appetizer, Masala Lamb Pie 做main. 我就揀Grill Pepper做Appetizer, Duck Leg Confit 做main.\n" +
"好快待者就送上自家製bread and butter，暖暖地好香口。大概20分鐘後，我地嘅lunch set 就上菜。先講自己個set，Grill Pepper 甜甜地酸酸地，做到開胃嘅效果。 Duck Leg Confit 鴨肉煮得嫩同有油香，唔韌唔鞋，汁味道咸淡適中，如果再加杯紅酒就更好！到老婆果set， 我試一口Green Pea Soup，豆味好香，Masala Lamb Pie, 羊味好出好香口，個枇皮焗得好香口。\n" +
"總體黎講，味道收哂貨，美中不足係上菜程序就略為失色，成個set一次過上哂Salad, Appetizer 同 main course，好不西餐！ 相信同級餐廳會一道一道上。希望佢地注意…";
        StopwordRemoverThread swrt = new StopwordRemoverThread(null,testString);
        System.out.println(testString);
        swrt.removeStopword();
        System.out.println(swrt.getString());
        
    }
}
