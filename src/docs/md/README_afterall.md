# Java 粒子系统 (Particle System)

## 项目简介
这是一个基于 Java 开发的 2D 粒子系统。该系统利用基本物理机制结合标准绘图库 (`StdDraw`) 进行实时可视化，可以模拟喷泉、爆炸、烟雾、下雨、烟花、鼠标产生喷泉以及漩涡等多种不同的粒子特效。

## 环境要求
- **Java**: JDK 8 或更高版本
- **外部依赖**: 无（项目中已直接内置 `StdDraw.java` 实现图形渲染）

## 编译和运行命令

在项目的根目录下打开命令行工具（终端/cmd/PowerShell），执行以下命令：

**Windows 命令行:**
```bat
:: 创建输出目录并编译所有源文件
if not exist bin mkdir bin
javac -d bin src/main/java/com/susan/partical/*.java

:: 运行主程序 (默认效果)
java -cp bin com.susan.partical.Main
```

*(如果项目根目录下的 `build.bat` 已经写好了自动构建逻辑，您可以直接双击或运行 `build.bat` 来执行编译和启动工作。)*

## 所有支持的效果模式
本项目支持丰富的粒子效果模式。在运行时，您可以通过 `-mode` 参数指定想要观看的特效：

- `fountain`: **喷泉**（默认），粒子从底部中心持续喷发，受重力影响抛物线落下。
- `explosion`: **爆炸**，粒子从中心瞬间向四周炸开，逐渐消散。
- `smoke`: **烟雾**，粒子不断向上缓缓飘散，伴随体积变大变淡。
- `rain`: **下雨**，粒子从画面顶部随机生成，受重力均匀直线掉落。
- `firework`: **烟花**，模仿一枚烟花升空后在空中引爆、散作多颗星火的过程。
- `mouse_fountain`: **鼠标跟随喷泉**，喷发位置会实时跟随您的鼠标指针。
- `vortex`: **漩涡**，粒子在一个中心点周围作旋转运动，受向心力等影响改变运动轨迹。

## 命令行参数说明
通过传递命令行参数，可以自定义开启粒子系统的哪种行为和属性：

- `-mode <string>`: 设置粒子效果模式。支持的值包括 `fountain`, `explosion`, `smoke`, `rain`, `firework`, `mouse_fountain`, `vortex`。
- `-rate <int>`: 设置每帧发射的新粒子数量（针对持续发射的模式）。调大该值使得画面更密集。
- `-dt <double>`: 设置每次物理更新的时间步长。值越大粒子运动速度越快，但模拟精度会降低。
- `-time <double>`: 设置粒子系统的最长运行时间（秒）。达到设定时间后程序直接退出。如果不设置（或设为常规负数等逻辑），系统将一直运行直至手动关闭窗口。

**运行示例（附带参数）：**
```bash
java -cp bin com.susan.partical.Main -mode vortex -rate 10 -dt 0.5 -time 60
```

## 如何修改配置文件
如果项目已经添加了配置文件功能（例如 `config.properties` 或者 YAML/JSON）：
1. 在项目根目录或 `src/main/resources/` 中找到配置文件。
2. 用文本编辑器打开该文件，直接修改对应字段（如 `mode=rain` 或 `rate=20`）。
3. 运行程序，程序在启动时会自动读取并加载该配置参数，覆盖默认属性。
*(注：如果目前只有命令行控制版本，未发现配置文件模块，则请完全通过上方的命令行参数进行调整)*

## 项目文件结构树
```text
Lab4_10255102520_宋易乐_particle_system/
│  build.bat               # 批处理构建与运行脚本
│  README.md               # 项目根目录说明文档
│
├─imgs/                    # 演示截图/GIF存图目录
│
└─src/
    ├─docs/                # 文档目录
    │  ├─chats/            # AI 辅助设定的聊天/Prompt记录
    │  └─md/
    │      README_Part2.md    # 功能迭代补充文档
    │      README_afterall.md # 本阶段的最终说明文档 (本文档)
    │
    └─main/                # 源码核心逻辑目录
        └─java/
            └─com/
                └─susan/
                    └─partical/
                        Main.java           # 程序入口点，用于解析参数并启动画布
                        Particle.java       # 粒子实体类，维护粒子的位置、质量、速度及颜色
                        ParticleSystem.java # 全局系统控制器，负责批量管理更新与渲染
                        StdDraw.java        # Princeton标准绘图库，内嵌于项目中
```