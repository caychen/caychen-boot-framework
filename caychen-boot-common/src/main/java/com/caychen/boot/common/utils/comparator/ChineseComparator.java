package com.caychen.boot.common.utils.comparator;

import com.caychen.boot.common.utils.ChineseUtil;

import java.lang.reflect.Field;
import java.util.Comparator;

public class ChineseComparator<T> implements Comparator<T> {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <p>
     * In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.<p>
     * <p>
     * The implementor must ensure that <tt>sgn(compare(x, y)) ==
     * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>compare(x, y)</tt> must throw an exception if and only
     * if <tt>compare(y, x)</tt> throws an exception.)<p>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
     * <tt>compare(x, z)&gt;0</tt>.<p>
     * <p>
     * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
     * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
     * <tt>z</tt>.<p>
     * <p>
     * It is generally the case, but <i>not</i> strictly required that
     * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     * @throws NullPointerException if an argument is null and this
     *                              comparator does not permit null arguments
     * @throws ClassCastException   if the arguments' types prevent them from
     *                              being compared by this comparator.
     */
    @Override
    public int compare(T o1, T o2) {
        try {
            // 获取object的类型
            Class<? extends Object> clazz = o1.getClass();
            // 获取该类型声明的成员
            Field[] fields = clazz.getDeclaredFields();
            Object value1 = null;
            Object value2 = null;
            for (Field field : fields) {
                if ("name".equals(field.getName())) {
                    // 对于private私有化的成员变量，通过setAccessible来修改器访问权限
                    field.setAccessible(true);
                    // 获取对象的成员属性值
                    try {
                        value1 = field.get(o1);
                        value2 = field.get(o2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // 重新设置回私有权限
                        field.setAccessible(false);
                    }
                    break;
                }
            }
            if (value1 != null && value2 != null) {
                String s1 = ChineseUtil.getFirstSpell(value1.toString());
                String s2 = ChineseUtil.getFirstSpell(value2.toString());
                return s1.compareTo(s2);
            }
        } catch (Exception e) {

        } finally {

        }
        return 0;
    }

}
