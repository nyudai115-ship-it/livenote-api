# LiveNote API ドキュメント

## 認証

- POST `/api/v1/users/signup` : ユーザー新規登録
- POST `/api/v1/users/login` : ログイン

## アーティスト

- GET `/api/v1/artists` : アーティスト一覧取得
- POST `/api/v1/artists` : アーティスト追加
- PUT `/api/v1/artists/{id}` : アーティスト編集
- DELETE `/api/v1/artists/{id}` : アーティスト削除
- GET `/api/v1/artists/{id}` : アーティスト個別取得

## アルバム

- GET `/api/v1/albums` : アルバム一覧取得
- POST `/api/v1/albums` : アルバム追加
- PUT `/api/v1/albums/{id}` : アルバム編集
- DELETE `/api/v1/albums/{id}` : アルバム削除

## 曲

- GET `/api/v1/songs` : 曲一覧取得
- POST `/api/v1/songs` : 曲追加

## ニュース

- GET `/api/v1/news/external?artistName={name}&page={page}` : ニュース取得（jpop/kpop/アーティスト名指定）

## お気に入り

- POST `/api/v1/favorite-news/toggle` : ニュース記事のお気に入り追加/解除
- GET `/api/v1/favorite-news/list?user={username}` : お気に入り記事一覧取得

## デプロイ手順

1. `mvn clean package` で jar ファイル作成
2. `java -jar target/livenote-0.0.1-SNAPSHOT.jar` で起動
3. MySQL 等 DB 設定は `application.properties` で管理
4. サーバー起動後 `http://localhost:8080/` でアクセス

---

API 仕様やデプロイ手順は README にも記載推奨
