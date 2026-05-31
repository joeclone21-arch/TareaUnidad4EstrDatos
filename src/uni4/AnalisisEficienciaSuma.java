package uni4;


public class AnalisisEficienciaSuma {

    // Solucion A: inicial - O(K^2)
    public static boolean sumaGenerica(int[] arr, int target) {
        // Compara cada elemento con todos los demás usando bucles anidados
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] + arr[j] == target) {
                    return true; // Encontrado
                }
            }
        }
        return false; // No existe la pareja
    }

    // Solucion B: Optimizada (Dos Punteros) - O(K)
    // Aprovecha que el arreglo ya viene ordenado de menor a mayor
    public static boolean sumaOptimizada(int[] arr, int target) {
        int izquierda = 0;
        int derecha = arr.length - 1;

        while (izquierda < derecha) {
            int sumaActual = arr[izquierda] + arr[derecha];

            if (sumaActual == target) {
                return true; // Encontrado
            } else if (sumaActual < target) {
                izquierda++; // La suma es muy baja, se mueve a números más grandes
            } else {
                derecha--; // La suma es muy alta, se mueve a números más chicos
            }
        }
        return false; // No existe la pareja
    }

    public static void main(String[] args) {
        // se simula un tamaño controlado de 50000 para no colgar la PC
        final int K = 50000; 
        int[] datos = new int[K];
        
        // se llena el arreglo ordenado simple para la prueba
        for (int i = 0; i < K; i++) {
            datos[i] = i * 2; 
        }
        
        // un numero target que obligue al peor caso es decir que no exista
        int target = 999999; 

        System.out.println("--- EJECUTANDO ANÁLISIS DE EFICIENCIA (K = " + K + ") ---");

        // medición solucion inicial
        long inicioGen = System.nanoTime();
        boolean resGen = sumaGenerica(datos, target);
        long finGen = System.nanoTime();
        System.out.println("Solucion Inicial O(K^2) -> Resultado: " + resGen + " | Tiempo: " + (finGen - inicioGen) / 1e6 + " ms");

        // medición solucion Optimizada
        long inicioOpt = System.nanoTime();
        boolean resOpt = sumaOptimizada(datos, target);
        long finOpt = System.nanoTime();
        System.out.println("Solucion Optimizada O(K) -> Resultado: " + resOpt + " | Tiempo: " + (finOpt - inicioOpt) / 1e6 + " ms");
    }
}