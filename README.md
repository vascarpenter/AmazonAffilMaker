## AmazonAffilMaker

### swingバージョンの affiliate link maker

- amazonのASINからリンクを作るプログラム
  - AmazonAfiComposeの swing UI バージョン
- やはり Swing UI Designerは簡単でいい
  - 某EGMAINシステムがJava1.6からJava1.8になった
    - Java11以降ではないのでJetpack Compose Desktopが使えないことが危惧されたが、これでちょっと安心

- gradle対応にするにはUIをjavaソースコードで書き出す必要がある
- IntelliJでgui.java内に$$$で始まるGUI生成コードを作らせるには
  - Settings... (IntelliJ IDEAメニュー)から
  - エディター＞GUIデザイナーから Java ソースコードを選ぶ
  - ビルド、実行、デプロイ＞ビルドツール＞Gradle から 「ビルドおよび実行に使用:」のポップアップメニューでGradleではなくIntelliJ IDEAを選ぶ
  - そして gui.java をビルドメニューから再コンパイルすると`$$$setupUI$$$();`というようなコードができる
  - ちなみに生成された`$$$setupUI$$$()`の中身はgui.formから生成された物で、毎回 gui.form (GUI)を変更するたびに上記手続きが必要
  - 生成後は"ビルドおよび実行に使用"を Gradle に戻しておく

### build
- amazonが SDKを zip形式で提供している
  - ここからダウンロード：https://webservices.amazon.com/paapi5/documentation/with-sdk.html
  - 展開してできた `*.jar` を libsディレクトリにいれる
  - `forms_rt.jar`を 同じlibsディレクトリにいれる
    - `forms_rt.jar`の場所は `/Applications/IntelliJ IDEA CE.app/Contents/lib`
- `./gradlew build`
- fatJarの場合は `./gradlew shadowJar`

### run
- 実行前に環境変数を設定すること
    - `PA_ACCESS_KEY` amazon API access key
    - `PA_SECRET_KEY`  amazon API secret key
    - `PA_ASSOCIATE_TAG`  アフィアソシエイトtag (例 gikohadiary-22)
- `java -jar AmazonAffiMaker.jar`
