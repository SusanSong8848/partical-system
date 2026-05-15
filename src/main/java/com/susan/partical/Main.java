import java.awt.Color;
import java.util.Random;
public class Main {
    public static void main(String []args){
        //默认参数：
        String mode = "fountain";                   //（可命令行输入）
        int rate = 6;            // 每帧发射粒子数      //（可命令行输入）
        double dt = 0.02;        // 模拟步长        //（可命令行输入）
        double totalTime = 10.0; // 最大运行时间（秒）      //（可命令行输入）
        double damp = 0.99;      // 空气阻力
        double boundDamp = 0.8;  // 碰撞能量保留  

        // 简单命令行解析
        for (int i = 0; i < args.length; ++i){
            if (args[i].equals("-mode")) mode = args[++i];
            else if (args[i].equals("-rate")) rate = Integer.parseInt(args[++i]);
            else if (args[i].equals("-dt")) dt = Double.parseDouble(args[++i]);
            else if (args[i].equals("-time") || args[i].equals("-time") || args[i].equals("-time"))
                totalTime = Double.parseDouble(args[++i]);
        }

        // 画布设置（使用 StdDraw）
        StdDraw.setScale();
        StdDraw.enableDoubleBuffering(); // 双缓冲避免闪烁

        

        // 根据模式创建发射器
        ParticleSystem.Emitter emitter = null;

        switch(mode){
            case "fountain": 
                emitter = createFountainEmitter(rate);
                boundDamp = 0.3;        //喷泉水只在地面反弹，而且反弹没那么多
                break;
            case "explosion":
                emitter = createExplosionEmitter(rate);
                //在命令行中设置：rate = 500;     //一次性发射 200 个粒子，向四周均匀扩散
                break;
            case "smoke":
                emitter = createSmokeEmitter(rate);
                boundDamp = 0.2;        //烟雾反弹没那么多
                break;
            case "rain":      // 自定义效果（第二部分）
                emitter = createRainEmitter(rate);
                boundDamp = 0.0;
                break;
            case "firework":  // 另一种自定义效果
                emitter = createFireworkEmitter(rate);
                break;
            default:
                System.out.println("Unknown mode: " + mode);
                System.exit(1);
        }
        


        //运行时的系统设置：
        ParticleSystem system = new ParticleSystem(emitter, damp, boundDamp,
                0.0, 1.0, 0.0, 1.0);

        double frameTime = 0.026; // 约等于38 fps   
        // /*frameTime = 0.016 对我当前的电脑来说，确实可能要求太高了，反而导致画面“断断续续”。
        // 将帧时间放宽到 0.026 秒后，电脑有更多喘息空间，动画自然就流畅了。 */
        double elapsed = 0;     //已经经过了几秒（时间进程）


        //真正开始操作屏幕：
        while(elapsed < totalTime){
            StdDraw.clear();    //清空屏幕到默认色
            system.emit();      //释放新一轮粒子
            system.update(dt);      //跟新所有粒子状态
            system.draw();      //绘图（把所有粒子画到图上）
            StdDraw.show();             //前面使用了双缓冲，所以这里可以用
            elapsed += dt;
            StdDraw.pause((int)(frameTime * 1000)); // 控制帧率     //暂停t（毫秒）。该方法旨在支持计算机动画
        /*通常电影 24 fps，游戏 30 / 60 fps。(frames per second 帧率fps)
        本例 frameTime = 0.016 秒 ≈ 16 毫秒，意味着 1/0.016 ≈ 62.5 fps，约 60 帧/秒。
        人眼在 24 fps 以上就会感觉画面流畅，60 fps 非常平滑。 */
        
        /*frameTime = 0.016 对我当前的电脑来说，确实可能要求太高了，反而导致画面“断断续续”。
        将帧时间放宽到 0.026 秒后，电脑有更多喘息空间，动画自然就流畅了。 */
        }
        System.exit(0);     //循环结束后，程序将执行 System.exit(0)，立即终止 Java 虚拟机，关闭图形窗口。   
                                    // 若不写这句，程序可能仍然挂起，窗口不会自动关闭，需要手动点击关闭按钮。
    }




    // ---------- 具体发射器实现 ----------

    /** 1.喷泉发射器：从 (x, y) = (0.5, 0.01) 持续向上扇形发射 */
    private static ParticleSystem.Emitter createFountainEmitter(int rate){
        return new ParticleSystem.Emitter() {
            public @Override
            Particle[] emit() {
                // TODO Auto-generated method stub
                Particle []batch = new Particle[rate];
                for (int i = 0; i < rate; ++i){
                    double speed = 1.9 + Math.random() * 0.8;    //速度在1.9 - 2.7（v^2 = 2gy(这里g我设的是-4.0因为不想那么快)，要想y大概在0.8(屏幕的3/4)，则v要在2.5）
                    double angle = 75 + Math.random() * 30;     //发射的角度75到105度
                    double rad = Math.toRadians(angle);         //转换为弧度radians
                    double vx = speed * Math.cos(rad);
                    double vy = speed * Math.sin(rad);

                    double life = 1.5 + Math.random() * 0.5;        //可用于计算alpha，在Particle里
                    double r = 0.005 + Math.random() * 0.005;        //radius半径在0.005 - 0.01
                    Color c = new Color(30, 144, 255);   // 蓝色调
                    batch[i] = new Particle(0.5, 0.01, vx, vy, 0, -4.0, life, r, 1.0, c);        //为了速度变化没那么快，更舒缓，我把g设为-4.0
                }
                return batch;
            }
        };
    }


    /** 2.爆炸发射器：一次性发射 500 个粒子（或按 rate 参数），向四周均匀扩散 */
    private static ParticleSystem.Emitter createExplosionEmitter(int rate) {
        return new ParticleSystem.Emitter() {
            boolean exploded = false;
            @Override
            public Particle[] emit() {
                if (exploded) return new Particle[0];
                exploded = true;
                Particle[] batch = new Particle[rate];
                for (int i = 0; i < rate; i++) {
                    double angle = Math.random() * 360;
                    double rad = Math.toRadians(angle);
                    double speed = 0.3 + Math.random() * 0.7;
                    double vx = Math.cos(rad) * speed;
                    double vy = Math.sin(rad) * speed;
                    double life = 1.0 + Math.random() * 1.0;
                    double r = 0.008 + Math.random() * 0.004;
                    Color c = new Color(255, 100 + (int)(Math.random()*155),
                            0); // 橙红到黄
                    batch[i] = new Particle(0.5, 0.5, vx, vy,
                            0, -1.0, life, r, 1.0, c);
                }
                return batch;
            }
        };
    }

    /** 3.烟雾发射器：从底部一个区域缓慢上浮 */
    private static ParticleSystem.Emitter createSmokeEmitter(int rate) {
        return () -> {
            Particle[] batch = new Particle[rate];
            for (int i = 0; i < rate; i++) {
                double x = 0.4 + Math.random() * 0.2;
                double y = 0.05 + Math.random() * 0.02;
                double vx = (Math.random() * 2 - 1.0) * 0.2;
                double vy = 0.1 + Math.random() * 0.3;
                double life = 2.5 + Math.random() * 2.0;
                double r = 0.02 + Math.random() * 0.03;
                Color c = new Color(180, 180, 180); // 灰白色烟雾
                batch[i] = new Particle(x, y, vx, vy,
                        0, 0.5, life, r, 1.01, c); // 轻微向上加速度     //粒子不断变大：ra_variation = 1.01
            }
            return batch;
        };
    }
    
    // 下面两个为第二部分扩展效果 ----------
    /** 4.雨滴效果 */
    private static ParticleSystem.Emitter createRainEmitter(int rate){
        return new ParticleSystem.Emitter() {
            Particle[] batch = new Particle[rate];
            public Particle[] emit(){
                for (int i = 0; i < rate; i++) {
                double x = Math.random();            // 全屏随机 x
                double y = 1.0;                      // 从顶部下落
                double vx = 0;
                double vy = -3.0 - Math.random() * 3.0; // 快速下落
                //double life = 0.5;  // 不必要，到边界会消失
                double r = 0.003;
                batch[i] = new Particle(x, y, vx, vy,
                        0, -2, 5.0, r, 1.0, new Color(100, 100, 255));
            }
            return batch;
            }
           
        };
    }

    /** 烟花效果（简单版）：先像爆炸一样散开，粒子带尾迹（此处仅为单次爆炸） */
    private static ParticleSystem.Emitter createFireworkEmitter(int rate) {
        return new ParticleSystem.Emitter() {
            boolean launched = false;
            @Override
            public Particle[] emit() {
                if (launched) return new Particle[0];
                launched = true;
                Particle[] batch = new Particle[rate];
                for (int i = 0; i < rate; i++) {
                    double angle = Math.random() * 360;
                    double rad = Math.toRadians(angle);
                    double speed = 0.2 + Math.random() * 0.8;
                    double vx = Math.cos(rad) * speed;
                    double vy = Math.sin(rad) * speed;
                    double life = 0.8 + Math.random() * 0.6;
                    double r = 0.008;
                    Color c = Color.getHSBColor((float)Math.random(), 1.0f, 1.0f);
                    batch[i] = new Particle(0.5, 0.6, vx, vy,
                            0, 0.0, life, r, 1.0, c);
                }
                return batch;
            }
        };
    }



}
