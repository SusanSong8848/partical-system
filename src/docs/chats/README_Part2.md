## 新增ai实现的两种粒子效果（mouse_fountain(随鼠标移动喷泉)，vortex(龙卷风/旋涡)）：

6. firework：  
- 命令行输入：`java Main.java -mode mouse_fountain -rate 6`  
- 运行结果：  
  <video controls src="../../../imgs/mouseFountain.mp4" title="mouse_fountain"></video>  

7. firework：  
- 命令行输入：`java Main.java -mode vortex -rate 6`  
- 运行结果：  
  <video controls src="../../../imgs/vortex.mp4" title="vortex"></video>  



## 项目结构 (Project Structure)

```text
.
├── README.md
├── build.bat                  # 一键编译运行脚本
├── .gitignore                 # Git 忽略配置
├── imgs/                      # 演示图片与视频
│   └── explosion.mp4 ...
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── susan/
        │           └── partical/
        │                   Main.java
        │                   Particle.java
        │                   ParticleSystem.java
        │                   StdDraw.java
        └── resource/          # 资源文件
```

## 编译与运行 (Build and Run)

本项目使用 `javac` 结合批处理脚本进行管理，为了保持 `src` 目录的纯净，我们**不推荐**直接在此目录混合生成 `.class` 文件。
取而代之的是，利用提供的 `build.bat` 脚本实现一键编译，编译后的 `class` 文件会被输出至新建的 `out/` 输出目录内。

**Windows 系统下双击 `build.bat` 即可执行，或者在终端中运行：**
```bat
.\build.bat
```
*(如果需要运行特定模式如 `-mode smoke`，可手动执行：`java -cp out com.susan.partical.Main -mode smoke -rate 1`)*