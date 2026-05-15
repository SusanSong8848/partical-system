import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**粒子系统：控制粒子及粒子效果 */
public class ParticleSystem {
    private List<Particle> particles;
    private Emitter emitter;          // 发射器     //Emittet是函数式接口(接口定义在下面，main里面生成实例的时候再实现)
    private double damp;              // 全局阻尼
    private double boundDamp;         // 边界反弹的能量损失系数
    private double boundsXMin, boundsXMax, boundsYMin, boundsYMax;      //边界


    /**一个函数式接口: 发射器接口：每次调用(这个接口里的emit()函数)返回一个新建的粒子数组 */
    public interface Emitter{
        Particle[] emit();
    }


    /**
     * 构造粒子系统：控制粒子及粒子效果。
     * @param emitter  发射器实例(即已实现的)
     * @param damp     速度阻尼 (0~1，1 为无阻尼)
     * @param boundDamp 边界反弹能量保留系数 (0~1)
     * @param boundsXMin, boundsXMax, boundsYMin, boundsYMax  画布有效区域
     */
    public ParticleSystem(Emitter emitter, double damp, double boundDamp, 
                          double boundsXMin, double boundsXMax, double boundsYMin, double boundsYMax){
        this.particles = new ArrayList<>();
        this.emitter = emitter;
        this.damp = damp;
        this.boundDamp = boundDamp;
        this.boundsXMin = boundsXMin;
        this.boundsXMax = boundsXMax;
        this.boundsYMin = boundsYMin;
        this.boundsYMax = boundsYMax;
    }

    /** 发射一轮粒子（调用发射器并加入列表）（发射器是在main函数中实现的，这里作为新粒子加入particles）*/
    public void emit(){
        Particle []newParticles = emitter.emit();
        for (Particle p : newParticles){
            particles.add(p);
        }
    }

    /** 对所有粒子进行 dt 步长的更新，并处理边界反弹与死亡 */
    public void update(double dt) {
        /*寿命耗尽的粒子需要从列表中移除，因此必须使用显式迭代器配合 iter.remove().
        普通的 for‑each 循环（for (Particle p : particles)）在遍历期间如果直接调用 particles.remove(p) 会抛出 ConcurrentModificationException，因为内部的迭代器检测到集合被意外修改了。
而显式迭代器的 iter.remove() 是安全操作，它会删除刚刚 next() 返回的那个粒子，并且不会破坏迭代器的一致性。 */
        Iterator<Particle> iter = particles.iterator();     
        while (iter.hasNext()) {
            Particle p = iter.next();
            p.update(dt, damp);

            // 边界反弹
            if (p.getX() < boundsXMin) {
                // 直接修正位置并反转速度
                // 使用反射+位置修正
                p.setX(2 * boundsXMin - p.getX());       //此时boudsXMin为中位线
                p.setVx(-p.getVx() * boundDamp);
            } else if (p.getX() > boundsXMax) {
                p.setX(2 * boundsXMax - p.getX());
                p.setVx(-p.getVx() * boundDamp);
            }
            if (p.getY() < boundsYMin) {
                p.setY(2 * boundsYMin - p.getY());
                p.setVy(-p.getVy() * boundDamp);
            } else if (p.getY() > boundsYMax) {
                p.setY(2 * boundsYMax - p.getY());
                p.setVy(-p.getVy() * boundDamp);
            }

            // 移除死亡粒子
            if (!p.isAlive()) {
                iter.remove();
            }
        }

    }
    /** 在 StdDraw 画布上绘制所有粒子 */
    public void draw() {
        for (Particle p : particles) {
            StdDraw.setPenColor(p.getColor());
            StdDraw.filledCircle(p.getX(), p.getY(), p.getRadius());
        }
    }

    public int particleCount() { return particles.size(); }
}


