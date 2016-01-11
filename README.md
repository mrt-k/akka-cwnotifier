# akka-cwnotifier

ChatWorkの期限切れタスクを通知するakka-httpサーバー

---

### Setup

環境変数に以下の3つを設定する必要があります。

* API_KEY - ChatWorkのAPIキーです
* ROOM_ID - 通知先のroom_idです
* PORT - 待ち受けるポート番号(デフォルトは8080)

###### ローカルで動かす場合

ポート8000番で動かしたいとき。

```
$ export PORT=8000
$ export API_KEY="apikey"
$ export ROOM_ID="room_id"
```

###### herokuで動かす場合

herokuの場合はPORTは設定しなくて構いません。

```
$ heroku config:set API_KEY="apikey"
$ heroku config:set ROOM_ID="room_id"
```


### Run

環境変数に設定したポート番号でサーバーが起動します。  
http://localhost:8000/ などにアクセスすれば通知が届きます。

###### ローカルで動かす場合

```
$ sbt run
```

###### herokuにデプロイする

```
$ git clone https://github.com/mrt-k/akka-cwnotifier.git
$ cd akka-cwnotifier
$ heroku create
$ git push heroku master
```
