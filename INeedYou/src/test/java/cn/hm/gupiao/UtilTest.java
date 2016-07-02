package cn.hm.gupiao;

import cn.hm.gupiao.util.CircleArray;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by huangming on 2016/7/2.
 */
public class UtilTest {

    @Test
    public void test() {
        CircleArray<Integer> c = new CircleArray<>(10);
        ArrayBlockingQueue queue = new ArrayBlockingQueue(10);
        for (int i = 0; i < 20; i++) {
            if (i > 9) {
                queue.poll();
            }
            c.add(i);
            queue.add(i);

            for (Integer integer : c) {
                System.out.print(integer);
                System.out.print(" ");
            }
            System.out.println();

            for (Object o : queue) {
                System.out.print(o);
                System.out.print(" ");
            }
            System.out.println();
            System.out.println("比较!---------");

        }


    }

}
