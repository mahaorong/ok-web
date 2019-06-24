const path = require('path');

module.exports = {
  //入口文件的配置项
  entry: {
    entry: ['./js/ajax.js', './bootstrap/bootstrapTreeview/js/bootstrap-treeview2.js']
  },
  //出口文件的配置项
  output: {
    //输出的路径，用了Node语法
    path: path.resolve(__dirname, 'dist'),
    //输出的文件名称
    filename: 'bundle2.js'
  },
  //模块：例如解读CSS,图片如何转换，压缩
  module: {
    rules: [
      //css压缩规则
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      },
      //图片压缩规则
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          //输出地址
          name: './images/[name].[hash:12].[ext]'
        }
      },
      //媒体文件压缩规则
      {
        test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          //输出地址          
          name: './media/[name].[hash:12].[ext]'
        }
      },
      //字体压缩规则
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          //输出地址          
          name: './fonts/[name].[hash:12].[ext]'
        }
      },
      //js 打包配置
      {
        test: /\.js$/, // 值一个正则，符合这些正则的资源会用一个loade来处理
        use: {
          loader: 'babel-loader', // 使用bable-loader来处理
          options: { // 指定参数
            presets: [
              ['babel-preset-env', {
                targets: {
                  browsers: ['> 1%', 'last 2 version'] //具体可以去babel-preset里面查看
                }
              }]

            ] // 指定哪些语法编译
          }
        },
        exclude: '/node_module/' // 排除在外
      }
    ]
  },
  //插件，用于生产模版和各项功能
  plugins: [
  ],
  //配置webpack开发服务功能
  devServer: {}
}