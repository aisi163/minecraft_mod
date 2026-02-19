<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>JarPackage 目录说明</title>
    <style>
        /* 适配GitHub浅色/深色模式，保持视觉统一 */
        :root {
            --bg-color: #ffffff;
            --text-color: #24292e;
            --border-color: #e1e4e8;
            --accent-color: #0366d6;
            --note-bg: #f6f8fa;
        }
        @media (prefers-color-scheme: dark) {
            :root {
                --bg-color: #0d1117;
                --text-color: #c9d1d9;
                --border-color: #30363d;
                --accent-color: #58a6ff;
                --note-bg: #161b22;
            }
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            line-height: 1.6;
            color: var(--text-color);
            background-color: var(--bg-color);
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            text-align: center;
            padding: 20px 0;
            border-bottom: 2px solid var(--accent-color);
            margin-bottom: 30px;
        }
        .header h1 {
            margin: 0;
            font-size: 28px;
            color: var(--accent-color);
        }
        .content {
            background-color: var(--note-bg);
            border-radius: 8px;
            padding: 25px;
            border: 1px solid var(--border-color);
            margin-bottom: 25px;
        }
        .content h2 {
            font-size: 20px;
            margin-top: 0;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .content ul {
            padding-left: 20px;
            margin: 10px 0;
        }
        .content li {
            margin-bottom: 10px;
        }
        .highlight {
            font-weight: 600;
            color: var(--accent-color);
        }
        .usage-tips {
            background-color: rgba(88, 166, 255, 0.1);
            border-left: 4px solid var(--accent-color);
            padding: 15px;
            margin-top: 20px;
            border-radius: 0 4px 4px 0;
        }
        .footer {
            text-align: center;
            color: #6a737d;
            font-size: 14px;
            padding-top: 20px;
            border-top: 1px solid var(--border-color);
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Minecraft 模组 Jar 包目录</h1>
        <p>Minecraft Mod Jar Package Directory</p>
    </div>

    <div class="content">
        <h2>目录说明</h2>
        <p>本目录 <span class="highlight">不存储源码文件</span>，仅存放可直接部署/使用的 Minecraft 模组 Jar 包，所有文件均为编译后的成品，可直接用于服务器/客户端部署。</p>
        
        <h2>包含的 Jar 包类型</h2>
        <ul>
            <li><strong>NPC Builder v1.0.0</strong>：双端适配的 NPC 构建工具 Jar 包，支持 Java 服务器</li>
            <li><strong>待上线工具</strong>：World Creator、Biome Generator 等工具的 Jar 包将随版本发布同步更新至此</li>
        </ul>

        <div class="usage-tips">
            <h3>使用提示</h3>
            <ul>
                <li>Jar 包可直接放入 Minecraft 服务器的 <code>mods</code> 目录启动使用</li>
                <li>如需查看/修改源码，请前往仓库根目录或对应模块的源码分支</li>
                <li>所有 Jar 包均已通过基础功能测试，如有运行问题可提交 Issues 反馈</li>
            </ul>
        </div>
    </div>

    <div class="footer">
        <p>最后更新时间：2026-02-19 | Minecraft 模组开发工具集</p>
    </div>
</body>
</html>
