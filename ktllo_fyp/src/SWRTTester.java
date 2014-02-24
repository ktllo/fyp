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
        String testString="We came here for the restaurant week. The main reason for choosing this restaurant was that this was the only restaurant available at the time we made reservation.\n" +
"We were seated at a table across from its open kitchen, from where I could see what the chef was doing.\n" +
"The food was average but value for money.\n" +
"The lunch menu was tailor-made for restaurant week.Starter\n" +
"Smoked salmon salad (good)\n" +
"Seafood chowder soup(there was not much seafood in the soup and was weak in flavour)\n" +
"Main course\n" +
"Grilled flank steak (average) and\n" +
"mashed potato with cheese (crispy and delicious)\n" +
"Desert\n" +
"New York cheesecake (average)\n" +
"Chocolate bar ( which consisted of 3 layers of different texture was amazing) and vanilla ice- cream (good)\n" +
"Beverage\n" +
"Ginger tea (incredibly good)\n" +
"Cappuccino (smell good, strong flavour)\n" +
"The manager was friendly. She gave us some discount coupons after the meal.\n" +
"This hotel restaurant was quite small but cozy. Service was good. Staff was professional. I will recommend this restaurant.";
        StopwordRemoverThread swrt = new StopwordRemoverThread(null,testString);
        System.out.println(testString);
        swrt.removeStopword();
        System.out.println(swrt.getString());
        
    }
}
