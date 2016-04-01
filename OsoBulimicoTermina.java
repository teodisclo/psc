package examen;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Semaphore;

public class OsoBulimicoTermina {
    static final int NPORCIONES = 50;  // capacidad tarro
    static Semaphore sPuedeLlenar = new Semaphore(NPORCIONES);
    static Semaphore sPuedeComer = new Semaphore(0);
    static Semaphore sMutex = new Semaphore(1);
    static Tarro t; //creado en clase principal
    
    static class Tarro {
        private int iPorcionesLlenas = 0;

        boolean nuevaPorcion()  {
            if (++iPorcionesLlenas < NPORCIONES ) {
                System.out.printf("Tarro: %d porciones llenas.\n", iPorcionesLlenas);
                return false;
            }
            return true;
        }
        void comoMiel() {
            System.out.printf("El oso se come la miel.\n");
            iPorcionesLlenas = 0;
        }
        int capacidad() {
            return NPORCIONES;
        }
    }
    static class Oso extends Thread {
        public void run() {
            while (true) {
                try {
                    System.out.printf("Oso durmiendo...\n");
                    sPuedeComer.acquire();
                    t.comoMiel();
                    sPuedeLlenar.release(t.capacidad());
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    static class Abeja extends Thread {
        static final int CUOTAPORCIONES = 5;   // cada abeja recoge esto
        private int idAbeja;
        private int iPorcionesColocadas;
        private ThreadLocalRandom tlr;
        
        Abeja(int idAbeja) {
            this.idAbeja = idAbeja;
            tlr = ThreadLocalRandom.current();
            iPorcionesColocadas = 0; //esto no hace falta
        }
        private void buscaMiel() throws InterruptedException {
            System.out.printf("Abeja %d va a buscar miel.\n", idAbeja);
            Thread.sleep(tlr.nextInt(100,500));
        }
        private boolean colocaPorcion() {
            System.out.printf("Abeja %d coloca porción %d\n.", idAbeja, iPorcionesColocadas);
            return t.nuevaPorcion();
        }
        public void run() {
            boolean bTarroLleno;
            try {
                System.out.printf("Creada abeja %d.\n", idAbeja);
                while (iPorcionesColocadas++ < CUOTAPORCIONES) {
                    buscaMiel();
                    sPuedeLlenar.acquire();
                    sMutex.acquire();
                    bTarroLleno = colocaPorcion();
                    sMutex.release();
                    if (bTarroLleno) {
                        System.out.printf("Tarro lleno: avisa al oso\n.");
                        sPuedeComer.release();
                    }
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class Principal {
        static final int NABEJAS = 20;
        
        public void arranca() {
            // crea tarro
            t = new Tarro();
            // crea abejas
            for (int i = 0; i < NABEJAS; i++) {
                new Abeja(i).start();
            }
            // crea oso
            new Oso().start();
        }
    }
    public static void main(String[] args) {

        new Principal().arranca();
    }
}
