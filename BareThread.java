package examen;

public class BareThread {

    static class Thr extends Thread {
        char c;

        Thr(char c) {
            this.c = c;
        }
    
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println(c);
            }
        }
    }    

    public static void main(String[] args) throws InterruptedException {
        Thr tA, tB;
        
        tA = new Thr('a');
        tB = new Thr('b');
        
        tA.start(); tB.start();
        
        tA.join(); tB.join();
        
        

    }

}
