# Lab4_10255102520_宋易乐_particle_system

-----

# 第一部分：

## 1.  
__比起前两个类，这次的particle我尝试加入javadoc来使类更可读__  
- 粒子类：[ParticleSystem.java的代码](src/main/java/com/susan/partical/ParticleSystem.java)  
- 我新增了粒子属性 `ra_variationprivate` ，因为烟雾特效的粒子半径会变大;
  >double ra_variation;        // 新增：粒子大小变化


## 2.
- 粒子系统类：[ParticleSystem.java的代码](src/main/java/com/susan/partical/ParticleSystem.java)  

- - 这个Emitter是一个 __函数类接口__（只有一个emit()抽象函数），方便在后面main函数中创建 __【实现了emit()的Emitter的对象】__（意思就是创建对象的时候再去实现，这样就可以实现每个对象的emit()都可以不同（如喷泉，爆炸，烟雾）--> __多态__）  
  - 这里main函数里的对象定义为emmiter，通过函数不同调用使emitter实现不同的特性（喷泉，爆炸，烟雾）  
    ```
    // 不是 new Emitter()，而是 new 一个实现了 Emitter 的匿名类实例  
    emitter = new ParticleSystem.Emitter() {  
        @Override
        public Particle[] emit() { ... }
    };

    // Lambda 语法糖，意思完全一样，编译器自动生成匿名类
    emitter = () -> { ... };
    ```
  - 创建好了过后把它的引用给system.emitter(ParticleSystem的对象)  
  - 好处：__通过函数式接口 Emitter，不同效果只需提供不同的 Emitter 实现，ParticleSystem 本身不与具体效果耦合。__  

## 3.  

不同粒子系统的具体实现：[Main.java的代码](src/main/java/com/susan/partical/Main.java)  

1. fountain：  
- 命令行输入：`java Main.java -mode fountain -rate 6 -dt 0.02`  或者直接 `java Main.java`(因为是喷泉模式是默认的)
- 运行结果：  
  <video controls src="imgs/fountain.mp4" title="fountain"></video>  

2. explosion：  
- 命令行输入：`java Main.java -mode explosion -rate 500`  
- 运行结果：  
  <video controls src="imgs/explosion.mp4" title="explosion"></video>  

3. smoke：  
- 命令行输入：`java Main.java -mode smoke -rate 1`  
- 运行结果：  
  <video controls src="imgs/smoke.mp4" title="smoke"></video>  

4. rain：  
- 命令行输入：`java Main.java -mode rain -rate 20`  
- 运行结果：  
  <video controls src="imgs/rain.mp4" title="rain"></video>

5. firework：  
- 命令行输入：`java Main.java -mode firework -rate 200`  
- 运行结果：  
  <video controls src="imgs/firework.mp4" title="firework"></video>  







-----

# 第二部分：
啊no学长，这个部分我没明白你的意思（
- - __“3.实现并展示三种粒子效果之一”，这个我以为是手动实现3个，然后就都靠手动实现了fountain, explosion, smoke。 然后我又觉得好玩，就手动实现了另外两种（rain，firework）。__），
  - __所以我在“第一部分：手动编程”实现了5种效果。__
  - __而且最重要的是`反弹：对应速度分量取反 / 加入能量损失：例如碰撞后速度乘以 λ∈(0,1)`这个效果：我看您给的示例喷泉视频里面有反弹和弹跳能量的损失，我就以为我自己也要手动编写`反弹和反弹能量损失`的效果，所以就也手动实现了。__ 

- 所以我就把自己vibecoding的内容就变成了：
  - 使用鼠标指针或其他任何方式控制粒子的发射点（点哪就从哪喷射）。
  - 先用鼠标定位的效果做一个喷泉（改良喷泉）
  - 一种全新的效果（例如“漩涡/龙卷风”，我还没做过）
  - 练习“用 AI 编程助手生成代码 + 审查整合（因为我之前的代码也写了很多，要保证之前的能运行，融合起来也很有难 度）”

## README_Part2  
后续ai写的第二部分的README内容在 __README_Part2__ 里，点击跳转：
[README_Part2](src/docs/chats/README_Part2.md)