import java.awt.Color;
/**
 * 一个粒子类
 */
public class Particle {
    private double x, y;          // 位置
    private double vx, vy;        // 速度
    private double ax, ay;        // 加速度
    private double life;          // 剩余寿命         
    private double maxLife;       // 初始寿命，用于计算透明度等（因为life的值会随时间改变，所以干脆弄一个初始值） 
    private double radius;        // 绘制半径
    private Color color;          // 颜色（包含 alpha）
    private double ra_variation;        // 新增：粒子大小变化
    /**
     * 创建一个粒子。
     * @param x ,y    初始坐标
     * @param vx ,vy   初始速度
     * @param ax ,ay  恒定加速度（例如重力）
     * @param life    寿命，会在更新中递减
     * @param radius  绘制半径
     * @param ra_variation  粒子大小变化率
     * @param color   颜色（java.awt.Color）
     */
    public Particle(double x, double y,
                    double vx, double vy,
                    double ax, double ay,
                    double life,
                    double radius, double ra_variation,
                    Color color){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        this.life = life;
        this.maxLife = life;
        this.radius = radius;
        this.ra_variation = ra_variation;
        this.color = color;
    }

    /**
     * @param dt    时间步长
     * @param damp  速度阻尼因子（0 ~ 1），1代表无阻尼
     */
    public void update(double dt, double damp){
        // 速度更新
        vx = (vx + ax * dt) * damp;
        vy = (vy + ay * dt) * damp;
        // 位置更新
        x += vx * dt;
        y += vy * dt;
        // 寿命递减
        life -= dt;
        // 新增：半径变化：
        radius = radius * ra_variation;
    }

    // getter 用于绘图（在ParticleSystem.update里面用）
    public double getX() { return x; }
    public double getY() { return y; }
    public double getVx() { return vx; }
    public double getVy() { return vy; }

    public double getRadius() { return radius; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setVx(double vx) { this.vx = vx; }
    public void setVy(double vy) { this.vy = vy; }

    /** 是否还活着 */
    //后面ParticleSystem.update要用
    public boolean isAlive() {
        return life > 0;
    }

    /** 用于绘制透明的颜色（根据当前寿命比例调整） */
    public Color getColor() {
        float alpha = (float) (life / maxLife);     //透明度
        if (alpha < 0) alpha = 0;
        if (alpha > 1) alpha = 1;
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return new Color(red, green, blue, (int)(alpha * 255));
        //这里说一下为什么要重新调用构造函数，因为p.color是一直在变的，因为alpha是private，是在getColor()里才被修正的。
    }
}
