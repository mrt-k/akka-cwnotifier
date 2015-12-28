# akka-ChatWorkTaskNotifier

ChatWorkの期限切れタスクを通知するakka-httpサーバー

---

### Setup

環境変数に以下の2つを設定する必要があります。

* API_KEY - ChatWorkのAPIキーです
* ROOM_ID - 通知先のroom_idです

###### ローカルで動かす場合

ローカルで動かす場合はサーバーのポートも環境変数で設定しておく必要があります。

```
$ export PORT=8000
$ export API_KEY="apikey"
$ export ROOM_ID="room_id"
```

###### herokuで動かす場合

```
heroku config:set API_KEY="apikey"
heroku config:set ROOM_ID="room_id"
```


### Run

環境変数に設定したポート番号でサーバーが起動します。  
http://localhost:8000/ などにアクセスすれば通知が届きます。

###### ローカルで動かす場合

```
$ sbt run
```

###### herokuにデプロイする


