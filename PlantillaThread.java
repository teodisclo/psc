package examen;

import java.util.concurrent.ThreadLocalRandom;

class PlantillaThread {

    static class Thr extends Thread {
        char c;
        
        Thr(char c) {
            this.c = c;
        }
        
        public void run() {
            ThreadLocalRandom tThr = ThreadLocalRandom.current();
            for (int i = 0; i < 100; i++) {
                System.out.println(c);
                try {
                    Thread.sleep(tThr.nextInt(100,200));
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

    public static void main(String[] args) throws InterruptedException {
        
        Thr ta, tb;
        
        ta = new Thr('a');
        tb = new Thr('b');
        
        ta.start();
        tb.start();
        
        ta.join();
        tb.join();
        
        System.out.println("Fin programa.");
        
        
    }

}
