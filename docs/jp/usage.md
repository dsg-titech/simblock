# SimBlockユーザガイド

## 1. 環境準備
Windows、Linux、MacOS等のJavaをサポートするオペレーティングシステムで利用可能です。  
SimBlockの利用には、以下に示すバージョンのJDKとGradleが必要です。

なお、SimBlockの公開リポジトリにはGradle wrapperを同梱してありますので、Gradleは利用時に自動インストールすることもできます（手順は後述します）。

| ソフトウェア | バージョン |
|:-----------|:------------|
| JDK | 1.8.0以降 |
| Gradle | 5.1.1以降 |

## 2. ダウンロード
GitHub上からZIPファイルをダウンロードして解凍するか、もしくは、SimBlockのリポジトリをクローンします。  

リリース一覧：　[https://github.com/dsg-titech/simblock/releases](https://github.com/dsg-titech/simblock/releases)  
クローンコマンド：　`$ git clone git@github.com:dsg-titech/simblock.git`

### 2-a. ディレクトリ構成
解凍/クローンしたディレクトリの内部は、以下のような構成になっています。

```
simblock
+-- docs
+-- gradle
|   +-- wrapper
+-- simulator
    +-- src
        +-- dist
        |   +-- conf
        |   +-- out
        |       +-- graph
        +-- main
            +-- java
                +-- SimBlock
                    +-- ...
                    :
```

| ディレクトリ | 説明 |
|:-----------|:------------|
| *docs* | マニュアル類 |
| *gradle/wrapper* | Gradle wapper用のディレクトリ |
| *simulator/src/dist/conf* | シミュレータが読み込むファイルを置くディレクトリ |
| *simulator/src/dist/out* | シミュレータが出力するファイルが格納されるディレクトリ |
| *simulator/src/main/java/SimBlock* | シミュレータ本体のソースコード |

## 3. ビルド
以降では、解凍/クローンしたリポジトリのルートディレクトリの名前を、 *\<ROOT_DIR\>* と表記します。  
ターミナル（xterm、コマンドプロンプト等）を開き、 *\<ROOT_DIR\>* に移動します。  

### 3-a. Gradleをインストール済みの場合
Gradleをインストール済みの場合、以下のGradleコマンドを実行することでSimBlockをビルドできます。

`$ gradle build`

成功すると、 *\<ROOT_DIR\>/simulator* 配下に *build* ディレクトリが生成されます。  
*build* ディレクトリは以下のような構成になっています。

```
<ROOT_DIR>
+-- simulator
    +-- build
        +-- classes
        +-- distributions
        +-- libs
        +-- scripts
        +-- tmp
```

| ディレクトリ | 説明 |
|:-----------|:------------|
| *simulator/build/classes* | ビルドされたクラスファイル群が格納されるディレクトリ |
| *simulator/build/distributions* | 配布用アーカイブ（zip, tar）が格納されるディレクトリ |
| *simulator/build/libs* | jarファイルが格納されるディレクトリ |
| *simulator/build/scripts* | 起動スクリプトが格納されるディレクトリ |

なお、 *simulator/build/scripts* の起動スクリプトは、配布用アーカイブに含めるために生成されているものであり、このディレクトリ構成のまま実行してもエラーになりますのでご注意ください。

### 3-b. Gradleを未インストールの場合
Gradleをインストールしていない場合、Gradleコマンドを実行する代わりに、 *\<ROOT_DIR\>* にある *gradlew*（Windowsの場合は *gradlew.bat* ）を使うことができます。

`$ gradlew build`

これはGradle wrapperと呼ばれるプログラムで、Gradleがインストールされていない場合には自動でインストールを行い、インストールしたGradleを呼び出してくれます。  
つまり、Gradleコマンドの代わりに *gradlew* （ *gradlew.bat* ）を使うことができます。

### 3-c. ビルド生成物の破棄
以下のGradleコマンドを実行すると、Gradleのビルドによって生成されたファイル群が削除されます。

`$ gradle clean`

ビルドを実行する前に一度cleanしたい場合は、以下のようにまとめて実行を指示することもできます。

`$ gradle clean build`

この場合、clean→build、の順序で実行されます。

## 4. 実行
シミュレータを実行する手段は大きく2つあります。  
Gradleコマンドによる実行と、ビルド生成物を用いた実行です。

### 4-a. Gradleコマンドによる実行
*\<ROOT_DIR\>* にて、以下のGradleコマンドを実行することで、シミュレータが起動します。

`$ gradle :simulator:run`

シミュレータが出力するファイルは、 *\<ROOT_DIR\>/simulator/src/dist/output* ディレクトリ配下に格納されます。

### 4-b. ビルド生成物を用いた実行
*\<ROOT_DIR\>/simulator/build/distributions* の配布用アーカイブ（zip, tar）を解凍します。  
すると、以下のようなディレクトリ構成が現れます。

```
SimBlock
+-- bin
+-- conf
+-- lib
+-- output
```

この、 *bin* ディレクトリの中に、実行用のスクリプトが入っています。
ターミナルから、 *runSimBlock* （Windowsの場合は *runSimBlock.bat* ）を実行することで、シミュレータが起動します。  
シミュレータが出力するファイルは、 *output* ディレクトリ配下に格納されます。

### 4-c. その他の実行方法
配布用アーカイブを解凍した中には、シミュレータのjarファイルが含まれています（ *lib/simulator.jar* ）。  
このjarファイルを直接javaコマンドで実行することも、もちろん可能です。ただし、クラスパス等を適切に設定する必要があります。

また、Eclipse等のIDE上から実行することもできます。  
これについては、後述するIDEへの取り込みの中で説明します。

## 5. シミュレータのパラメータ設定
| パラメータ | 場所 | 説明 |
|:-----------|:------------|:------------|
| 地域名 | *BlockChainSimulator.settings.NetworkConfiguration#REGION_LIST* | ノードの存在する地域の一覧。デフォルトでは6地域です。 |
| 各地域の遅延 | *BlockChainSimulator.settings.NetworkConfiguration#LATENCY* | 地域ごとの遅延。 |
| 各地域の送信帯域 | *BlockChainSimulator.settings.NetworkConfiguration#UPLOAD_BANDWIDTH* | 地域ごとの受信帯域幅。 |
| 各地域の受信帯域 | *BlockChainSimulator.settings.NetworkConfiguration#DOWNLOAD_BANDWIDTH* | 地域ごとの送信帯域幅。 |
| 地域分布 | *BlockChainSimulator.settings.NetworkConfiguration#REGION_DISTRIBUTION* | ノードの地域分布。 |
| 次数分布 | *BlockChainSimulator.settings.SimulationConfiguration#DEGREE_DISTRIBUTION* | アウトバウンドの個数を表します。詳しくはMirror論文参照（Andrew Miller et al., "Discovering bitcoin's public topology and influential nodes", 2015.） |
| ブロック生成間隔 | *BlockChainSimulator.settings.SimulationConfiguration#INTERVAL* | ブロック生成間隔。 |
| ルーティングテーブル | *BlockChainSimulator.settings.SimulationConfiguration#TABLE* | ルーティングテーブルの種類を表します。 |
| ブロック高 | *BlockChainSimulator.settings.SimulationConfiguration#ENDBLOCKHEIGHT* | 何ブロック生成後にシミュレーションを終えるかを表します。 |
| ブロックサイズ | *BlockChainSimulator.settings.SimulationConfiguration#BLOCKSIZE* | ブロックサイズ。 |
| ノード数 | *BlockChainSimulator.settings.SimulationConfiguration#NUM_OF_NODES* | ブロックチェーンネットワーク参加ノード総数。|

シミュレータのパラメータは *SimBlock.settings* パッケージ内の2つのクラス、 *NetworkConfiguration* と *SimulationConfiguration* に書かれています。
前者はネットワーク関係のパラメータを、後者はブロックチェーンネットワークに関するパラメタを保持しています。
ユーザーはこれらのファイル内の変数の値を書き換えてビルドすることで、シミュレーションの設定を変えることができます。

## 6. シミュレータの出力
シミュレータは、シミュレーションの実行結果を標準出力およびファイルに出力します。
ファイル出力の場所は、Gradleコマンドによる実行の場合は *\<ROOT_DIR\>/simulator/src/dist/out* 、
ビルド生成物を用いた実行の場合は *SimBlock/output* です。

出力される内容は以下のとおりです。

- 標準出力および *out.txt*
    - ブロックIDの行が出力された後に、以下が列挙されます。
        - ＜ノードID, 時間＞
            - 時間 : 当該ブロックIDのブロックが、生成されてから当該ノードIDのノードに到達するまでの時間。
- *Blocklist.txt*
    - ＜フォーク情報, ブロック高, ブロックID＞
        - フォーク情報 : 「OnChain」「Orphan」のいずれか。前者がメインチェーン、後者がフォークを表します。
- *graph* ディレクトリ配下
    - 数字.txt : 「数字」のブロック高のときのネットワーク情報
        - ＜ノードID, ノードID＞
            - 左のノードIDから右のノードIDへのコネクションを意味します。
- *output.json*
	- 発生したイベントの情報が列挙されます。本ファイルを[SimBlock Visualizer](https://github.com/dsg-titech/simblock-visualizer)に読み込ませることで、可視化を実行できます。
		- ＜イベントの種類, データ内容＞
		- イベントの種類
			- add-link : 隣接ノードの追加。
			- remove-link : 隣接ノードの除去。
			- flow-block : ブロックの送信。
			- simulation-end : シミュレーションの終了。
		- データ内容
			- timestamp : イベントの時系列を表すタイムスタンプ。
			- block-id : ブロックのID。
			- transmission-timestamp : 送信時のタイムスタンプ。
			- reception-timestamp : 受信時のタイムスタンプ。
			- begin-node-id : 起点ノード。
			- end-node-id : 終点ノード。

## 7. IDEへの取り込み
Gradleを使って、IDEの設定ファイルを生成し、簡単にIDEにインポートすることができます。

### 7-a. IntelliJ IDEAの場合
以下のGradleコマンドを実行すれば、IntelliJ IDEA用の各種設定ファイルを生成できます。

`$ gradle idea`

成功すると、以下のファイル群が生成されます。

| ファイル |
|:-----------|
| *\<ROOT_DIR\>/\<ROOT_DIR\>.iml* |
| *\<ROOT_DIR\>/\<ROOT_DIR\>.ipr* |
| *\<ROOT_DIR\>/\<ROOT_DIR\>.iws* |
| *\<ROOT_DIR\>/simulator/simulator.iml* |

IntelliJ IDEAを起動し、 **File -> Open** から *\<ROOT_DIR\>.ipr* を選択すれば、プロジェクトとして開くことができます。  
*\<ROOT_DIR\>/simulator* はモジュールとしてインポートされる形になります。  
ここまでくれば、IDE上からシミュレータを実行可能です。  
例えば、プロジェクト構造のツールウィンドウから *\<ROOT_DIR\>/simulator/src/main/java/SimBlock/simulator/Main.java* を選択し、右クリックして **Run 'Main.main()'** を実行することで、シミュレータが起動します。  
シミュレータが出力するファイルは、 *\<ROOT_DIR\>/simulator/src/dist/output* ディレクトリ配下に格納されます。

#### 生成物の破棄
以下のGradleコマンドを実行すると、Gradleのideaタスクによって生成されたファイル群が削除されます。
（※ *\<ROOT_DIR\>.iws* ファイルは削除されません。また、IDEでプロジェクトを開いた状態のままの場合、削除されたファイルをIDE側で自動的に再生成する場合があります。）

`$ gradle cleanIdea`

ideaタスクを実行する前に一度cleanIdeaしたい場合は、以下のようにまとめて実行を指示することもできます。

`$ gradle cleanIdea idea`

この場合、cleanIdea→idea、の順序で実行されます。

### 7-b. Eclipseの場合
以下のGradleコマンドを実行すれば、Eclipse用の各種設定ファイルを生成できます。

`$ gradle eclipse`

成功すると、以下のファイル・ディレクトリ群が生成されます。

| ファイル |
|:-----------|
| *\<ROOT_DIR\>/.project* |
| *\<ROOT_DIR\>/simulator/.classpath* |
| *\<ROOT_DIR\>/simulator/.project* |
| *\<ROOT_DIR\>/simulator/.settings* |

Eclipseを起動し、 **File -> Open Projects from File System** にて、 **Import source** として *\<ROOT_DIR\>* を選択し、リストアップされる2つのEclipse projectにチェックを付けて **Finish** ボタンを押します。  
*\<ROOT_DIR\>/simulator* と *\<ROOT_DIR\>* がそれぞれプロジェクトとしてインポートされる形になります。  
ここまでくれば、IDE上からシミュレータを実行可能です。  
例えば、パッケージエクスプローラから simulator プロジェクトを選択し、右クリックして  **Run As -> Java Application** を実行することで、シミュレータが起動します。  
シミュレータが出力するファイルは、 *\<ROOT_DIR\>/simulator/src/dist/output* ディレクトリ配下に格納されます。

#### 生成物の破棄
以下のGradleコマンドを実行すると、Gradleのeclipseタスクによって生成されたファイル群が削除されます。

`$ gradle cleanEclipse`

eclipseタスクを実行する前に一度cleanEclipseしたい場合は、以下のようにまとめて実行を指示することもできます。

`$ gradle cleanEclipse eclipse`

この場合、cleanEclipse→eclipse、の順序で実行されます。

