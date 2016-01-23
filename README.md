# akka-cwnotifier

ChatWorkの期限切れタスクを通知するakka-httpサーバー

---

# Deploy on Heroku

[![Heroku Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy?template=https://github.com/mrt-k/akka-cwnotifier/tree/master)

---


# Setup

環境変数に以下の3つを設定する必要があります。

* API_KEY - ChatWorkのAPIキーです
* MY_ROOM_ID - 自分のroom_idです
* ROOM_IDS - 送信したいroomd_idです。「:」(セミコロン)で区切ります。 ex) $ export ROOM_IDS="12345:67890"
* PORT - 待ち受けるポート番号(デフォルトは8080)

### ローカルで動かす場合

ポート8000番で動かしたいとき。

```
$ export PORT=8000
$ export API_KEY="apikey"
$ export MY_ROOM_ID="room_id"
$ export ROOM_IDS="room_id1:room_id2"
```


### herokuで動かす場合

herokuの場合はPORTは設定しなくて構いません。

```
$ heroku config:set API_KEY="apikey"
$ heroku config:set MY_ROOM_ID="room_id"
$ heroku config:set ROOM_IDS="room_id1:room_id2"
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


