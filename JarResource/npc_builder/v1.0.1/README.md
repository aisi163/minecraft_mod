<h1>NPC Builder v1.0.1 - 功能说明文档</h1>
<p>本目录为 NPC Builder v1.0.1 开源Jar资源的功能说明页，以下为该版本已实现的核心功能及命令说明。</p>

<h2>更新内容</h2>
<p style="font-size: 15px; line-height: 1.8; padding: 10px; background-color: #fff8f8; border-left: 4px solid #d73a4a;">
  <strong>如下</strong>：<br>
  1.优化了端处理代码结构<br> 
  2.添加了提示信息<br>
</p>

<h2>功能限制说明</h2>
<p style="font-size: 15px; line-height: 1.8; padding: 10px; background-color: #fff8f8; border-left: 4px solid #d73a4a;">
  <strong>注意</strong>：<br>
  1.「业务场景绑定」模块中的<strong>服务器跳转</strong>功能，因网易Minecraft平台接口限制，本版本暂未开发，相关命令仅为预留标识，执行后无实际效果。<br> 
  2. 上述代码为Forge gradle项目的源码部分，它不是完整的Forge gradle项目。
</p>

<h2>核心功能列表</h2>
<table style="width: 100%; border-collapse: collapse; margin: 20px 0; font-size: 14px; background-color: #f6f8fa;">
  <thead>
    <tr style="background-color: #eef2f7;">
      <th style="padding: 12px 8px; border: 1px solid #e1e4e8; text-align: left; font-weight: 600; width: 18%;">功能模块</th>
      <th style="padding: 12px 8px; border: 1px solid #e1e4e8; text-align: left; font-weight: 600; width: 22%;">核心命令示例</th>
      <th style="padding: 12px 8px; border: 1px solid #e1e4e8; text-align: left; font-weight: 600; width: 60%;">功能描述</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-weight: 500;">基础生命周期管理</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-family: monospace;">/npc create/list/remove</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; line-height: 1.5;">
        支持指定坐标/实体类型创建NPC，查看全量NPC列表，删除指定NPC（自动清理托管实体）；<br>
        支持`~`相对坐标，适配地图/服务器不同场景的位置需求。
      </td>
    </tr>
    <tr>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-weight: 500;">属性精准修改</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-family: monospace;">/npc move_to/rename/hosting_entity</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; line-height: 1.5;">
        移动NPC位置、修改NPC唯一标识/头顶标签名、更换NPC托管实体（如僵尸换骷髅）；<br>
        无需手动修改实体NBT标签，命令化操作更高效。
      </td>
    </tr>
    <tr>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-weight: 500;">详细信息查询</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-family: monospace;">/npc detail_info/equipment_get</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; line-height: 1.5;">
        查看NPC坐标、继承实体、UID等全量信息，查询NPC装备/手持物配置，替代原生NBT查询的复杂操作。
      </td>
    </tr>
    <tr>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-weight: 500;">业务场景绑定</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-family: monospace;">/npc add_affair/search_affair/drop_affair</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; line-height: 1.5;">
        为NPC绑定维度跳转、坐标跳转、命令执行、服务器跳转（预留）等业务；<br>
        每个NPC支持单一业务绑定，可查询/删除已绑定业务。
      </td>
    </tr>
    <tr>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-weight: 500;">装备管理</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-family: monospace;">/npc equipment set</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; line-height: 1.5;">
        为NPC托管实体（僵尸/骷髅/村民等）设置头部/胸部/主手等部位的装备，支持自定义物品ID和数量。
      </td>
    </tr>
    <tr>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-weight: 500;">智能帮助系统</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; font-family: monospace;">/npc help [子命令]</td>
      <td style="padding: 12px 8px; border: 1px solid #e1e4e8; line-height: 1.5;">
        提供分级帮助体系：总帮助展示所有子命令简要用法，指定子命令可查看详细参数、示例及注意事项；<br>
        支持命令tab补全，降低记忆成本。
      </td>
    </tr>
  </tbody>
</table>

<h2>使用注意事项</h2>
<ul style="line-height: 1.8; margin: 10px 0 20px 20px;">
  <li>所有命令需在Minecraft服务端控制台/游戏内管理员权限（OP）下执行，普通玩家无使用权限；</li>
  <li>相对坐标`~`的使用需遵循Minecraft原生规则（~x ~y ~z），例如`/npc create ~1 ~0 ~2 zombie`表示在玩家当前位置X+1、Y不变、Z+2处创建僵尸NPC；</li>
  <li>托管实体更换仅支持Minecraft原版实体类型，自定义实体需提前注册后才能使用。</li>
</ul>

<p style="color: #6a737d; font-size: 14px; border-top: 1px solid #e1e4e8; padding-top: 15px; margin-top: 20px;">
  最后更新时间：2026-02-25 | NPC Builder v1.0.1 功能说明
</p>
