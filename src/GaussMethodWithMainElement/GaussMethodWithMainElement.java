package GaussMethodWithMainElement;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GaussMethodWithMainElement {
    /**
     * ********** ИЩЕМ РЕЗУЛЬТАТ УРАВНЕНИЯ Ax = b  ***********
     */
    private double A[][];       // входная квадратная матрица A
    private double inpA[][];    // копия матрицы, не меняется
    private double trA[][];     // копия матрицы, приведенная к треугольному виду
    private double revA[][];    // обратная матрица матрице A
    private double B[];         // входной вектор b
    private double trB[];       // вектор b после приведения матрицы A к треугольному виду
    private double E[][];       // единичная матрица
    private double x[];         // результирующий вектор
    private double nevVect[];   // вектор невязки для проверки ошибок
    private double determ = 1;  // определитель матрицы A
    private int n;              // количество элеметов в в строке и столбце матрицы A

    public GaussMethodWithMainElement() {
        n = 0;
    }

    /**
     * Не тестить
     * Куча копирований матриц и векторов для дальнейшей работы
     */
    public void init() {
        trA = new double[n][n];
        inpA = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                trA[i][j] = A[i][j];
                inpA[i][j] = A[i][j];
            }

        trB = new double[n];
        System.arraycopy(B, 0, trB, 0, n);
        nevVect = new double[n];
        revA = new double[n][n];
        E = new double[n][n];
        for (int i = 0; i < n; i++)
            E[i][i] = 1;
    }

    /**
     * Можно тестить
     * Ищет максимальный элемент в столбцах и переносит их на главную диагональ
     * перетаскивая всю строчку матрицы
     * Использует trA как источник матрицы и determ для домножения на -1
     */
    public void moveToMainDiag() {
        double max;
        int index;
        for (int i = 0; i < n; i++) {
            max = trA[i][i];
            index = i;
            //find max elem
            for (int j = i; j < n; j++)
                if (max < trA[j][i]) {
                    index = j;
                    max = trA[j][i];
                }
            if (index != i) {
                determ *= -1;
                swap(i, index);
            }
        }
    }

    /**
     * Можно потестить
     * меняет местами строчки с номерами i и j
     * в матрицах trA, A, E
     * и векторах trB, B,
     *
     * @param i верхняя строчка
     * @param j нижняя строчка
     */
    public void swap(int i, int j) {
        // меняем местами элементы в векторах
        double m = trB[i];
        trB[i] = trB[j];
        trB[j] = m;
        m = B[i];
        B[i] = B[j];
        B[j] = m;
        // меняем местами строчки в матрицах
        for (int k = 0; k < n; k++) {
            m = trA[i][k];
            trA[i][k] = trA[j][k];
            trA[j][k] = m;
            m = A[i][k];
            A[i][k] = A[j][k];
            A[j][k] = m;
            m = E[i][k];
            E[i][k] = E[j][k];
            E[j][k] = m;
        }
    }

    /**
     * Можно потестить
     * приводим матрицы trA, E к треугольному виду
     * вектор trB меняется одновременно с ними
     */
    public void formTriangle() {
        for (int i = 0; i < n - 1; i++)
            for (int k = i + 1; k < n; k++) {
                double f = 0;
                if (trA[i][i] != 0)
                    f = trA[k][i] / trA[i][i];
                trB[k] -= f * trB[i];
                for (int j = n - 1; j >= 0; j--) {
                    trA[k][j] -= f * trA[i][j];
                    E[k][j] -= f * E[i][j];
                }
            }
    }

    /**
     * Можно потестить
     * Проверка обратной матрицы,
     * что при перемножении inpA и revA получается E
     */
    public void checkReverseMatrix() {
        System.out.print("Checking:\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double k = 0.0;
                for (int l = 0; l < n; l++) {
                    double t = inpA[i][l] * revA[l][j];
                    k = k + t;
                }
                System.out.print(Double.toString(k) + ' ');
            }
            System.out.print('\n');
        }
    }

    /**
     * Можно потестить
     * Транспонирует поданную матрицу
     *
     * @param matr квадратная матрица
     * @return квадратная транспонированная матрица
     */
    public double[][] transp(double[][] matr) {
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                double k = matr[i][j];
                matr[i][j] = matr[j][i];
                matr[j][i] = k;
            }
        return matr;
    }

    /**
     * ищем результат trA * r = rB
     *
     * @param rB вектор в правой части системы линейных уравнений
     * @return результирующий вектор
     */
    public double[] result(double[] rB) {
        double[] r = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            r[i] = rB[i];
            for (int j = i; j < n - 1; j++)
                r[i] = r[i] - r[j + 1] * trA[i][j + 1];
            r[i] /= trA[i][i];
        }
        return r;
    }

    /**
     * Можно потестить
     * вычисляем вектор невязки как nevVect = b - A * x
     */
    public void vectNev() {
        if (determ != 0) {
            nevVect = new double[n];
            for (int i = 0; i < n; i++) {
                nevVect[i] = B[i];
                for (int j = 0; j < n; j++)
                    nevVect[i] -= x[j] * A[i][j];
            }
        }
    }

    /**
     * можно потестить
     * ищем определитель входнйо матрицы
     *
     * @param triangl квадратная матрица, приведенная к треугольнмоу виду
     *
     *                результат кладется в determ
     */
    public void findDeterminant(double[][] triangl) {
        for (int i = 0; i < n; i++)
            determ = determ * triangl[i][i];
    }

    /**
     * не тестировать(хотя на самом деле можно позаморачиваться)
     * Основной кусок, сдесь собраны все вычисления
     */
    public void findResult() {
        init();
        moveToMainDiag();
        formTriangle();
        findDeterminant(trA);

        // проверка на невырожденность
        if (determ != 0.0) {

            printIntermed();

            //ищем результирующий вектор
            x = result(trB);
            vectNev();
            printResult();
            System.out.print('\n');

            E = transp(E);
            // ищем обратную матрицу
            revA = new double[n][n];
            for (int i = 0; i < n; i++) {
                revA[i] = result(E[i]);
            }
            revA = transp(revA);
            System.out.println("\nReverse matrix:");
            printMatr(revA);

            // проверяем обратную матрицу
            checkReverseMatrix();
        } else
            System.out.println("Determinant equal 0, origin matrix is degenerate");
    }

    //---------------------------останьное не тестить!-----------------------------
    /**
     * read input data:
     * n - count of int in column
     * A - square matrix
     * B - vector B in right side of linear equation
     * and check matrix for correct
     *
     * @return
     */
    public int readInput() {
        try (Scanner in = new Scanner(new File("input.txt"))) {
            n = in.nextInt();
            A = new double[n][n];
            B = new double[n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (in.hasNextDouble())
                        A[i][j] = in.nextDouble();
                    else {
                        System.out.println("Input date is incorrect!");
                        return 1;
                    }

            for (int i = 0; i < n; i++) {
                if (in.hasNextDouble())
                    B[i] = in.nextDouble();
                else {
                    System.out.println("Input date is incorrect!");
                    return 1;
                }

            }
            if (in.hasNextDouble()) {
                System.out.println("Input date is incorrect!");
                return 1;
            }
            return 0;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    /**
     * intermediate show
     */
    public void printIntermed() {
        System.out.println("Original date:");
        printLin(A, B);
        System.out.println("Triangular form of matrix:");
        printLin(trA, trB);
        System.out.print("Determinant: ");
        System.out.println(determ);
    }


    public void printResult() {
        if (determ != 0) {
            System.out.println("Result:");
            for (int i = 0; i < n; i++)
                System.out.print(Double.toString(x[i]) + ' ');
            System.out.println("\nVector of Neviazka");
            for (int i = 0; i < n; i++)
                System.out.print(Double.toString(nevVect[i]) + ' ');
        } else
            System.out.print("Determinant equal 0, origin matrix is degenerate");
    }

    /**
     * show linear equition
     *
     * @param matr left matrix
     * @param b    right vector
     */
    public void printLin(double[][] matr, double[] b) {
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++)
                System.out.print(Double.toString(matr[i][k]) + ' ');
            System.out.print("  ");
            System.out.println(Double.toString(b[i]));

        }
        System.out.print('\n');
    }

    /**
     * show motrix in console
     *
     * @param matr matrix
     */
    public void printMatr(double[][] matr) {
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++)
                System.out.print(Double.toString(matr[i][k]) + ' ');
            System.out.print('\n');
        }
        System.out.print('\n');
    }
}
