import java.util.Arrays;

public class Task5 {

    public static void main(String[] args) {
        int size = 10_000_000;
        int half = size / 2;
        float[] array = new float[size];
        Arrays.fill(array, 1.0f);

        firstMethod(array);
        try {
            secondMethod(array, size, half);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void firstMethod (float[] array) {
        Arrays.fill(array, 1.0f);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + i / 5.0) * Math.cos(0.2f + i / 5.0) * Math.cos(0.4f + i / 2.0));
        }
        System.out.println("One thread time: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static void secondMethod  (float[] array, int size, int half) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        float[] leftHalf = new float[half];
        float[] rightHalf = new float[half];

        System.arraycopy(array, 0, leftHalf, 0, half);
        System.arraycopy(array, 0, rightHalf, 0, half);

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run () {
                for (int i = 0; i < leftHalf.length; i++) {
                    leftHalf[i] = (float) (leftHalf[i] * Math.sin(0.2f + i / 5.0) * Math.cos(0.2f + i/ 5.0) * Math.cos(0.4f + i / 2.0));
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rightHalf.length; i++) {
                    rightHalf[i] = (float) (rightHalf[i] * Math.sin(0.2f + i / 5.0) * Math.cos(0.2f + i/ 5.0) * Math.cos(0.4f + i / 2.0));
                }
            }
        });

        thread1.start();
        thread2.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        float[] mergedArray = new float[size];
        for (float[] floats : Arrays.asList(leftHalf, rightHalf)) System.arraycopy(floats, 0, mergedArray, 0, half);
        System.out.println("Two thread time: " + (System.currentTimeMillis() - startTime) + " ms ");
    }
}
