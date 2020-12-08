package me.zhaotb.leecode.day20201203;


/**
 * 标记法：从2开始，选出质数，并标记出质数的倍数， 2、4、6、8、....  已经标记的，说明肯定是非质数，则不用再去判断了
 * 然后从没标记树取，取出3，并标记3的倍数，且从3*3开始标记，因为2*3肯定已经被2标记过了；后续同理，x从x*x开始标记
 * @author zhaotangbo
 * @since 2020/12/3
 */
public class PrimeNumber {

    public int countPrimes(int n) {
        if (n <= 1)return 0;
        int count = 0;
        int[] arr = new int[n];
        double sqrt = Math.sqrt(n);
        for (int i = 2; i < n; i++){
            if (arr[i] == 0 && isPrime(i)){
                count ++;
                if (i <= sqrt) {
                    for (int j = i * i; j < n; j += i) {
                        arr[j] = 1;
                    }
                }
            }
        }
        return count;
    }

    /**
     * 判断输入数字是否为质数。循环只需要去开方的值。
     * @param x 输入非负整数
     * @return true质数
     */
    private boolean isPrime(int x){
        double sqrt = Math.sqrt(x);
        for (int i = 2; i <= sqrt ; i++) {
            if (x % i == 0)return false;
        }
        return true;
    }

}
