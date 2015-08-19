package miner.topo;

/**
 * Created by cutoutsy on 8/12/15.
 */
public class ThreadTest extends Thread{
    private int ticket = 5;
    public void run(){
        for(int i = 0; i < 10; i++){
            if(ticket > 0){
                System.out.println("ticket = "+ticket--);
            }
        }
    }
}
