package by.it.group410971.ratkevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int findFirstCovering(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        int result = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point && segments[mid].stop >= point) {
                result = mid;
                right = mid - 1; // ищем ЛЕВЫЙ подходящий
            } else if (segments[mid].start > point) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSort(segments, 0, segments.length - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int idx = findFirstCovering(segments, p);
            int count = 0;
            if (idx != -1) {
                for (int j = idx; j < segments.length && segments[j].start <= p; j++) {
                    if (segments[j].stop >= p) count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    void quickSort(Segment[] a, int left, int right) {
        while (left < right) {
            int lt = left, gt = right;
            Segment pivot = a[left];
            int i = left;

            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else i++;
            }

            // элиминация хвостовой рекурсии — сортируем меньшую часть рекурсивно, большую — циклом
            if (lt - left < right - gt) {
                quickSort(a, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(a, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            Segment that = (Segment) o;
            return Integer.compare(this.start, that.start);
        }
    }

}
