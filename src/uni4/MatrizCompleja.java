package uni4;

import java.util.Random;

public class MatrizCompleja {

    private static final int FILAS = 1000; //para prueba inicial f y c de 50
    private static final int COLUMNAS = 1000;
    private static final int TOTAL_ELEMENTOS = FILAS * COLUMNAS;

    public static void main(String[] args) {
        // genera la matriz aleatoria (-500000 a 500000 para buena dispersión)
        int[][] matriz = generarMatriz(FILAS, COLUMNAS);
        
        // valores a buscar X y -X
        int valorX = 500;
        int valorMenosX = -valorX;

        System.out.println("--- PRUEBA DE BUSQUEDA (matriz desordenada) ---");
        // busqueda secuencial (Funciona en desordenado)
        buscarSecuencial(matriz, valorX);
        buscarSecuencial(matriz, valorMenosX);

        // para busqueda binaria e interpolación, se necesita aplanar y ordenar el arreglo primero
        int[] arregloAplanado = aplanarMatriz(matriz);
        
        // uso Merge Sort para ordenarlo de manera eficiente para las busquedas
        int[] datosMerge = arregloAplanado.clone();
        long tInicioMerge = System.nanoTime();
        mergeSort(datosMerge, 0, datosMerge.length - 1);
        System.out.println("Merge Sort finalizado en: " + (System.nanoTime() - tInicioMerge) / 1e6 + " ms");
        
        int[] datosShell = arregloAplanado.clone();
        long tInicioShell = System.nanoTime();
        shellSort(datosShell);
        System.out.println("Shell Sort finalizado en: " + (System.nanoTime() - tInicioShell) / 1e6 + " ms");
        
        int[] datosCounting = arregloAplanado.clone();
        long tInicioCount = System.nanoTime();
        countingSort(datosCounting);
        System.out.println("Counting Sort finalizado en: " + (System.nanoTime() - tInicioCount) / 1e6 + " ms");
        
        int[] datosRadix = arregloAplanado.clone();
        long tInicioRadix = System.nanoTime();
        radixSort(datosRadix);
        System.out.println("Radix Sort finalizado en: " + (System.nanoTime() - tInicioRadix) / 1e6 + " ms");
        
        System.out.println("\n--- PRUEBAS DE BUSQUEDA (Arreglo ordenado asintoticamente) ---");
        buscarBinariaAnat(datosMerge, valorX);
        buscarInterpolacionAnat(datosMerge, valorX);
        buscarBinariaAnat(datosMerge, valorMenosX);
        buscarInterpolacionAnat(datosMerge, valorMenosX);
        
    }

 
    // PARA LA MATRIZ
    
    public static int[][] generarMatriz(int f, int c) {
        int[][] mat = new int[f][c];
        Random rnd = new Random();
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
                // numeros entre -500,000 y 500,000
            	// para prueba inicial rango numeros entre 1000 y -1000
                //mat[i][j] = rnd.nextInt(2001) - 1000;
            	mat[i][j] = rnd.nextInt(1000001) - 500000;
            }
        }
        return mat;
    }

    public static int[] aplanarMatriz(int[][] mat) {
        int[] arr = new int[TOTAL_ELEMENTOS];
        int k = 0;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                arr[k++] = mat[i][j];
            }
        }
        return arr;
    }

    public static int[][] desaplanarArreglo(int[] arr, int f, int c) {
        int[][] mat = new int[f][c];
        int k = 0;
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
                mat[i][j] = arr[k++];
            }
        }
        return mat;
    }

    
    // A. METODOS DE BUSQUEDA 
 
    
    // busqueda Secuencial - O(N * M)
    public static void buscarSecuencial(int[][] mat, int target) {
        long inicio = System.nanoTime();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                if (mat[i][j] == target) {
                    long fin = System.nanoTime();
                    System.out.println("Secuencial: Encontrado " + target + " en [" + i + "][" + j + "] en " + (fin - inicio)/1e6 + " ms");
                    return;
                }
            }
        }
        long fin = System.nanoTime();
        System.out.println("Secuencial: " + target + " NO encontrado. Tiempo: " + (fin - inicio)/1e6 + " ms");
    }

    // busqueda Binaria - O(log K)
    public static void buscarBinariaAnat(int[] arr, int target) {
        long inicio = System.nanoTime();
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) {
                long fin = System.nanoTime();
                System.out.println("Binaria: Encontrado " + target + " en indice " + mid + " en " + (fin - inicio)/1e6 + " ms");
                return;
            }
            if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        long fin = System.nanoTime();
        System.out.println("Binaria: " + target + " NO encontrado en " + (fin - inicio)/1e6 + " ms");
    }

    // busqueda por Interpolacion - O(log(log K)) promedio
    public static void buscarInterpolacionAnat(int[] arr, int target) {
        long inicio = System.nanoTime();
        int low = 0, high = arr.length - 1;

        while (low <= high && target >= arr[low] && target <= arr[high]) {
            if (low == high) {
                if (arr[low] == target) {
                    long fin = System.nanoTime();
                    System.out.println("Interpolacion: Encontrado en " + (fin - inicio)/1e6 + " ms");
                    return;
                }
                break;
            }
            
            // formula de interpolacion lineal basada en valor numerico
            int pos = low + (int)(((double)(high - low) / (arr[high] - arr[low])) * (target - arr[low]));

            if (arr[pos] == target) {
                long fin = System.nanoTime();
                System.out.println("Interpolacion: Encontrado " + target + " en indice " + pos + " en " + (fin - inicio)/1e6 + " ms");
                return;
            }
            if (arr[pos] < target) low = pos + 1;
            else high = pos - 1;
        }
        long fin = System.nanoTime();
        System.out.println("Interpolacion: " + target + " NO encontrado en " + (fin - inicio)/1e6 + " ms");
    }


    // B. ALGORITMOS DE ORDENAMIENTO 
  

    //  MERGE SORT - O(K log K) - eficiente alto
    public static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    private static void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];
        System.arraycopy(arr, l, L, 0, n1);
        System.arraycopy(arr, m + 1, R, 0, n2);

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else arr[k++] = R[j++];
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    // SHELL SORT - O(K^(1.5)) - eficiente
    public static void shellSort(int[] arr) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j;
                for (j = i; j >= gap && arr[j - gap] > temp; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = temp;
            }
        }
    }

    // BUBBLE SORT - O(K^2) - ejecutar con cuidado con K = 1,000,000
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean intercambiado;
        for (int i = 0; i < n - 1; i++) {
            intercambiado = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    intercambiado = true;
                }
            }
            if (!intercambiado) break; // Optimizacion si ya esta ordenado
        }
    }

    // INSERTION SORT - O(K^2) - muy lento para 1 millon de elementos
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    // Counting Sort y Radix Sort modificados para manejar numeros negativos requieren 
    // desplazar los valores buscando el elemento mínimo, o bien usar Radix especializado
    
 // COUNTING SORT - O(K + R) - Eficiente para rangos controlados
    public static void countingSort(int[] arr) {
        if (arr.length == 0) return;

        // buscar el valor máximo y mínimo para determinar el rango
        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
            if (arr[i] < min) min = arr[i];
        }

        int rango = max - min + 1;
        int[] count = new int[rango];
        int[] output = new int[arr.length];

        // almacena el conteo de cada elemento (usando el offset 'min')
        for (int i = 0; i < arr.length; i++) {
            count[arr[i] - min]++;
        }

        // cambiar count[i] para que contenga la posición real en el arreglo de salida
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // construir el arreglo de salida
        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i] - min] - 1] = arr[i];
            count[arr[i] - min]--;
        }

        // copiar los elementos ordenados de vuelta al arreglo original
        System.arraycopy(output, 0, arr, 0, arr.length);
    }
    
    // RADIX SORT - O(d * (K + B)) - ordenamiento por digitos
    public static void radixSort(int[] arr) {
        if (arr.length == 0) return;

        // Encuentra el valor mínimo para manejar los números negativos
        int min = arr[0];
        for (int val : arr) {
            if (val < min) min = val;
        }

        // Si hay negativos, mover temporalmente todo el rango a positivo
        int desplazamiento = 0;
        if (min < 0) {
            desplazamiento = -min;
            for (int i = 0; i < arr.length; i++) {
                arr[i] += desplazamiento;
            }
        }

        // Encuentra el valor máximo absoluto para saber cuántos dígitos procesar
        int max = arr[0];
        for (int val : arr) {
            if (val > max) max = val;
        }

        // Hacer Counting Sort para cada dígito 
        // exponencial pasa de 1 (unidades) a 10 (decenas), 100 (centenas) etc
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortPorDigito(arr, exp);
        }

        // Revertir el desplazamiento para recuperar los números negativos originales
        if (desplazamiento > 0) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] -= desplazamiento;
            }
        }
    }

    // metodo auxiliar de Counting Sort modificado para Radix (Base 10)
    private static void countingSortPorDigito(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10]; // Base 10 (dígitos del 0 al 9)

        // almacena el conteo de las ocurrencias del dígito actual
        for (int i = 0; i < n; i++) {
            int digito = (arr[i] / exp) % 10;
            count[digito]++;
        }

        // cambia la posición para reflejar el índice real
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // construye el arreglo de salida respetando la estabilidad
        for (int i = n - 1; i >= 0; i--) {
            int digito = (arr[i] / exp) % 10;
            output[count[digito] - 1] = arr[i];
            count[digito]--;
        }

        // copiar al arreglo original
        System.arraycopy(output, 0, arr, 0, n);
    }
}