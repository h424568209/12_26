package Exceptions;



/**
 * finally的作用【
 *
 * 程序的目的是要确保main（）结束的时候开关必须是关闭的，所以在每个try块和异常处理程序的末尾都加入了对sw.off()方法的调用。
 * 但是也可能有这种情况：异常被抛出，但没被处理程序捕获，这是sw.off（）就不在调用
 * 使用finally（） 就可以顺利的执行
 */
class Switchs {
    private boolean state = false;
    public boolean read(){return state;}
    public void on(){state = true;
        System.out.println(this);}
        public void off(){
        state = false;
            System.out.println(this);
        }
        public String toString(){
        return state ? "on":"off";
        }
}
class  OnoffExecption1 extends Exception {}
class  OnoffExecption2 extends Exception{}
public class Switch{
     private static Switchs sw = new Switchs();
     public static void f() throws OnoffExecption1,OnoffExecption2{

     }

    public static void main(String[] args) {
        try {
            sw.on();
            f();
        }
        catch (OnoffExecption1 e){
            System.out.println("onOffException1");
       //     sw.off();
        }
        catch (OnoffExecption2 e){
            System.out.println("OnOffException2");
       //     sw.off();
        }
        finally {
            sw.off();
        }
    }
}
