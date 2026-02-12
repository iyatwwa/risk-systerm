// 声明所有 .vue 文件都是一个 Vue 组件类型
declare module '*.vue' {
  // 从 vue 中导入 DefineComponent 类型
  import type { DefineComponent } from 'vue'

  // 定义一个具有任意 props、任意 raw 数据、任意 data 等的组件
  // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
  const component: DefineComponent<{}, {}, any>

  // 将该组件导出
  export default component
}
