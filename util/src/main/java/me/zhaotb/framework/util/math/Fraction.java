package me.zhaotb.framework.util.math;

/**
 * @author zhaotangbo
 * @date 2019/2/22
 */
public class Fraction {

    /**
     * 分子
     */
    private int numerator;

    /**
     * 分母
     */
    private int denominator;

    /**
     *
     * @param numerator 分子
     * @param denominator 分母
     */
    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * 加法
     * @param another 另一个分数
     * @return 返回相加后的分数
     */
    public Fraction add(Fraction another){
        int lcm = MathUtil.lcm(this.denominator, another.denominator);
        return new Fraction(this.numerator * lcm / this.denominator + another.numerator * lcm / another.denominator, lcm);
    }



    /**
     * @return 返回化简后的分数
     */
    public Fraction simple(){
        int gcd = MathUtil.gcd(this.numerator, this.denominator);
        return new Fraction(this.numerator / gcd, this.denominator / gcd);
    }

    @Override
    public String toString() {
        return this.denominator == 1 ? String.valueOf(this.numerator) : this.numerator + "/" + this.denominator;
    }
}
