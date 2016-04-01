package examen;

public class ArrayThreads {

    static Thread[] tArr = {
        new Thr('a'),
        new Thr('b'),
        new Thr('c')
    };
            
    static class Thr extends Thread {
        char c;
        
        Thr(char c) {
            this.c = c;
        }
        public void run() {
            System.out.println('c');
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < tArr.length; i++) {
            tArr[i].start();
        }
    }

}
