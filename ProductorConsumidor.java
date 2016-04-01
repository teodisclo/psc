package examen;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class ProductorConsumidor {

    static final int BUFSIZ = 10;
    static final int ITEM = 1;
    static final int NPROD = 5;
    static final int NCONS = 5;
    static final int MAXITER = 30;
    
    static Semaphore sEspacio = new Semaphore(BUFSIZ);
    static Semaphore sItem = new Semaphore(0);
    static Semaphore sMutex = new Semaphore(1);

    static Buff bBuff = new Buff();
    
    private static class Productor extends Thread {
        
        private void esperaEvento(int nInf, int nSup) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(nInf, nSup));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
         }
        public void run() {
            for (int i = 0; i < MAXITER; i++) {
                esperaEvento(100,200);
                try {
                    sEspacio.acquire();
                    sMutex.acquire();
                    bBuff.inserta(1);
                    sMutex.release();
                    sItem.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static class Consumidor extends Thread {
        private void procesaEvento(int nInf, int nSup) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(nInf, nSup));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
         }
        public void run() {
            for (int i = 0; i < MAXITER; i++) {
                try {
                    sItem.acquire();
                    sMutex.acquire();
                    bBuff.extrae();
                    sMutex.release();
                    sEspacio.release();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                procesaEvento(300,500);
            }
        }
    }
    
    private static class Buff {
        private int[] aInt = new int[BUFSIZ];
        
        void inserta(int n) {
            for (int i = 0; i < aInt.length; i++) {
                if (aInt[i] == 0) {
                    aInt[i] = n;
                    System.out.printf("Insertado elemento %d\n", i);
                    return;
                }
            }
            System.out.printf("Error: intento de inserción en buffer lleno.\n");
            System.exit(-1);
        }

        int extrae() {
            int n;
            for (int i = 0; i < aInt.length; i++) {
                if (aInt[i] != 0) {
                    n = aInt[i];
                    aInt[i] = 0;
                    System.out.printf("Extraido elemento %d\n", i);
                    return n;
                }
            }
            System.out.printf("Error: intento de extracción de buffer vacío");
            System.exit(-1);
            return -1;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {

        Productor[] aProd = new Productor[NPROD];
        Consumidor[] aCons = new Consumidor[NCONS];
        
        for (int i = 0; i < aProd.length; i++) {
            aProd[i] = new Productor();
            aProd[i].start();
        }
        for (int i = 0; i < aCons.length; i++) {
            aCons[i] = new Consumidor();
            aCons[i].start();
        }
        for (int i = 0; i < aProd.length; i++) {
            aProd[i].join();
        }
        for (int i = 0; i < aCons.length; i++) {
            aCons[i].join();
        }
        System.out.println("Fin del programa.");
    }

}
