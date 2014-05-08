package mobi.tarantio.tools.sharinger.app;

/**
 * Created by kolipass on 06.05.14.
 */
public class test {
    public static void main(String[] args){
        IntentHandler intentHandler = new IntentHandler(" ");
        System.out.println(intentHandler.checkBracketsCloseUrl("Лайфхакер: Foursquare: все тот же, но без чекинов. (http://google.com/newsstand/s/CBIwuIzbtAY)"));
    }
}
